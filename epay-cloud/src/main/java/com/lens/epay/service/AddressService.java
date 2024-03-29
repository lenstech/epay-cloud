package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.AddressType;
import com.lens.epay.enums.InvoiceType;
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
    protected Address putOperations(Address entity, UUID userId) {
        if (entity.getAddressType().equals(AddressType.INVOICE)) {
            if (entity.getInvoiceType().equals(InvoiceType.CORPORATE)) {
                if (entity.getFirmName() == null) {
                    throw new BadRequestException(FIRM_NAME_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL);
                } else if (entity.getTaxAdministration() == null) {
                    throw new BadRequestException(TAX_ADMINISTRATOR_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL);
                } else if (entity.getTaxNo().length() != 10) {
                    throw new BadRequestException(TAX_NO_OF_CORPORATE_INVOICE_SHOULD_BE_10_CHARACTERS);
                }
            } else {
                if (entity.getIdentityNo().length() != 11) {
                    throw new BadRequestException(IDENTITY_NO_OF_INDIVIDUAL_INVOICE_SHOULD_BE_11_CHARACTERS);
                }
            }
        }
        entity.setUser(userRepository.findUserById(userId));
        return entity;
    }

    @Override
    protected Address saveOperations(Address address, AddressDto addressDto, UUID userId) {
        if (address.getAddressType().equals(AddressType.INVOICE)) {
            if (address.getInvoiceType().equals(InvoiceType.CORPORATE)) {
                if (address.getFirmName() == null) {
                    throw new BadRequestException(FIRM_NAME_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL);
                } else if (address.getTaxAdministration() == null) {
                    throw new BadRequestException(TAX_ADMINISTRATOR_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL);
                } else if (address.getTaxNo().length() != 10) {
                    throw new BadRequestException(TAX_NO_OF_CORPORATE_INVOICE_SHOULD_BE_10_CHARACTERS);
                }
            } else {
                if (address.getIdentityNo().length() != 11) {
                    throw new BadRequestException(IDENTITY_NO_OF_INDIVIDUAL_INVOICE_SHOULD_BE_11_CHARACTERS);
                }
            }
        }
        address.setUser(userRepository.findUserById(userId));
        return address;
    }

    public List<AddressResource> getAddressesOfUser(UUID userId) {
        return mapper.toResources(repository.findAddressByUserId(userId));
    }

    public AddressResource updateAddressByAdmin(UUID addressId, AddressDto addressDto, UUID userId) {
        return super.put(addressId, addressDto, userId);
    }

    public AddressResource saveAddressByAdmin(AddressDto addressDto, UUID userId) {
        return super.save(addressDto, userId);
    }
}
