package com.lens.epay.model.dto.user;

import com.lens.epay.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 11 Şub 2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFirmUserDto {
    @Email(message = "Lütfen email formatında giriniz")
    @NotNull(message = "Email boş olamaz")
    private String email;

    @NotNull(message = "Lütfen isminizi giriniz.")
    private String name;

    @NotNull(message = "Lütfen soyisminizi giriniz.")
    private String surname;

//    private UUID firmId;

    @NotNull(message = "Lütfen şifrenizi giriniz.")
    private String password;

    private Role role = Role.BASIC_USER;

    private UUID departmentId;

    private String userFirmId;

    private String title;

    private String phoneNumber;
}
