package com.lens.epay.mapper;

import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.LoginResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DepartmentMapper.class})
public interface LoginMapper {

    LoginResource toResource(User user);
}
