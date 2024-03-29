package com.lens.epay.service;

import com.iyzipay.model.Cancel;
import com.iyzipay.model.Payment;
import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.enums.SearchOperator;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.mapper.OrderMapper;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.*;
import com.lens.epay.model.other.SearchCriteria;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.repository.OrderRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.repository.specifications.OrderSpecification;
import com.lens.epay.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;
import static com.lens.epay.constant.HttpSuccessMessagesConstants.MAIL_SEND_YOUR_SUCCESSFULLY;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Service
@Transactional
public class OrderService extends AbstractService<Order, UUID, OrderDto, OrderResource> {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private MailUtil mailUtil;

    @Value("${order.info.reciever}")
    private String orderReceiver;

    @Override
    public OrderRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<OrderDto, Order, OrderResource> getConverter() {
        return mapper;
    }

    //Customer
    @Override
    public OrderResource save(OrderDto orderDto, UUID userId) {
        Order order = getConverter().toEntity(orderDto);
        LocalDateTime date = LocalDateTime.now();
        if (Integer.parseInt(orderDto.getExpireYear()) < date.getYear() ||
                (Integer.parseInt(orderDto.getExpireYear()) == date.getYear() && Integer.parseInt(orderDto.getExpireMonth()) < date.getMonthValue())) {
            throw new BadRequestException(ErrorConstants.CARD_EXPIRED);
        }
        float sum = 0F;
        for (BasketObject object : order.getBasketObjects()) {
            Product product = object.getProduct();
            if (product.getDiscountedPrice() == null) {
                sum += object.getProduct().getPrice() * object.getProductQuantity();
            } else {
                sum += object.getProduct().getDiscountedPrice() * object.getProductQuantity();
            }
            object.setOrder(order);
        }
        if (order.getTotalProductPrice() == null) {
            order.setTotalProductPrice(sum);
        } else if (Math.abs(sum - order.getTotalProductPrice()) > 0.50) {
            throw new BadRequestException(TOTAL_PRICE_IS_NOT_CORRECT);
        }
        if (order.getTotalProductPrice() >= 160) {
            order.setDeliveryFee(0F);
        } else {
            order.setDeliveryFee(14F);
        }
        order.setTotalPrice(order.getTotalProductPrice() + order.getDeliveryFee());
        User user = userRepository.findUserById(userId);
        order.setOrderStatus(OrderStatus.TAKEN);
        order.setUser(user);
        order.setPaid(false);
        getRepository().save(order);
        if (orderDto.getPaymentType() == PaymentType.CREDIT_CARD) {
            CreditCardTransaction transaction = new CreditCardTransaction();
            Payment payment = paymentService.payByCard(orderDto, user, order);
            if ("success".equals(payment.getStatus())) {
                order.setPaid(true);
                transaction.setIyziCommissionFee(payment.getIyziCommissionFee().floatValue());
                transaction.setIyziCommissionRateAmount(payment.getMerchantCommissionRateAmount().floatValue());
                transaction.setIpAddress(orderDto.getIpAddress());
                try {
                    mailUtil.sendOrderInfoMailToCustomer(user.getEmail());
                    logger.info("Customer " + MAIL_SEND_YOUR_SUCCESSFULLY + " to: " + user.getEmail());
                } catch (Exception e) {
                    logger.info("Customer " + MAIL_SEND_FAILED + " to: " + user.getEmail());
                }
                try {
                    mailUtil.sendOrderInfoMailToSeller(orderReceiver, user.getName() + " " + user.getSurname(), payment.getPaidPrice());
                    logger.info("Seller " + MAIL_SEND_YOUR_SUCCESSFULLY + " to: " + orderReceiver);
                } catch (Exception e) {
                    logger.info("Seller " + MAIL_SEND_FAILED);
                }
            } else if (payment.getFraudStatus() != null && payment.getFraudStatus() == 0) {
                order.setPaid(false);
                transaction.setErrrorMessage(payment.getErrorMessage());
                order.setOrderStatus(OrderStatus.WAIT_FOR_FRAUD_CONTROL);
            } else {
                throw new BadRequestException(payment.getErrorMessage());
            }
            transaction.setIyzicoPaymentId(payment.getPaymentId());
            transaction.setIpAddress(orderDto.getIpAddress());
            transaction.setIyzicoFraudStatus(payment.getFraudStatus());
            transaction.setOrder(order);
            order.setCreditCardTransaction(transaction);
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    @Scheduled(cron = "0 0 * * *")
    public void checkFraudControlResult() {
        List<Order> orders = getRepository().findOrdersByCreditCardTransactionIyzicoFraudStatus(0);
        if (orders == null) {
            return;
        }
        for (Order order : orders) {
            if (!order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
                continue;
            }
            Payment payment = paymentService.getPayment(order.getCreditCardTransaction());
            if (payment.getFraudStatus() == 1) {
                order.setPaid(true);
                CreditCardTransaction transaction = order.getCreditCardTransaction();
                transaction.setIyziCommissionFee(payment.getIyziCommissionFee().floatValue());
                transaction.setIyziCommissionRateAmount(payment.getMerchantCommissionRateAmount().floatValue());
                transaction.setIyzicoPaymentId(payment.getPaymentId());
                transaction.setIyzicoFraudStatus(payment.getFraudStatus());
                order.setCreditCardTransaction(transaction);
                order.setOrderStatus(OrderStatus.TAKEN);
            } else if (payment.getFraudStatus() == -1) {
                order.setOrderStatus(OrderStatus.REJECTED_FROM_FRAUD_CONTROLLER);
            }
        }
    }

    @Override
    public OrderResource put(UUID id, OrderDto updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        Order theReal = getRepository().findById(id).orElseThrow(() -> new NotFoundException(ID_IS_NOT_EXIST));
        if (updatedDto == null) {
            throw new BadRequestException(DTO_CANNOT_BE_EMPTY);
        }
        if (!theReal.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        try {
            Order updated = getConverter().toEntity(updatedDto);
            updated.setId(theReal.getId());
            updated.setCreatedDate(theReal.getCreatedDate());
            updated.setUser(userRepository.findUserById(userId));
            updated.setPaid(theReal.getPaid());
            updated.setOrderStatus(theReal.getOrderStatus());
            updated.setCargoFirm(theReal.getCargoFirm());
            updated.setCargoNo(theReal.getCargoNo());
            return getConverter().toResource(getRepository().save(updated));
        } catch (Exception e) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
    }

    //Seller
    public OrderResource approveOrder(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.TAKEN) &&
                !order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            return getConverter().toResource(order);
        } else if (order.getPaid()) {
            order.setOrderStatus(OrderStatus.APPROVED);
        } else if (order.getPaymentType().equals(PaymentType.REMITTANCE)) {
            order.setOrderStatus(OrderStatus.REMITTANCE_INFO_WAITED);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    //Customer
    public OrderResource enterRemittanceInfo(UUID orderId, String remittanceNo, String remittanceBank, UUID userId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        if (!order.getOrderStatus().equals(OrderStatus.TAKEN) &&
                !order.getOrderStatus().equals(OrderStatus.REMITTANCE_INFO_WAITED) &&
                !order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getPaymentType().equals(PaymentType.REMITTANCE)) {
            order.setRemittanceNo(remittanceNo);
            order.setRemittanceBank(remittanceBank);
            order.setOrderStatus(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    //Seller
    public OrderResource approveRemittance(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.TAKEN) &&
                !order.getOrderStatus().equals(OrderStatus.REMITTANCE_INFO_WAITED) &&
                !order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (!order.getPaymentType().equals(PaymentType.REMITTANCE)) {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setPaid(true);
        return getConverter().toResource(getRepository().save(order));
    }

    public OrderResource declineRemittance(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.TAKEN) &&
                !order.getOrderStatus().equals(OrderStatus.REMITTANCE_INFO_WAITED) &&
                !order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (!order.getPaymentType().equals(PaymentType.REMITTANCE)) {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        order.setOrderStatus(OrderStatus.REMITTANCE_INFO_WAITED);
        return getConverter().toResource(getRepository().save(order));
    }

    //SELLER
    public OrderResource setPreparedForCargo(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.APPROVED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (!order.getPaid() && !order.getPaymentType().equals(PaymentType.PAY_AT_THE_DOOR)) {
            throw new BadRequestException(PAYMENT_IS_NOT_COMPLETED_YET);
        }
        order.setOrderStatus(OrderStatus.PREPARED_FOR_CARGO);
        return getConverter().toResource(getRepository().save(order));
    }

    //SELLER
    public OrderResource setCargoInfo(String cargoNo, String cargoFirm, UUID orderId, Long epochMilli) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.APPROVED) &&
                !order.getOrderStatus().equals(OrderStatus.PREPARED_FOR_CARGO)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        order.setCargoNo(cargoNo);
        order.setCargoFirm(cargoFirm);
        order.setOrderStatus(OrderStatus.SHIPPED);

        if (epochMilli != null) {
            try {
                order.setShippedDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("Asia/Istanbul")));
            } catch (Exception e) {
                order.setShippedDate(ZonedDateTime.now(ZoneId.of("Asia/Istanbul")));
            }
        } else {
            order.setShippedDate(ZonedDateTime.now(ZoneId.of("Asia/Istanbul")));
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    //Customer
    public OrderResource approveCargoByCustomer(UUID orderId, UUID userId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        if (order.getPaymentType().equals(PaymentType.PAY_AT_THE_DOOR)) {
            order.setOrderStatus(OrderStatus.WAIT_FOR_APPROVE_PAY_AT_THE_DOOR_BY_SELLER);
        } else {
            order.setOrderStatus(OrderStatus.COMPLETED);
            getRepository().save(order);
        }
        return getConverter().toResource(order);
    }

    //    SELLER
    public OrderResource approveCargoBySeller(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getPaymentType().equals(PaymentType.PAY_AT_THE_DOOR)) {
            order.setOrderStatus(OrderStatus.WAIT_FOR_APPROVE_PAY_AT_THE_DOOR_BY_SELLER);
        } else {
            order.setOrderStatus(OrderStatus.COMPLETED);
            getRepository().save(order);
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    //    SELLER
    public OrderResource approvePayAtDoor(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.SHIPPED) && !order.getOrderStatus().equals(OrderStatus.WAIT_FOR_APPROVE_PAY_AT_THE_DOOR_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getPaymentType().equals(PaymentType.PAY_AT_THE_DOOR)) {
            order.setPaid(true);
            order.setOrderStatus(OrderStatus.COMPLETED);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        return getConverter().toResource(getRepository().save(order));
    }


    /**
     * From that point Return will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    //CUSTOMER
    public OrderResource cancelRequestByCustomer(UUID orderId, UUID userId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        OrderStatus orderStatus = order.getOrderStatus();
        if (order.getRepaid()) {
            throw new BadRequestException(REPAYMENT_ALREADY_COMPLETED);
        }

        if (orderStatus.equals(OrderStatus.TAKEN) || orderStatus.equals(OrderStatus.APPROVED) || orderStatus.equals(OrderStatus.REMITTANCE_INFO_WAITED)) {

            if (order.getPaid()) {
                if (order.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
                    Cancel cancel = paymentService.repayByCard(order);
                    if (cancel.getStatus().equals("success")) {
                        order.setOrderStatus(OrderStatus.REPAID);
                        order.setRepaid(true);
                    } else {
                        throw new BadRequestException(cancel.getErrorMessage());
                    }
                } else {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                }
            } else {
                order.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER_BEFORE_SHIPPED);
            }
        } else if (orderStatus.equals(OrderStatus.COMPLETED) || orderStatus.equals(OrderStatus.SHIPPED)) {
            order.setOrderStatus(OrderStatus.RETURN_REQUEST_SELLER_ACCEPT_WAITED);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_CANCEL_AT_THIS_POINT);
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    //SELLER
    public OrderResource acceptReturnRequest(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.RETURN_REQUEST_SELLER_ACCEPT_WAITED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        order.setOrderStatus(OrderStatus.RETURN_CARGO_INFO_WAITED);
        return getConverter().toResource(getRepository().save(order));
    }

    //CUSTOMER
    public OrderResource enterReturnCargoInfo(UUID orderId, UUID userId, String returnCargoFirm, String returnCargoNo) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.RETURN_CARGO_INFO_WAITED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        order.setReturnCargoFirm(returnCargoFirm);
        order.setReturnCargoNo(returnCargoNo);
        order.setOrderStatus(OrderStatus.BACK_SHIPPED);
        return getConverter().toResource(getRepository().save(order));
    }

    //SELLER
    public OrderResource cancelRequestBySeller(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus.equals(OrderStatus.TAKEN) || orderStatus.equals(OrderStatus.APPROVED) || orderStatus.equals(OrderStatus.PREPARED_FOR_CARGO) || orderStatus.equals(OrderStatus.REMITTANCE_INFO_WAITED)) {
            if (order.getPaid()) {
                if (order.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
                    Cancel cancel = paymentService.repayByCard(order);
                    if (cancel.getStatus().equals("success")) {
                        order.setOrderStatus(OrderStatus.REPAID);
                        order.setRepaid(true);
                    } else {
                        throw new BadRequestException(cancel.getErrorMessage());
                    }
                } else {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                }
            } else {
                order.setOrderStatus(OrderStatus.CANCELLED_BY_SELLER_BEFORE_SHIPPED);
            }
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_CANCEL_AT_THIS_POINT);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    //SELLER
    public OrderResource approveReturnCargo(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (!orderStatus.equals(OrderStatus.SHIPPED) && !orderStatus.equals(OrderStatus.RETURN_CARGO_INFO_WAITED) && !orderStatus.equals(OrderStatus.BACK_SHIPPED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getPaymentType().equals(PaymentType.CREDIT_CARD) && order.getPaid() && !order.getRepaid()) {
            Cancel cancel = paymentService.repayByCard(order);
            if (cancel.getStatus().equals("success")) {
                order.setOrderStatus(OrderStatus.REPAID);
                order.setRepaid(true);
            } else {
                throw new BadRequestException(cancel.getErrorMessage());
            }
        } else {
            if (order.getPaid() && !order.getRepaid()) {
                order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
            } else {
                order.setOrderStatus(OrderStatus.REPAID);
                order.setRepaid(true);
            }
        }
        return getConverter().toResource(getRepository().save(order));
    }

    //SELLER
    public OrderResource enterReturnRemittanceInfo(UUID orderId, String returnRemittanceNo, String bankName) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.RETURN_REMITTANCE_IS_WAITED)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        if (order.getPaid()) {
            order.setReturnRemittanceNo(returnRemittanceNo);
            order.setReturnRemittanceBank(bankName);
            order.setOrderStatus(OrderStatus.REPAID);
            order.setRepaid(true);
        } else {
            throw new BadRequestException(PAYMENT_IS_NOT_OCCURED);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    /**
     * From that point Management will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    //ADMIN
    public OrderResource setStatus(OrderStatus orderStatus, UUID orderId) {
        Order order = getRepository().getOne(orderId);
        order.setOrderStatus(orderStatus);
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    //SELLER
    public Page<OrderResource> getOrderReport(int pageNumber,
                                              boolean desc,
                                              String sortBy,
                                              Long startDateEpochMilliSecond,
                                              Long endDateEpochMilliSecond,
                                              OrderStatus orderStatus,
                                              PaymentType paymentType,
                                              String cargoFirm,
                                              String remittanceBank,
                                              Boolean paid) {

        OrderSpecification spec = new OrderSpecification();
        if (startDateEpochMilliSecond != null) {
            ZonedDateTime start = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startDateEpochMilliSecond), ZoneId.of("Asia/Istanbul"));
            spec.add(new SearchCriteria("createdDate", start, SearchOperator.FROM));
        }
        if (endDateEpochMilliSecond != null) {
            ZonedDateTime end = ZonedDateTime.ofInstant(Instant.ofEpochMilli(endDateEpochMilliSecond), ZoneId.of("Asia/Istanbul"));
            spec.add(new SearchCriteria("createdDate", end, SearchOperator.TO));
        }
        if (orderStatus != null) {
            spec.add(new SearchCriteria("orderStatus", orderStatus, SearchOperator.EQUAL));
        }
        if (paymentType != null) {
            spec.add(new SearchCriteria("paymentType", paymentType, SearchOperator.EQUAL));
        }
        if (cargoFirm != null) {
            spec.add(new SearchCriteria("cargoFirm", cargoFirm, SearchOperator.MATCH));
        }
        if (remittanceBank != null) {
            spec.add(new SearchCriteria("remittanceBank", remittanceBank, SearchOperator.MATCH));
        }
        if (paid != null) {
            spec.add(new SearchCriteria("paid", true, SearchOperator.EQUAL));
        }
        PageRequest pageable = getPageable(pageNumber, sortBy, desc);
        return repository.findAll(spec, pageable).map(mapper::toResource);
    }


    /**
     * From that point user monitor will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public Page<OrderResource> getSelfOrders(UUID userId, int pageNo) {
        PageRequest pageable = getPageable(pageNo);
        return getRepository().findOrdersByUserId(pageable, userId).map(getConverter()::toResource);
    }
}
