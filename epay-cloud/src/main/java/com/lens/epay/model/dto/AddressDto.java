package com.lens.epay.model.dto;

import com.lens.epay.enums.AddressType;
import com.lens.epay.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 18 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String name;

    private String country = "Türkiye";

    @NotNull(message = "Lütfen şehrinizi giriniz.")
    private String city;

    private String district;

    private String neighborhood;

    private String street;

    private String buildNo;

    private String innerNo;

    private String otherInfo;

    private Integer postalCode;

    private String receiverName;

    private String receiverSurname;

    private String receiverPhoneNumber;

    private AddressType addressType;

    private InvoiceType invoiceType;

    private String firmName;

    private String identityNo;

    private String taxNo;

    private String taxAdministration;
}
