package com.lens.epay.model.resource.user;

import com.lens.epay.common.AbstractResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
public class UserGroupResource extends AbstractResource {
    private Set<MinimalUserResource> users;
    private Boolean isPrivate;

}
