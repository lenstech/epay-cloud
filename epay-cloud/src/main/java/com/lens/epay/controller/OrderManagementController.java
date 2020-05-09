package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.enums.Role;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.service.OrderService;
import com.lens.epay.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

import static com.lens.epay.constant.GeneralConstants.DTO_DATE_FORMAT;
import static com.lens.epay.constant.GeneralConstants.DTO_DATE_TIME_FORMAT;

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

    @PutMapping("/decline-remittance")
    @ApiOperation("Seller declines remittance infos of the order. Customer should provide new info")
    public ResponseEntity<OrderResource> declineRemittance(@RequestParam UUID orderId,
                                                           @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(orderService.declineRemittance(orderId));
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
                                                      @RequestParam @DateTimeFormat(pattern = DTO_DATE_TIME_FORMAT) Date shippedDate,
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


    @GetMapping("/fraud")
    @ApiOperation("Admin can update fraud suspect transactions. The check is automatically done at the beginning of every hour")
    public void fraud(@RequestHeader("Authorization") String token){
        authorizationConfig.permissionCheck(token, Role.FIRM_ADMIN);
        orderService.checkFraudControlResult();
    }

    @GetMapping("/report")
    @ApiOperation("Report of orders")
    public ResponseEntity<Page<OrderResource>> orderReport(@RequestParam int pageNumber,
                                                           @RequestParam(required = false) Boolean desc,
                                                           @RequestParam(required = false) String sortBy,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = DTO_DATE_FORMAT) Date startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = DTO_DATE_FORMAT) Date endDate,
                                                           @RequestParam(required = false) OrderStatus orderStatus,
                                                           @RequestParam(required = false) PaymentType paymentType,
                                                           @RequestParam(required = false) String cargoFirm,
                                                           @RequestParam(required = false) String remittanceBank,
                                                           @RequestParam(required = false) Boolean paid,
                                                           @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.FIRM_ADMIN);
        return ResponseEntity.ok(orderService.getOrderReport(pageNumber, desc, sortBy, startDate, endDate, orderStatus, paymentType, cargoFirm, remittanceBank, paid));
    }
}
