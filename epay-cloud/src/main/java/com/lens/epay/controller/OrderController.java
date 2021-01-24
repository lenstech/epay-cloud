package com.lens.epay.controller;

import com.iyzipay.model.InstallmentInfo;
import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.CreditCardInstallmentCheckDto;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.OrderService;
import com.lens.epay.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@RestController
@RequestMapping(value = "/order")
@Api(value = "Order", tags = {"Order Operations"})
public class OrderController extends AbstractController<Order, UUID, OrderDto, OrderResource> {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JwtResolver resolver;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    protected AbstractService<Order, UUID, OrderDto, OrderResource> getService() {
        return orderService;
    }

    @Override
    public Role getSaveRole() {
        return Role.CUSTOMER;
    }

    @Override
    public Role getGetRole() {
        return Role.CUSTOMER;
    }

    @Override
    public Role getGetAllRole() {
        return Role.BASIC_USER;
    }

    @Override
    public Role getUpdateRole() {
        return Role.ADMIN;
    }

    @Override
    public Role getDeleteRole() {
        return Role.ADMIN;
    }

    @Override
    @ApiOperation(value = "Send order request by using this endpoint, it can be done by authorization")
    @PostMapping
    public OrderResource save(@RequestHeader("Authorization") String token, @Valid @RequestBody OrderDto orderDto, BindingResult bindingResult, HttpServletRequest request) {
        orderDto.setIpAddress(request.getRemoteAddr());
        return super.save(token, orderDto, bindingResult, request);
    }

    @Override
    @ApiOperation(value = "Update order, it can be done by authorization")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderResource update(@RequestHeader("Authorization") String token, @Valid @RequestBody OrderDto orderDto, BindingResult bindingResult, @RequestParam UUID objectId) {
        return super.update(token, orderDto, bindingResult, objectId);
    }

    @PostMapping("/check-installment")
    public ResponseEntity<InstallmentInfo> checkCreditCardInstallment(@RequestBody @Valid CreditCardInstallmentCheckDto creditCardInstallmentCheckDto, BindingResult bindingResult, @RequestHeader("Authorization") String token) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        logger.info("checkCreditCardInstallment getSalesAgreement");
        return ResponseEntity.ok(paymentService.checkCardInstallment(creditCardInstallmentCheckDto));
    }

    @PutMapping("/enter-remittance-info")
    @ApiOperation("Customer enters the remittance information to System by using this endpoint")
    public ResponseEntity<OrderResource> enterRemittanceNo(@RequestParam UUID orderId,
                                                           @RequestParam String remittanceNo,
                                                           @RequestParam String remittanceBank,
                                                           @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting enterRemittanceNo userId: %s.", userId));
        return ResponseEntity.ok(orderService.enterRemittanceInfo(orderId, remittanceNo, remittanceBank, userId));
    }

    @PutMapping("/approve-cargo")
    @ApiOperation("Customer approves that cargo is reached by using this endpoint")
    public ResponseEntity<OrderResource> approveCargo(@RequestParam UUID orderId,
                                                      @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting approveCargo userId: %s.", userId));
        return ResponseEntity.ok(orderService.approveCargoByCustomer(orderId, userId));
    }

    @PutMapping("/cancel-request")
    @ApiOperation("Customer can create cancel request for an order. When OrderStatus is  TAKEN, APPROVED or REMITTANCE_INFO_WAITED " +
            "the cancelation is approved. However, at PREPARED_FOR_CARGO and WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER  stages," +
            " return request can not be applied. In other status Seller should " +
            " approve return request")
    public ResponseEntity<OrderResource> cancelRequestByCustomer(@RequestParam UUID orderId,
                                                                 @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting cancelRequestByCustomer userId: %s.", userId));
        return ResponseEntity.ok(orderService.cancelRequestByCustomer(orderId, userId));
    }

    @PutMapping("/enter-return-cargo-info")
    @ApiOperation("Customer should enter return cargo information for return request")
    public ResponseEntity<OrderResource> enterReturnCargoInfo(@RequestParam UUID orderId,
                                                              @RequestParam String returnCargoFirm,
                                                              @RequestParam String returnCargoNo,
                                                              @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting enterReturnCargoInfo userId: %s.", userId));
        return ResponseEntity.ok(orderService.enterReturnCargoInfo(orderId, userId, returnCargoFirm, returnCargoNo));
    }

    @GetMapping("/get-self-orders/{pageNo}")
    @ApiOperation("Customer can reach orders of himself")
    public ResponseEntity<Page<OrderResource>> getSelfOrders(@PathVariable int pageNo,
                                                             @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting getSelfOrders userId: %s.", userId));
        return ResponseEntity.ok(orderService.getSelfOrders(userId, pageNo));
    }
}
