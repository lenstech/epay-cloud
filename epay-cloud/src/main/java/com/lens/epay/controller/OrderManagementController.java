package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.Role;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.service.OrderService;
import com.lens.epay.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 25 Nis 2020
 */

@RestController
@RequestMapping("/order/management")
@Api(value = "Order Management", tags = {"Order Management Operations"})
public class OrderManagementController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthorizationConfig authorizationConfig;


    @PutMapping("/approve")
    @ApiOperation("Seller approves the order. By this way, seller tells Customer that order will prepare without problem.")
    public ResponseEntity<OrderResource> approveOrder(@RequestParam UUID orderId,
                                                      @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.approveOrder(orderId));
    }

    @PutMapping("/approve-remittance")
    @ApiOperation("Seller approves remittance of the order which is came from Customer")
    public ResponseEntity<OrderResource> approveRemittance(@RequestParam UUID orderId,
                                                           @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.approveRemittance(orderId));
    }

    @PutMapping("/set-prepared-for-cargo")
    @ApiOperation("Seller changes the OrderStatus from APPROVED to PREPARED_FOR_CARGO of the order which is came from Customer")
    public ResponseEntity<OrderResource> setPreparedForCargo(@RequestParam UUID orderId,
                                                             @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.setPreparedForCargo(orderId));
    }

    @PutMapping("/set-cargo-info")
    @ApiOperation("Cargo info will be filled by seller. Date format will be: \"yyyy-MM-dd HH:mm:ss\"")
    public ResponseEntity<OrderResource> setCargoInfo(@RequestParam String cargoNo,
                                                      @RequestParam String cargoFirm,
                                                      @RequestParam UUID orderId,
                                                      @RequestParam String shippedDate,
                                                      @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.setCargoInfo(cargoNo, cargoFirm, orderId, shippedDate));
    }

    @PutMapping("/approve-cargo-reached")
    @ApiOperation("Seller approves cargo is reached to Customer by using this endpoint")
    public ResponseEntity<OrderResource> approveCargoReached(@RequestParam UUID orderId,
                                                             @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.approveCargoBySeller(orderId));
    }


    @PutMapping("/approve-pay-at-door")
    @ApiOperation("Seller approves that payment at door is completed by using this endpoint")
    public ResponseEntity<OrderResource> approvePayAtDoor(@RequestParam UUID orderId,
                                                          @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.approvePayAtDoor(orderId));
    }


    /**
     * From that point Return will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @PutMapping("/accept-return-request")
    @ApiOperation("Seller accepts that return request is acceptable by using this endpoint")
    public ResponseEntity<OrderResource> acceptReturnRequest(@RequestParam UUID orderId,
                                                             @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.acceptReturnRequest(orderId));
    }

    @PutMapping("/cancel")
    @ApiOperation("Seller can cancel the order request by using this endpoint")
    public ResponseEntity<OrderResource> cancelRequestBySeller(@RequestParam UUID orderId,
                                                               @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.cancelRequestBySeller(orderId));
    }

    @PutMapping("/approve-return-cargo")
    @ApiOperation("Seller approves that return cargo is reached back by using this endpoint")
    public ResponseEntity<OrderResource> approveReturnCargo(@RequestParam UUID orderId,
                                                            @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.approveReturnCargo(orderId));
    }

    @PutMapping("/enter-return-remittance-info")
    @ApiOperation("Seller enters the return remittance information to System by using this endpoint")
    public ResponseEntity<OrderResource> enterReturnRemittanceInfo(@RequestParam UUID orderId,
                                                                   @RequestParam String returnRemittanceNo,
                                                                   @RequestParam String returnRemittanceBankName,
                                                                   @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.enterReturnRemittanceInfo(orderId, returnRemittanceNo, returnRemittanceBankName));
    }

    /**
     * From that point Management will be considered
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @PutMapping("/set-status")
    @ApiOperation("Admin can set status of order by manually")
    public ResponseEntity<OrderResource> setOrderStatus(@RequestParam UUID orderId,
                                                        @RequestParam OrderStatus orderStatus,
                                                        @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.ADMIN);
        return ResponseEntity.ok(orderService.setStatus(orderStatus, orderId));
    }
}
