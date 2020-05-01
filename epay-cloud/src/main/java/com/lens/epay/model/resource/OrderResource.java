package com.lens.epay.model.resource;

import com.lens.epay.common.AbstractResource;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.model.resource.product.BasketObjectResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResource extends AbstractResource {

    private MinimalUserResource user;

    private OrderStatus orderStatus;

    private ZonedDateTime shippedDate;

    private String cargoNo;

    private String cargoFirm;

    private String returnCargoNo;

    private String returnCargoFirm;

    private AddressResource deliveryAddress;

    private AddressResource invoiceAddress;

    private Float totalPrice;

    private PaymentType paymentType;

    private List<BasketObjectResource> basketObjects;

    private Boolean paid;

    private Boolean repaid;

    private String remittanceNo;

    private String remittanceBank;

    private String returnRemittanceNo;

    private String returnRemittanceBank;

}
