package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.organization.DepartmentDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.resource.organization.DepartmentResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {BranchMapper.class})
public interface DepartmentMapper extends Converter<DepartmentDto, Department, DepartmentResource> {
}
