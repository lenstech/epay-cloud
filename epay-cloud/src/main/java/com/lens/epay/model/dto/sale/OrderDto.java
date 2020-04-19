package com.lens.epay.model.dto.sale;

import com.lens.epay.enums.PaymentType;
import com.lens.epay.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.Map;
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

    private UUID deliveryAddressId;

    private UUID invoiceAddressId;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @CreditCardNumber
    private String  creditCardNumber;

    private String creditCardHolderName;

    @Size(max = 2)
    private String expireYear;
    @Size(max = 2)
    private String expireMonth;

    @Size(max = 3)
    private String cvc;

    @ElementCollection
    private Map<Product, Short> productQuantity;

    private Boolean paid;

    private String remittanceNo;

    private String returnRemittanceNo;

}
