package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.product.CategoryDto;
import com.lens.epay.model.entity.Category;
import com.lens.epay.model.resource.product.CategoryResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends Converter<CategoryDto, Category, CategoryResource> {

}
