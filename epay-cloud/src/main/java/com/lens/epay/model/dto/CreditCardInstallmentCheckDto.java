package com.lens.epay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardInstallmentCheckDto {

    private String cardBinNumber;

    private BigDecimal price;

}
