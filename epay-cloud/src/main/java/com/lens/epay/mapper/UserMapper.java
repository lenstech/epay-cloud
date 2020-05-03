package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DepartmentMapper.class})
public interface UserMapper extends Converter<RegisterFirmUserDto, User, CompleteUserResource> {

    User customerDtoToUser(RegisterCustomerDto customerDto);

    List<User> customerDtoToUser(List<RegisterCustomerDto> customerDtos);

}
