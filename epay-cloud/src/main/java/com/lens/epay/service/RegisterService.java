package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.RegisterDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.repository.DepartmentRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.ID_IS_NOT_EXIST;
import static com.lens.epay.constant.ErrorConstants.MAIL_ALREADY_EXISTS;


/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public CompleteUserResource save(RegisterDto registerDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = mapper.toEntity(registerDto);
        if (registerDto.getDepartmentId() != null) {
            Department department = departmentRepository.findDepartmentById(registerDto.getDepartmentId());
            if (department == null) {
                throw new BadRequestException(ID_IS_NOT_EXIST);
            }
            user.setDepartment(department);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(MAIL_ALREADY_EXISTS);
        }
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        userRepository.saveAndFlush(user);
        confirmationTokenService.sendActivationToken(user);
        return mapper.toResource(user);
    }

    @Transactional
    public void confirmRegister(String confirmationToken) {
        UUID id = jwtResolver.getIdFromToken(confirmationToken);
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new BadRequestException(ErrorConstants.USER_NOT_EXIST);
        }
        user.setConfirmed(true);
        userRepository.save(user);
    }

}
