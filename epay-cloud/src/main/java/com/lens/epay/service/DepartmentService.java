package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.DepartmentMapper;
import com.lens.epay.mapper.MinimalUserMapper;
import com.lens.epay.model.dto.organization.DepartmentDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.organization.DepartmentResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.repository.BranchRepository;
import com.lens.epay.repository.DepartmentRepository;
import com.lens.epay.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */
@Service
public class DepartmentService extends AbstractService<Department, UUID, DepartmentDto, DepartmentResource> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentMapper mapper;

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MinimalUserMapper userMapper;

    @Override
    public DepartmentRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<DepartmentDto, Department, DepartmentResource> getConverter() {
        return mapper;
    }

    @Transactional
    public DepartmentResource addPersonal(UUID departmentId, UUID userId) {
        Department department = repository.findDepartmentById(departmentId);
        User user = userRepository.findUserById(userId);

        if (user.getDepartment().equals(department)) {
            throw new BadRequestException(USER_ALREADY_ADDED_TO_DEPARTMENT);
        }
        user.setDepartment(department);
        userRepository.save(user);
        return mapper.toResource(repository.findDepartmentById(departmentId));
    }

    @Transactional
    public DepartmentResource removePersonal(UUID departmentId, UUID userId) {
        Department department = repository.findDepartmentById(departmentId);
        User user = userRepository.findUserById(userId);
        if (!user.getDepartment().equals(department)) {
            throw new BadRequestException(USER_IS_NOT_EXIST);
        }
        user.setDepartment(null);
        userRepository.save(user);
        return mapper.toResource(department);
    }

    public Set<MinimalUserResource> getPersonals(UUID departmentId) {
        Department department = repository.findDepartmentById(departmentId);
        return userMapper.toResource(userRepository.findUsersByDepartment(department));
    }
}
