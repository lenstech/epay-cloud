package com.lens.epay.model.dto.organization;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */

@Getter
@Setter
public class DepartmentDto {

    private String name;

    private String description;

    @NotNull(message = "BranchId cannot be blank")
    private UUID BranchId;
}
