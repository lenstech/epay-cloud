package com.lens.epay.configuration;

import com.lens.epay.enums.Role;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */
@Component
public class DbInitializeData {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void postConstruct() {
        if(!userRepository.existsByEmail("lensipt@gmail.com")){
            User admin = new User();
            admin.setEmail("lensipt@gmail.com");
            admin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setRole(Role.ADMIN);
            admin.setConfirmed(true);
            userRepository.save(admin);
        }
    }
}
