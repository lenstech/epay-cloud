package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.AddressDto;
import com.lens.epay.model.entity.Address;
import com.lens.epay.model.resource.AddressResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MinimalUserMapper.class})
public interface AddressMapper extends Converter<AddressDto, Address, AddressResource> {

}
