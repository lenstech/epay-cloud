package com.lens.epay.model.resource.user;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Getter
@Setter
@Resource
public class MinimalUserResource {

    private UUID id;

    private String name;

    private String surname;

    private String userFirmId;

    private String title;

}
