package com.lens.epay.model.resource.organization;

import com.lens.epay.common.AbstractResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchResource extends AbstractResource {

    private String city;

    private String address;

    private FirmResource firm;
}
