package com.lens.epay.repository;

import com.lens.epay.model.entity.Firm;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FirmRepository extends EpayRepository<Firm, UUID> {

    Boolean existsByName(String name);

    Firm findByName(String name);
}
