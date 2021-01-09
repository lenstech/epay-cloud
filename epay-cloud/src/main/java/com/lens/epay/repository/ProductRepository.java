package com.lens.epay.repository;

import com.lens.epay.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends EpayRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findProductsByCategoryId(Pageable pageable, UUID catrgoryId);

    Page<Product> findProductsByStockedTrue(Pageable pageable);

    List<Product> findProductsByStockedTrue();

    boolean existsByName(String name);
}
