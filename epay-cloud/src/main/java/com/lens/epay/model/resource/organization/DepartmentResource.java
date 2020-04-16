package com.lens.epay.model.resource.organization;

import com.lens.epay.common.AbstractResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */
@Getter
@Setter
@NoArgsConstructor
public class DepartmentResource extends AbstractResource {

    private String description;

    private BranchResource branch;
}
