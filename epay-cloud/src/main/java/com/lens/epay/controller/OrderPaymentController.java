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
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@RestController
@RequestMapping("/order")
@Api(value = "Order", tags = {"Order and Payment Operations"})
public class OrderPaymentController extends AbstractController<Order, UUID, OrderDto, OrderResource> {

    @Override
    protected AbstractService<Order, UUID, OrderDto, OrderResource> getService() {
        return orderService;
    }

    @Override
    public void setMinRole() {
        minRole = Role.CUSTOMER;
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

    @PostMapping("/set-cargo-info")
    @ApiOperation("Cargo info will be filled by seller")
    public ResponseEntity<OrderResource> setCargoInfo(@RequestParam String cargoNo,
                                                      @RequestParam String cargoFirm,
                                                      @RequestParam UUID orderId,
                                                      @RequestParam @Nullable ZonedDateTime shippedDate,
                                                      @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.setCargoInfo(cargoNo, cargoFirm, orderId, resolver.getIdFromToken(token), shippedDate));
    }
}
