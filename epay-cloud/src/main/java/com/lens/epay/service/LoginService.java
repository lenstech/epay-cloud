package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.LoginDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.security.JwtResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    public LoginResource login(LoginDto dto) {
        if (StringUtils.isBlank(dto.getEmail())) {
            throw new BadRequestException(ErrorConstants.MAIL_NOT_EXIST);
        }
        User user = userRepository.findByEmail(dto.getEmail().trim().toLowerCase());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user != null && encoder.matches(dto.getPassword(), user.getPassword())) {
            if (!user.isConfirmed()) {
                throw new UnauthorizedException(ErrorConstants.PLEASE_CONFIRM_YOUR_EMAIL_ADDRESS);
            }
            String token = jwtGenerator.generateLoginToken(user.getId(), user.getRole());
            CompleteUserResource userResource = userMapper.toResource(user);
            return new LoginResource(userResource, token);
        } else {
            throw new UnauthorizedException(ErrorConstants.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    public LoginResource updateToken(String token) {
        UUID userId = jwtResolver.getIdFromToken(token);
        User user = userService.fromIdToEntity(userId);
        return new LoginResource(userMapper.toResource(user), jwtGenerator.generateLoginToken(userId, user.getRole()));
    }
}
