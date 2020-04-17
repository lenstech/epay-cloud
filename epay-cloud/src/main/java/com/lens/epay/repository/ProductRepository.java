package com.lens.epay.repository;

import com.lens.epay.model.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends EpayRepository<Product, UUID> {

}
