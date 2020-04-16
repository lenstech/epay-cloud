package com.lens.epay.repository;

import com.lens.epay.model.entity.UserGroup;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserGroupRepository extends EpayRepository<UserGroup, UUID> {
}
