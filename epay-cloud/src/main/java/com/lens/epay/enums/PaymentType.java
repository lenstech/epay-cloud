package com.lens.epay.enums;

import com.iyzipay.model.Payment;
import lombok.Getter;

@Getter
public enum PaymentType {
    CREDIT_CARD("kredi kartı"), REMITTANCE("havale"), PAY_AT_THE_DOOR("kapıda ödeme");

    private String turkish;

    PaymentType(String turkish) {
        this.turkish = turkish;
    }

    public String getTurkish(PaymentType paymentType){
        return paymentType.getTurkish();
    }
}
