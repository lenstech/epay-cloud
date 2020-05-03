package com.lens.epay.repository;

import com.lens.epay.model.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends EpayRepository<Department, UUID> {

    Department findDepartmentById(UUID id);

    Set<Department> findDepartmentsByBranchId(UUID branchId);

    Boolean existsByName(String name);
}
