package com.lens.epay.model.resource.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Resource;

@Getter
@Setter
@Resource
@AllArgsConstructor
@NoArgsConstructor
public class LoginResource {

    private CompleteUserResource user;

    private String token;
}
