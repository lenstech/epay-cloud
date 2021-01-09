package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.BranchMapper;
import com.lens.epay.mapper.DepartmentMapper;
import com.lens.epay.model.dto.organization.BranchDto;
import com.lens.epay.model.entity.Branch;
import com.lens.epay.model.resource.organization.BranchResource;
import com.lens.epay.model.resource.organization.DepartmentResource;
import com.lens.epay.repository.BranchRepository;
import com.lens.epay.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Service
public class BranchService extends AbstractService<Branch, UUID, BranchDto, BranchResource> {

    @Autowired
    private BranchRepository repository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BranchMapper mapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public BranchRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<BranchDto, Branch, BranchResource> getConverter() {
        return mapper;
    }

    public Set<DepartmentResource> getDepartments(UUID branchId) {
        return departmentMapper.toResources(departmentRepository.findDepartmentsByBranchId(branchId));
    }
}
