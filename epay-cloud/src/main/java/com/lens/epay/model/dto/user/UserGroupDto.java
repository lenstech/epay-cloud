package com.lens.epay.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
public class UserGroupDto {

    private String name;

    private Boolean isPrivate;

}
