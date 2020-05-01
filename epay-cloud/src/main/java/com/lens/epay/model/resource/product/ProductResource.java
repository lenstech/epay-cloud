package com.lens.epay.model.resource.product;

import com.lens.epay.common.AbstractResource;
import com.lens.epay.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource extends AbstractResource {
    private String name;

    private String description;

    private Unit unit;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Float price;

    private CategoryResource category;

    private Boolean stocked = true;
}
