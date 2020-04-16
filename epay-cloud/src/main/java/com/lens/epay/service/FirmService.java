package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.BranchMapper;
import com.lens.epay.mapper.FirmMapper;
import com.lens.epay.model.dto.organization.FirmDto;
import com.lens.epay.model.entity.Firm;
import com.lens.epay.model.resource.organization.BranchResource;
import com.lens.epay.model.resource.organization.FirmResource;
import com.lens.epay.repository.BranchRepository;
import com.lens.epay.repository.FirmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 1 Mar 2020
 */
@Service
public class FirmService extends AbstractService<Firm, UUID, FirmDto, FirmResource> {

    @Autowired
    private FirmRepository repository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private FirmMapper mapper;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    public JpaRepository<Firm, UUID> getRepository() {
        return repository;
    }

    @Override
    public Converter<FirmDto, Firm, FirmResource> getConverter() {
        return mapper;
    }

    public Set<BranchResource> getBranches(UUID firmId) {
        return branchMapper.toResources(branchRepository.findBranchesByFirmId(firmId));
    }
}
