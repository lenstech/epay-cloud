package com.lens.epay.enums;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

public enum OrderStatus {
    TAKEN,
    WAIT_FOR_FRAUD_CONTROL,
    REJECTED_FROM_FRAUD_CONTROLLER,
    APPROVED,
    PREPARED_FOR_CARGO,
    REMITTANCE_INFO_WAITED,
    WAIT_FOR_APPROVE_REMITTANCE_BY_SELLER,
    SHIPPED,
    COMPLETED,
    WAIT_FOR_APPROVE_PAY_AT_THE_DOOR_BY_SELLER,
    RETURN_REQUEST_SELLER_ACCEPT_WAITED,
    RETURN_CARGO_INFO_WAITED,
    BACK_SHIPPED,
    RETURN_REMITTANCE_IS_WAITED,
    REPAID,
    CANCELLED_BY_CUSTOMER_BEFORE_SHIPPED,
    CANCELLED_BY_SELLER_BEFORE_SHIPPED
}
