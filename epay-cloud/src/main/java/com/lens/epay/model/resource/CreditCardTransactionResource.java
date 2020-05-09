package com.lens.epay.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 9 May 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardTransactionResource {

    private String iyzicoPaymentId;

    private Integer iyzicoFraudStatus;

    private Float iyziCommissionFee;

    private Float iyziCommissionRateAmount;

    private String errorMessage;

    private String ipAddress;
}
