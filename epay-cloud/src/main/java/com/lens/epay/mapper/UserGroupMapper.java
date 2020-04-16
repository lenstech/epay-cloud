package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.user.UserGroupDto;
import com.lens.epay.model.entity.UserGroup;
import com.lens.epay.model.resource.user.UserGroupResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MinimalUserMapper.class})
public interface UserGroupMapper extends Converter<UserGroupDto, UserGroup, UserGroupResource> {

}
