package com.lens.epay.model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

    private String name;

    private String city;

    private String address;

    @NotNull(message = "FirmId cannot be blank")
    private UUID firmId;
}
