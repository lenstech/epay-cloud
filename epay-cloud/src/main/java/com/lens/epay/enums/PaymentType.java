package com.lens.epay.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
    CREDIT_CARD("kredi kartı"), REMITTANCE("havale"), PAY_AT_THE_DOOR("kapıda ödeme");

    private final String turkish;

    PaymentType(String turkish) {
        this.turkish = turkish;
    }

    public String getTurkish(PaymentType paymentType) {
        return paymentType.getTurkish();
    }
}
