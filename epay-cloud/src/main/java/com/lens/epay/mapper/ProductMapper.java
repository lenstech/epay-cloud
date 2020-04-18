package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryMapper.class, CategoryRepository.class})
public interface ProductMapper extends Converter<ProductDto, Product, ProductResource> {

    @Override
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "findOneById")
    Product toEntity(ProductDto productDto);
}
