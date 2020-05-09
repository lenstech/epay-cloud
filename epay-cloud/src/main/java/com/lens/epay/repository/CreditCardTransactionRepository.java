package com.lens.epay.repository;

import com.lens.epay.model.entity.CreditCardTransaction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditCardTransactionRepository extends EpayRepository<CreditCardTransaction, UUID> {
}
