package com.lens.epay.configuration;

import com.lens.epay.enums.Role;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
        List<User> initialUsers = new ArrayList<>();
        if(!userRepository.existsByEmail("lensipt@gmail.com")){
            User admin = new User();
            admin.setEmail("lensipt@gmail.com");
            admin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPhoneNumber("05064066032");
            admin.setRole(Role.ADMIN);
            admin.setConfirmed(true);
            initialUsers.add(admin);
        }
        if(!userRepository.existsByEmail("gkdemir@hotmail.com")){
            User basicUser = new User();
            basicUser.setEmail("gkdemir@hotmail.com");
            basicUser.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            basicUser.setName("default");
            basicUser.setSurname("basic");
            basicUser.setPhoneNumber("05064066031");
            basicUser.setRole(Role.BASIC_USER);
            basicUser.setConfirmed(true);
            initialUsers.add(basicUser);
        }
        if(!userRepository.existsByEmail("gokdeemir@gmail.com")){
            User customer = new User();
            customer.setEmail("gokdeemir@gmail.com");
            customer.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            customer.setName("default");
            customer.setSurname("customer");
            customer.setPhoneNumber("05064066030");
            customer.setRole(Role.CUSTOMER);
            customer.setConfirmed(true);
            initialUsers.add(customer);
        }
        if(!userRepository.existsByEmail("ajangs@hotmail.com")){
            User firmAdmin = new User();
            firmAdmin.setEmail("ajangs@hotmail.com");
            firmAdmin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            firmAdmin.setName("default");
            firmAdmin.setSurname("customer");
            firmAdmin.setPhoneNumber("05064066029");
            firmAdmin.setRole(Role.FIRM_ADMIN);
            firmAdmin.setConfirmed(true);
            initialUsers.add(firmAdmin);
        }
        if (!initialUsers.isEmpty()){
            userRepository.saveAll(initialUsers);
        }

        // TODO: 25 Nis 2020  default firm branch department oluşturulacak.
    }
}
