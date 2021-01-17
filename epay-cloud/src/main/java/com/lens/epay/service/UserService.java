package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.MinimalUserMapper;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 3 May 2020
 */

@Service
public class UserService extends AbstractService<User, UUID, RegisterCustomerDto, MinimalUserResource> {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private MinimalUserMapper minimalUserMapper;

    @Override
    public UserRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<RegisterCustomerDto, User, MinimalUserResource> getConverter() {
        return minimalUserMapper;
    }

    public MinimalUserResource findUserByIdToMinRes(UUID id) {
        return minimalUserMapper.toResource(fromIdToEntity(id));
    }

    public User findUserByEmail(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new BadRequestException(ErrorConstants.USER_NOT_EXIST);
        }
        return user;
    }

    //todo: userSearch
}
