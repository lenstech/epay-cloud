package com.lens.epay.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 11 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
    @NotNull(message = "Lütfen eski şifrenizi giriniz.")
    private String oldPassword;
    @NotNull(message = "Lütfen yeni şifrenizi giriniz.")
    private String newPassword;
}
