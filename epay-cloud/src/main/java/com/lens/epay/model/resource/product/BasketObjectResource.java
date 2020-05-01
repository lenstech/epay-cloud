package com.lens.epay.model.resource.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 1 May 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketObjectResource {

    private ProductResource product;

    private Integer productQuantity;
}
