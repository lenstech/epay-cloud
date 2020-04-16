package com.lens.epay.repository;

import com.lens.epay.model.entity.Branch;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;


@Repository
public interface BranchRepository extends EpayRepository<Branch, UUID> {

    Branch findBranchById(UUID id);

    Set<Branch> findBranchesByFirmId(UUID firmId);
}
