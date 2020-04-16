package com.lens.epay.service;

import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.lens.epay.constant.ErrorConstants.*;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private JwtResolver jwtResolver;

    @Transactional
    public void resetPassword(String password, String confirmationToken) {
        User user = userRepository.findUserById(jwtResolver.getIdFromToken(confirmationToken));
        if (user == null) {
            throw new BadRequestException(USER_NOT_EXIST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            throw new BadRequestException(NEW_PASSWORD_CANNOT_BE_SAME_AS_OLD);
        }
        user.setPassword(encoder.encode(password));
        user.setConfirmed(true);
        userRepository.save(user);
    }

    @Transactional
    public void resetPasswordByAdmin(String email, String newPassword, String token) {
        User admin = userRepository.findUserById(jwtResolver.getIdFromToken(token));
        User user = userRepository.findByEmail(email);
        if (admin == null || user == null) {
            throw new BadRequestException(USER_NOT_EXIST);
        }
        Role role = user.getRole();
        if (role.equals(Role.ADMIN) || role.equals(Role.FIRM_ADMIN)) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
}
