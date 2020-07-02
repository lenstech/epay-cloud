package com.lens.epay.controller;

import com.iyzipay.model.InstallmentInfo;
import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.CreditCardInstallmentCheckDto;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.OrderService;
import com.lens.epay.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@RestController
@RequestMapping(value = "/order")
@Api(value = "Order", tags = {"Order Operations"})
public class OrderController extends AbstractController<Order, UUID, OrderDto, OrderResource> {

    @Override
    protected AbstractService<Order, UUID, OrderDto, OrderResource> getService() {
        return orderService;
    }

    @Override
    public void setSaveRole() {
        super.saveRole = Role.CUSTOMER;
    }

    @Override
    public void setGetRole() {
        super.getRole = Role.CUSTOMER;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = Role.BASIC_USER;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.ADMIN;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.ADMIN;
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JwtResolver resolver;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @PostMapping("/check-installment")
    public ResponseEntity<InstallmentInfo> checkCreditCardInstallment(@RequestBody CreditCardInstallmentCheckDto creditCardInstallmentCheckDto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.checkCardInstallment(creditCardInstallmentCheckDto));
    }

    @PutMapping("/enter-remittance-info")
    @ApiOperation("Customer enters the remittance information to System by using this endpoint")
    public ResponseEntity<OrderResource> enterRemittanceNo(@RequestParam UUID orderId,
                                                           @RequestParam String remittanceNo,
                                                           @RequestParam String remittanceBank,
                                                           @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(orderService.enterRemittanceInfo(orderId, remittanceNo, remittanceBank, resolver.getIdFromToken(token)));
    }

    @PutMapping("/approve-cargo")
    @ApiOperation("Customer approves that cargo is reached by using this endpoint")
    public ResponseEntity<OrderResource> approveCargo(@RequestParam UUID orderId,
                                                      @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(orderService.approveCargoByCustomer(orderId, resolver.getIdFromToken(token)));
    }

    @PutMapping("/cancel-request")
    @ApiOperation("Customer can create cancel request for an order. When OrderStatus is  TAKEN, APPROVED or REMITTANCE_INFO_WAITED " +
            "the cancelation is approved. However, at PREPARED_FOR_CARGO and WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER  stages," +
            " return request can not be applied. In other status Seller should " +
            " approve return request")
    public ResponseEntity<OrderResource> cancelRequestByCustomer(@RequestParam UUID orderId,
                                                                 @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(orderService.cancelRequestByCustomer(orderId, resolver.getIdFromToken(token)));
    }

    @PutMapping("/enter-return-cargo-info")
    @ApiOperation("Customer should enter return cargo information for return request")
    public ResponseEntity<OrderResource> enterReturnCargoInfo(@RequestParam UUID orderId,
                                                              @RequestParam String returnCargoFirm,
                                                              @RequestParam String returnCargoNo,
                                                              @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(orderService.enterReturnCargoInfo(orderId, resolver.getIdFromToken(token), returnCargoFirm, returnCargoNo));
    }

    @GetMapping("/get-self-orders/{pageNo}")
    @ApiOperation("Customer can reach orders of himself")
    public ResponseEntity<Page<OrderResource>> getSelfOrders(@PathVariable int pageNo,
                                                             @RequestHeader("Authorization") String token){
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(orderService.getSelfOrders(resolver.getIdFromToken(token),pageNo));
    }
}
