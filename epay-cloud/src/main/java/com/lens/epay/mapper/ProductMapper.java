package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.service.BrandService;
import com.lens.epay.service.CategoryService;
import org.mapstruct.*;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryService.class, BrandService.class, BrandMapper.class, CategoryMapper.class})
public interface ProductMapper extends Converter<ProductDto, Product, ProductResource> {

    @Override
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "fromIdToEntity")
    @Mapping(source = "brandId", target = "brand", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, qualifiedByName = "fromIdToEntity")
    Product toEntity(ProductDto productDto);
}
