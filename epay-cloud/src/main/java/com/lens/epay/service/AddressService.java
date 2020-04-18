package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.exception.BadRequestException;
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

import static com.lens.epay.constant.ErrorConstants.*;
import static com.lens.epay.constant.ErrorConstants.ID_CANNOT_BE_EMPTY;

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
    public AddressResource save(AddressDto addressDto, UUID userId) {
        Address address = mapper.toEntity(addressDto);
        address.setUser(userRepository.findUserById(userId));
        return getConverter().toResource(getRepository().save(address));
    }

    @Override
    public AddressResource put(UUID id, AddressDto updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        Address theReal = getRepository().findById(id).orElseThrow(() -> new BadRequestException(ID_IS_NOT_EXIST));
        if (updatedDto == null) {
            throw new BadRequestException(DTO_CANNOT_BE_EMPTY);
        }
        try {
            Address updated = mapper.toEntity(updatedDto);
            updated.setId(theReal.getId());
            updated.setCreatedDate(theReal.getCreatedDate());
            updated.setUser(userRepository.findUserById(userId));
            return mapper.toResource(repository.save(updated));
        } catch (Exception e) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
    }

    public List<AddressResource> getAddressesOfUser(UUID userId){
        return mapper.toResources(repository.findAddressByUserId(userId));
    }
}
