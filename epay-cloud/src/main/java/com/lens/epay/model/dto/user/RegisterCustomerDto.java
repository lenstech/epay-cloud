package com.lens.epay.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 3 May 2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerDto {
    private String email;

    private String name;

    private String surname;

//    private UUID firmId;

    private String password;

    private String phoneNumber;
}
