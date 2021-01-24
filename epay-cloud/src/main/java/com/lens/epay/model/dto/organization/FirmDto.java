package com.lens.epay.model.dto.organization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 1 Mar 2020
 */
@Getter
@Setter
@NoArgsConstructor
public class FirmDto {

    @NotNull(message = "Firma ismi boş olmamalı.")
    private String name;

    private String city;

    private String taxId;

    private String Address;

    private String phoneNo;

    @Email
    private String email;
}
