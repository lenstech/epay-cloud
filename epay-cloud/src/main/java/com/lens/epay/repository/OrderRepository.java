package com.lens.epay.repository;

import com.lens.epay.model.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends EpayRepository<Order, UUID> {

    List<Order> findOrdersByUserId(UUID id);

}
