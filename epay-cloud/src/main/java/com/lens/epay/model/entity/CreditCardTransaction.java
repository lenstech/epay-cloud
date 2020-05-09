package com.lens.epay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 9 May 2020
 */

@Data
@Entity
@Table(name = "credit_card_transaction")
public class CreditCardTransaction extends AbstractEntity<UUID> {

    private String iyzicoPaymentId;

    private Integer iyzicoFraudStatus;

    private Float iyziCommissionFee;

    private Float iyziCommissionRateAmount;

    private String errrorMessage;

    private String ipAddress;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    private Order order;
}
