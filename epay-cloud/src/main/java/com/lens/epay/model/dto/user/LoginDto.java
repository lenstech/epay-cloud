package com.lens.epay.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 11 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Email(message = "Lütfen email formatında giriniz")
    @NotNull(message = "Email boş olamaz")
    private String email;

    @NotNull(message = "Lütfen şifrenizi giriniz")
    private String password;
}
