package com.lens.epay.mapper;

import com.lens.epay.model.entity.CreditCardTransaction;
import com.lens.epay.model.resource.CreditCardTransactionResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditCardTransactionMapper {
    CreditCardTransactionResource toResource(CreditCardTransaction entity);

    List<CreditCardTransactionResource> toResources(List<CreditCardTransaction> entities);
}
