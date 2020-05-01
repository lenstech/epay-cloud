package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.OrderMapper;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.BasketObject;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.repository.BasketRepository;
import com.lens.epay.repository.OrderRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;

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
        if (orderDto.getPaymentType() == PaymentType.CREDIT_CARD) {
            if (paymentService.payByCard(orderDto, userId)) {
                order.setPaid(true);
            } else {
                throw new BadRequestException(PAYMENT_IS_UNSUCCESSFUL);
            }
        } else {
            order.setPaid(false);
        }
        order.setOrderStatus(OrderStatus.TAKEN);
        order.setUser(userRepository.findUserById(userId));
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    //Seller
    // TODO: 23 Nis 2020 updatei gözden geçir
    @Override
    public OrderResource put(UUID id, OrderDto updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        Order theReal = getRepository().findById(id).orElseThrow(() -> new BadRequestException(ID_IS_NOT_EXIST));
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
    public OrderResource enterRemittanceNo(UUID orderId, String remittanceNo, String remittanceBank, UUID userId) {
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
    public OrderResource setCargoInfo(String cargoNo, String cargoFirm, UUID orderId, String shippedDate) {
        Order order = getRepository().getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.APPROVED) &&
                !order.getOrderStatus().equals(OrderStatus.PREPARED_FOR_CARGO)) {
            throw new BadRequestException(NOT_APPROPRIATE_ORDER_STATUS);
        }
        order.setCargoNo(cargoNo);
        order.setCargoFirm(cargoFirm);
        order.setOrderStatus(OrderStatus.SHIPPED);

        if (shippedDate != null) {
            try {
                order.setShippedDate(DateUtil.stringToZonedDateTime(shippedDate));
            } catch (Exception e) {
                order.setShippedDate(ZonedDateTime.now());
            }
        } else {
            order.setShippedDate(ZonedDateTime.now());
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
                    paymentService.repayByCard(orderId);
                    order.setOrderStatus(OrderStatus.REPAID);
                    order.setRepaid(true);
                } else {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                }
            } else {
                order.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER_BEFORE_SHIPPED);
            }
        } else if (orderStatus.equals(OrderStatus.PREPARED_FOR_CARGO) || orderStatus.equals(OrderStatus.WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER)) {
            throw new BadRequestException(NOT_APPROPRIATE_CANCEL_AT_THIS_POINT);
        } else {
            order.setOrderStatus(OrderStatus.RETURN_REQUEST_SELLER_ACCEPT_WAITED);
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
                    paymentService.repayByCard(orderId);
                    order.setOrderStatus(OrderStatus.REPAID);
                    order.setRepaid(true);
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
            paymentService.repayByCard(orderId);
            order.setOrderStatus(OrderStatus.REPAID);
            order.setRepaid(true);
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
    //SELLER
    public OrderResource setStatus(OrderStatus orderStatus, UUID orderId) {
        Order order = getRepository().getOne(orderId);
        order.setOrderStatus(orderStatus);
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    // TODO: 19 Nis 2020 repaid remittanceDan sonrası
    // TODO: 19 Nis 2020 graph çiz
    // TODO: 25 Nis 2020 order of an user
    // TODO: 25 Nis 2020 report (belli tarih arasında
}
