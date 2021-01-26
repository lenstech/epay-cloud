package com.lens.epay.model.dto.sale;

import com.iyzipay.model.Currency;
import com.lens.epay.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 18 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @NotNull(message = "Teslimat adresi boş olamaz.")
    private UUID deliveryAddressId;

    @NotNull(message = "Fatura adresi bol olamaz.")
    private UUID invoiceAddressId;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float totalProductPrice;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Integer installmentNumber;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @CreditCardNumber(message = "Kredi kartı numarasını doğru formatta giriniz.")
    private String creditCardNumber;

    @NotNull(message = "Kredi kartı sahibi ismi boş olmamalı.")
    private String creditCardHolderName;

    @Size(max = 4)
    @NotNull(message = "Kredi kartı son kullanım yılı ismi boş olmamalı.")
    private String expireYear;

    @Size(max = 2)
    @NotNull(message = "Kredi kartı son kullanım ayı ismi boş olmamalı.")
    private String expireMonth;

    @Size(max = 3)
    private String cvc;

    @NotNull(message = "Sepet Listesi boş olmamalı.")
    private List<BasketObjectDto> basketObjectDtoList;

    private String orderNote;
}
