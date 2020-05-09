package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.AddressMapper;
import com.lens.epay.model.dto.AddressDto;
import com.lens.epay.model.entity.Address;
import com.lens.epay.model.resource.AddressResource;
import com.lens.epay.repository.AddressRepository;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Service
public class AddressService extends AbstractService<Address, UUID, AddressDto, AddressResource> {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private AddressMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<AddressDto, Address, AddressResource> getConverter() {
        return mapper;
    }

    @Override
    protected Address putOperations(Address oldEntity, Address newEntity, UUID userId) {
        newEntity.setUser(userRepository.findUserById(userId));
        return super.putOperations(oldEntity, newEntity, userId);
    }

    @Override
    protected Address saveOperations(Address entity, AddressDto addressDto, UUID userId) {
        entity.setUser(userRepository.findUserById(userId));
        return entity;
    }

    public List<AddressResource> getAddressesOfUser(UUID userId){
        return mapper.toResources(repository.findAddressByUserId(userId));
    }

    public AddressResource updateAddressByAdmin(UUID addressId, AddressDto addressDto, UUID userId){
        return super.put(addressId,addressDto, userId);
    }

    public AddressResource saveAddressByAdmin(AddressDto addressDto, UUID userId){
        return super.save(addressDto, userId);
    }
}
