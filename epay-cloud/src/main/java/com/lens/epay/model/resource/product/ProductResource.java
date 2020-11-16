package com.lens.epay.model.resource.product;

import com.lens.epay.common.AbstractResource;
import com.lens.epay.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource extends AbstractResource {
    private String description;

    private Unit unit;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float price;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float discountedPrice;

    private CategoryResource category;

    private Boolean stocked;

    private UUID photoId;

    private BrandResource brand;
}
