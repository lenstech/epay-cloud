package com.lens.epay.repository;

import com.lens.epay.model.entity.BasketObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BasketRepository extends EpayRepository<BasketObject, UUID> {

        List<BasketObject> findOrderProductsByOrderId(UUID id);
}
