package com.lens.epay.util;

import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtResolver;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */
@UtilityClass
public class MapperUtil {

    @Autowired
    private JwtResolver resolver;

    @Autowired
    private UserRepository userRepository;

    @Named("tokenToUser")
    private User tokenToUser(String token){
        return userRepository.findUserById(resolver.getIdFromToken(token));
    }
}
