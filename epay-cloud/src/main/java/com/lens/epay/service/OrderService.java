package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Service
public class OrderService extends AbstractService<Order, UUID, OrderDto, OrderResource> {

    @Override
    public JpaRepository<Order, UUID> getRepository() {
        return null;
    }

    @Override
    public Converter<OrderDto, Order, OrderResource> getConverter() {
        return null;
    }

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OrderResource save(OrderDto orderDto, UUID userId) {
        Order order = getConverter().toEntity(orderDto);
        if (orderDto.getPaymentType() == PaymentType.CREDIT_CARD) {
            if (paymentService.payByCard(orderDto, userId)) {
                order.setPaid(true);
            }
        }
        order.setUser(userRepository.findUserById(userId));
        return getConverter().toResource(getRepository().save(order));
    }

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

    public OrderResource setCargoInfo(String cargoNo, String cargoFirm, UUID orderId, UUID userId, @Nullable ZonedDateTime shippedDate) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        order.setCargoNo(cargoNo);
        order.setCargoFirm(cargoFirm);
        order.setOrderStatus(OrderStatus.SHIPPED);
        if (shippedDate != null) {
            order.setShippedDate(shippedDate);
        } else {
            order.setShippedDate(ZonedDateTime.now());
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    public OrderResource setStatus(OrderStatus orderStatus, UUID orderId, UUID userId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        order.setOrderStatus(orderStatus);
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    public OrderResource cancelRequestByCustomer(UUID orderId, UUID userId) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus.equals(OrderStatus.TAKEN) || orderStatus.equals(OrderStatus.APPROVED) || orderStatus.equals(OrderStatus.REMITTANCE_WAITED)) {
            if (order.getPaymentType().equals(PaymentType.CREDIT_CARD) && order.getPaid()) {
                paymentService.repayByCard(orderId);
                order.setOrderStatus(OrderStatus.REPAID);
            } else {
                if (order.getPaid()) {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                } else {
                    order.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER);
                }
            }
        } else if (orderStatus.equals(OrderStatus.PREPARED_FOR_CARGO)) {
            throw new BadRequestException(NOT_APPROPRIATE_CANCEL_AT_THIS_POINT);
        } else {
            order.setOrderStatus(OrderStatus.RETURN_SHIPMENT_INFO_WAITED);
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    public OrderResource enterReturnShipmentInfo(UUID orderId, UUID userId, String returnCargoFirm, String returnCargoNo) {
        Order order = getRepository().getOne(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER);
        }
        order.setReturnCargoFirm(returnCargoFirm);
        order.setReturnCargoNo(returnCargoNo);
        order.setOrderStatus(OrderStatus.BACK_SHIPPED);
        return getConverter().toResource(getRepository().save(order));
    }


    public OrderResource cancelRequestBySeller(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus.equals(OrderStatus.TAKEN) || orderStatus.equals(OrderStatus.APPROVED) || orderStatus.equals(OrderStatus.PREPARED_FOR_CARGO) || orderStatus.equals(OrderStatus.REMITTANCE_WAITED)) {
            if (order.getPaymentType().equals(PaymentType.CREDIT_CARD) && order.getPaid()) {
                paymentService.repayByCard(orderId);
                order.setOrderStatus(OrderStatus.REPAID);
            } else {
                if (order.getPaid()) {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                } else {
                    order.setOrderStatus(OrderStatus.CANCELLED_BY_SELLER);
                }
            }
        } else {
            order.setOrderStatus(OrderStatus.CANCELLED_BY_SELLER);
        }
        getRepository().save(order);
        return getConverter().toResource(order);
    }

    public OrderResource approveReturnCargo(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus.equals(OrderStatus.SHIPPED) || orderStatus.equals(OrderStatus.RETURN_SHIPMENT_INFO_WAITED) || orderStatus.equals(OrderStatus.BACK_SHIPPED)) {
            if (order.getPaymentType().equals(PaymentType.CREDIT_CARD) && order.getPaid()) {
                paymentService.repayByCard(orderId);
                order.setOrderStatus(OrderStatus.REPAID);
            } else {
                if (order.getPaid()) {
                    order.setOrderStatus(OrderStatus.RETURN_REMITTANCE_IS_WAITED);
                } else {
                    order.setOrderStatus(OrderStatus.REPAID);
                }
            }
        }
        return getConverter().toResource(getRepository().save(order));
    }

    public OrderResource enterRemittanceNo(UUID orderId, String remittanceNo) {
        Order order = getRepository().getOne(orderId);
        if (order.getPaymentType().equals(PaymentType.REMITTANCE)) {
            order.setRemittanceNo(remittanceNo);
            order.setPaid(true);
            order.setOrderStatus(OrderStatus.APPROVED);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    public OrderResource enterReturnRemittanceNo(UUID orderId, String returnRemittanceNo) {
        Order order = getRepository().getOne(orderId);
        OrderStatus status = order.getOrderStatus();
        if (status.equals(OrderStatus.RETURN_REMITTANCE_IS_WAITED) && order.getPaymentType().equals(PaymentType.REMITTANCE) && order.getPaid()) {
            order.setReturnRemittanceNo(returnRemittanceNo);
            order.setOrderStatus(OrderStatus.REPAID);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        return getConverter().toResource(getRepository().save(order));
    }

    public OrderResource approvePayAtDoor(UUID orderId) {
        Order order = getRepository().getOne(orderId);
        if (order.getPaymentType().equals(PaymentType.PAY_AT_THE_DOOR)) {
            order.setPaid(true);
            order.setOrderStatus(OrderStatus.COMPLETED);
        } else {
            throw new BadRequestException(NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE);
        }
        return getConverter().toResource(getRepository().save(order));
    }


    // TODO: 19 Nis 2020 repaid remittanceDan sonrası
    // TODO: 19 Nis 2020 graph çiz
}
