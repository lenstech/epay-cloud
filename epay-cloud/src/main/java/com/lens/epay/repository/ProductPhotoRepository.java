package com.lens.epay.repository;

import com.lens.epay.model.entity.ProductPhoto;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPhotoRepository extends EpayRepository<ProductPhoto, UUID> {

    Optional<ProductPhoto> findProductPhotoByProductId(UUID productId);

    void deleteProductPhotoByProductId(UUID productId);

    Boolean existsByProductId(UUID productId);
}
