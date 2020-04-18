package com.lens.epay.model.resource;

import com.lens.epay.common.AbstractResource;
import com.lens.epay.enums.AddressType;
import com.lens.epay.enums.InvoiceType;
import com.lens.epay.model.resource.user.MinimalUserResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 18 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResource extends AbstractResource {

    private String name;

    private MinimalUserResource user;

    private String country;

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

    private String identityNo;

    private String taxNo;

    private String taxAdministration;
}
