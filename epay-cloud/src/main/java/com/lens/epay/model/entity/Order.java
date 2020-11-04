package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Data
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity<UUID> {

    @org.eclipse.persistence.annotations.Index
    @NotNull(message = "User cannot be blanked!")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @org.eclipse.persistence.annotations.Index
    @NotNull(message = "Order Status cannot be blanked!")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.TAKEN;

    private ZonedDateTime shippedDate;

    private String cargoNo;

    private String cargoFirm;

    private String returnCargoNo;

    private String returnCargoFirm;

    @ManyToOne(optional = false)
    private Address deliveryAddress;

    @ManyToOne(optional = false)
    private Address invoiceAddress;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull
    private Float totalProductPrice;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull
    private Float deliveryFee;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentType paymentType;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<BasketObject> basketObjects;

    private Boolean paid;

    private Boolean repaid =  false;

    private String remittanceNo;

    private String remittanceBank;

    private String returnRemittanceNo;

    private String returnRemittanceBank;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCardTransaction creditCardTransaction;

    private String orderNote;
}
