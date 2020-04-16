package com.lens.epay.mapper;


import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.organization.FirmDto;
import com.lens.epay.model.entity.Firm;
import com.lens.epay.model.resource.organization.FirmResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FirmMapper extends Converter<FirmDto, Firm, FirmResource> {
}
