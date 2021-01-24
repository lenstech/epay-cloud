package com.lens.epay.model.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketObjectDto {

    @NotNull(message = "Ürün no boş olmamalı")
    private UUID productId;

    @NotNull(message = "Ürün sayısı boş olmamalı")
    private Integer productQuantity;
}
