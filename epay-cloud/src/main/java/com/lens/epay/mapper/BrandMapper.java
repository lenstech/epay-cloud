package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.sale.BrandDto;
import com.lens.epay.model.entity.Brand;
import com.lens.epay.model.resource.product.BrandResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper extends Converter<BrandDto, Brand, BrandResource> {

}
