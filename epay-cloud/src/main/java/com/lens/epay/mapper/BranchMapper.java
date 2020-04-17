package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.organization.BranchDto;
import com.lens.epay.model.entity.Branch;
import com.lens.epay.model.resource.organization.BranchResource;
import com.lens.epay.repository.FirmRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE , uses = {FirmMapper.class , FirmRepository.class})
public interface BranchMapper extends Converter<BranchDto, Branch, BranchResource> {

    @Override
    @Mapping(source = "firmId", target = "firm", qualifiedByName = "firmById")
    Branch toEntity(BranchDto branchDto);
}
