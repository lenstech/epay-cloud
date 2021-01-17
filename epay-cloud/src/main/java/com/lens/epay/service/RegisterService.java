package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.repository.DepartmentRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.security.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.DEPARTMENT_IS_NOT_EXIST;
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
    private JwtResolver jwtResolver;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public LoginResource saveFirmUser(RegisterFirmUserDto registerFirmUserDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = mapper.toEntity(registerFirmUserDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(MAIL_ALREADY_EXISTS);
        }
        if (registerFirmUserDto.getDepartmentId() != null) {
            Department department = departmentRepository.findDepartmentById(registerFirmUserDto.getDepartmentId());
            if (department == null) {
                throw new BadRequestException(DEPARTMENT_IS_NOT_EXIST);
            }
            user.setDepartment(department);
        }
        user.setPassword(bCryptPasswordEncoder.encode(registerFirmUserDto.getPassword()));
        userRepository.saveAndFlush(user);
        tokenService.sendActivationTokenToMail(user);
        CompleteUserResource completeUserResource = mapper.toResource(user);
        return new LoginResource(completeUserResource, jwtGenerator.generateLoginToken(user.getId(), user.getRole()));
    }

    @Transactional
    public LoginResource saveCustomer(RegisterCustomerDto customerDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = mapper.customerDtoToUser(customerDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(MAIL_ALREADY_EXISTS);
        }
        user.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.saveAndFlush(user);
        tokenService.sendActivationTokenToMail(user);
        CompleteUserResource completeUserResource = mapper.toResource(user);
        return new LoginResource(completeUserResource, jwtGenerator.generateLoginToken(user.getId(), user.getRole()));
    }

    @Transactional
    public LoginResource confirmRegister(String confirmationToken) {
        UUID id = jwtResolver.getIdFromToken(confirmationToken);
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new BadRequestException(ErrorConstants.USER_NOT_EXIST);
        }
        user.setConfirmed(true);
        userRepository.save(user);
        return new LoginResource(mapper.toResource(user), jwtGenerator.generateLoginToken(user.getId(), user.getRole()));

    }

}
