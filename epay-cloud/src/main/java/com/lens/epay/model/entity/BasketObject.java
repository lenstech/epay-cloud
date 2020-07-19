package com.lens.epay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lens.epay.common.AbstractEntity;
import lombok.Data;
import org.eclipse.persistence.annotations.Index;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Nis 2020
 */

@Data
@Entity
@Table(name = "basket_object")
public class BasketObject extends AbstractEntity<UUID> {

    @Index
    @ManyToOne(optional = false)
    @NotNull
    private Product product;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float unitPrice;

    @NotNull
    private Integer productQuantity;

    @Index
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JsonIgnore
    private Order order;

}
