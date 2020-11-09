package com.lens.epay.repository;

import com.lens.epay.model.entity.Brand;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BrandRepository extends EpayRepository<Brand, UUID> {

    boolean existsByName(String name);
}
