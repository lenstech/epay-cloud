package com.lens.epay.repository;

import com.lens.epay.model.entity.Address;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface AddressRepository extends EpayRepository<Address, UUID> {

    List<Address> findAddressByUserId(UUID id);

}
