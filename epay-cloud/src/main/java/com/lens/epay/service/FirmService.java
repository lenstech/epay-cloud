package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.BranchMapper;
import com.lens.epay.mapper.FirmMapper;
import com.lens.epay.mapper.MinimalUserMapper;
import com.lens.epay.model.dto.organization.FirmDto;
import com.lens.epay.model.entity.Firm;
import com.lens.epay.model.resource.organization.BranchResource;
import com.lens.epay.model.resource.organization.FirmResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.repository.BranchRepository;
import com.lens.epay.repository.EpayRepository;
import com.lens.epay.repository.FirmRepository;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static com.lens.epay.constant.GeneralConstants.PAGE_SIZE;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MinimalUserMapper minimalUserMapper;

    @Override
    public EpayRepository<Firm, UUID> getRepository() {
        return repository;
    }

    @Override
    public Converter<FirmDto, Firm, FirmResource> getConverter() {
        return mapper;
    }

    public Set<BranchResource> getBranches(UUID firmId) {
        return branchMapper.toResources(branchRepository.findBranchesByFirmId(firmId));
    }

    public Page<MinimalUserResource> findUsersByFirmId(UUID firmId, int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        return userRepository.findUsersByDepartmentBranchFirmId(pageable, firmId).map(minimalUserMapper::toResource);
    }

}
