package com.lens.epay.model.resource.organization;

import com.lens.epay.common.AbstractResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * Created by Emir Gökdemir
 * on 1 Mar 2020
 */

@Getter
@Setter
@NoArgsConstructor
public class FirmResource extends AbstractResource {

    private String city;

    private String taxId;

    private String Address;

    private String phoneNo;

    @Email
    private String email;
}
