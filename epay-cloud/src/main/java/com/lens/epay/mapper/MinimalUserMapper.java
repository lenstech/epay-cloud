package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.MinimalUserResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MinimalUserMapper extends Converter<RegisterCustomerDto, User, MinimalUserResource> {
}
