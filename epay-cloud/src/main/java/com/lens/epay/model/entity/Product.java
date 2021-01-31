package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import com.lens.epay.enums.Unit;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Data
@Entity
@Table(name = "product")
public class Product extends AbstractEntity<UUID> {

    @NotNull
    private String name;

    @Column(name = "description")
    @Size(max = 1024)
    private String description;

    @Enumerated(EnumType.STRING)
    private Unit unit = Unit.PIECE;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float price;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float discountedPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "stocked")
    private Boolean stocked = true;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private boolean active;
}
