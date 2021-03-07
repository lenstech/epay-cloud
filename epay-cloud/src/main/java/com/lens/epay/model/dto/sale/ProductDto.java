package com.lens.epay.model.dto.sale;

import com.lens.epay.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotNull(message = "Lütfen ürün ismini giriniz.")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Unit unit = Unit.PIECE;

    @NotNull(message = "Fiyat boş bırakılamaz.")
    private Float price;

    private Float discountedPrice;

    @NotNull(message = "Kategori belirtilmeli!")
    private UUID categoryId;

    private UUID brandId;

    private Boolean stocked;

    private boolean active = true;
}
