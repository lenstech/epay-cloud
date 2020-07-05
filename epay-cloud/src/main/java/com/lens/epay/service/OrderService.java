package com.lens.epay.service;

import com.iyzipay.model.Cancel;
import com.iyzipay.model.Payment;
import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.enums.SearchOperator;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.mapper.CreditCardTransactionMapper;
import com.lens.epay.mapper.OrderMapper;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.BasketObject;
import com.lens.epay.model.entity.CreditCardTransaction;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.other.SearchCriteria;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.repository.BasketRepository;
import com.lens.epay.repository.OrderRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.repository.specifications.OrderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;
import static com.lens.epay.constant.GeneralConstants.DEFAULT_SORT_BY;
import static com.lens.epay.constant.GeneralConstants.PAGE_SIZE;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Service
@Transactional
public class OrderService extends AbstractService<Order, UUID, OrderDto, OrderResource> {

    @Override
    public OrderRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<OrderDto, Order, OrderResource> getConverter() {
        return mapper;
    }

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private CreditCardTransactionMapper transactionMapper;

    @Autowired
    private BasketRepository basketRepository;

    //Customer
    @Override
    public OrderResource save(OrderDto orderDto, UUID userId) {
        Order order = getConverter().toEntity(orderDto);
        float sum = 0F;
        for (BasketObject object : order.getBasketObjects()) {
            sum += object.getProduct().getPrice() * object.getProductQuantity();
            object.setOrder(order);
        }
        if (Math.abs(sum - order.getTotalPrice()) > 0.50) {
            throw new BadRequestException(TOTAL_PRICE_IS_NOT_CORRECT);
        }
        User user = userRepository.findUserById(userId);
        order.setOrderStatus(OrderStatus.TAKEN);
        order.setUser(user);
        order.setPaid(false);
        getRepository().save(order);
        if (orderDto.getPaymentType() == PaymentType.CREDIT_CARD) {
            CreditCardTransaction transaction = new CreditCardTransaction();
            Payment payment = paymentService.payByCard(orderDto, user, order);
            if (payment.getStatus().equals("success")) {
                order.setPaid(true);
                transaction.setIyziCommissionFee(payment.getIyziCommissionFee().floatValue());
                transaction.setIyziCommissionRateAmount(payment.getMerchantCommissionRateAmount().floatValue());
                transaction.setIpAddress(orderDto.getIpAddress());
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
    public OrderResource  setCargoInfo(String cargoNo, String cargoFirm, UUID orderId, Long epochSecond) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.APPROVED) &&
                !order.getOrderStatus().equals(OrderStatus.PREPARED_FOR_CARGO)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        order.setCargoNo(cargoNo);
        order.setCargoFirm(cargoFirm);
        order.setOrderStatus(OrderStatus.SHIPPED);

        if (epochSecond != null) {
            try {
                order.setShippedDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.of("Asia/Istanbul")));
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
        } else if (orderStatus.equals(OrderStatus.COMPLETED) || orderStatus.equals(OrderStatus.SHIPPED)){
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
                                              Boolean desc,
                                              String sortBy,
                                              Date startDate,
                                              Date endDate,
                                              OrderStatus orderStatus,
                                              PaymentType paymentType,
                                              String cargoFirm,
                                              String remittanceBank,
                                              Boolean paid) {
        PageRequest pageable;
        if (desc == null) {
            desc = true;
        }

        OrderSpecification spec = new OrderSpecification();
        if (startDate != null) {
            ZonedDateTime start = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
            spec.add(new SearchCriteria("createdDate", start, SearchOperator.FROM));
        }
        if (endDate != null) {
            ZonedDateTime end = ZonedDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
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

        try {
            if (desc) {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(sortBy).descending());
            } else {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(sortBy).ascending());
            }
            return repository.findAll(spec, pageable)
                    .map(getConverter()::toResource);
        } catch (Exception e) {
            pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            return repository.findAll(spec, pageable)
                    .map(getConverter()::toResource);
        }
    }


    /**
     * From that point user monitor will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public Page<OrderResource> getSelfOrders(UUID userId, int pageNo) {
        PageRequest pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, DEFAULT_SORT_BY);
        return getRepository().findOrdersByUserId(pageable, userId).map(getConverter()::toResource);
    }
}
