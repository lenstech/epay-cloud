package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.mapper.LoginMapper;
import com.lens.epay.model.dto.user.LoginDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.security.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginMapper mapper;

    public LoginResource login(LoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user != null && encoder.matches(dto.getPassword(), user.getPassword())) {
            if (!user.isConfirmed()) {
                throw new UnauthorizedException(ErrorConstants.PLEASE_CONFIRM_YOUR_EMAIL_ADDRESS);
            }
            String token = jwtGenerator.generateToken(user.getId(), user.getRole());
            LoginResource resource = mapper.toResource(user);
            resource.setToken(token);
            return resource;
        } else {
            throw new UnauthorizedException(ErrorConstants.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    public String updateToken(String token) {
        return jwtGenerator.generateToken(jwtResolver.getIdFromToken(token), jwtResolver.getRoleFromToken(token));
    }
}
