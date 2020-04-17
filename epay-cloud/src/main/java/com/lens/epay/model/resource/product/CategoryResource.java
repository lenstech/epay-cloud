package com.lens.epay.model.resource.product;

import com.lens.epay.common.AbstractResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResource extends AbstractResource {

    private String description;
}
