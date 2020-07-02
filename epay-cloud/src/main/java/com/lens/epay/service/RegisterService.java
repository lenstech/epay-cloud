package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.repository.DepartmentRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.util.MailUtil;
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
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MailUtil mailUtil;

    @Transactional
    public CompleteUserResource saveFirmUser(RegisterFirmUserDto registerFirmUserDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = mapper.toEntity(registerFirmUserDto);
        if (registerFirmUserDto.getDepartmentId() != null) {
            Department department = departmentRepository.findDepartmentById(registerFirmUserDto.getDepartmentId());
            if (department == null) {
                throw new NotFoundException(ID_IS_NOT_EXIST);
            }
            user.setDepartment(department);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(MAIL_ALREADY_EXISTS);
        }
        user.setPassword(bCryptPasswordEncoder.encode(registerFirmUserDto.getPassword()));
        userRepository.saveAndFlush(user);
        mailUtil.sendActivationMail(user, jwtGenerator.generateMailConfirmationToken(user.getId()));
//        confirmationTokenService.sendActivationToken(user);
        return mapper.toResource(user);
    }

    @Transactional
    public CompleteUserResource saveCustomer(RegisterCustomerDto customerDto){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = mapper.customerDtoToUser(customerDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(MAIL_ALREADY_EXISTS);
        }
        user.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.saveAndFlush(user);
        mailUtil.sendActivationMail(user, jwtGenerator.generateMailConfirmationToken(user.getId()));
        return mapper.toResource(user);
    }

    @Transactional
    public void confirmRegister(String confirmationToken) {
        UUID id = jwtResolver.getIdFromToken(confirmationToken);
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundException(ErrorConstants.USER_NOT_EXIST);
        }
        user.setConfirmed(true);
        userRepository.save(user);
    }

}
