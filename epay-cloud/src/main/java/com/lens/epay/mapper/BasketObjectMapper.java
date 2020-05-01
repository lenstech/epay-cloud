package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.sale.BasketObjectDto;
import com.lens.epay.model.entity.BasketObject;
import com.lens.epay.model.resource.product.BasketObjectResource;
import com.lens.epay.service.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Nis 2020
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductMapper.class, ProductService.class})
public interface BasketObjectMapper extends Converter<BasketObjectDto, BasketObject, BasketObjectResource> {

    @Override
    @Mapping(source = "productId", target = "product", qualifiedByName = "fromIdToEntity")
    BasketObject toEntity(BasketObjectDto basketObjectDto);


}
