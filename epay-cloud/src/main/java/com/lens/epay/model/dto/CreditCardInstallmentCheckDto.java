package com.lens.epay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
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

    @NotNull(message = "Kart numaranızı giriniz")
    private String cardBinNumber;

    @NotNull(message = "Tutarı giriniz")
    private BigDecimal price;

}
