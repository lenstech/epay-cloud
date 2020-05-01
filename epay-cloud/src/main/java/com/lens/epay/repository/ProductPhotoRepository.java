package com.lens.epay.repository;

import com.lens.epay.model.entity.ProductPhoto;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductPhotoRepository extends EpayRepository<ProductPhoto, UUID> {

    ProductPhoto findProductPhotoByProductId(UUID productId);

    Boolean existsByProductId(UUID productId);
}
