package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.BrandMapper;
import com.lens.epay.model.dto.sale.BrandDto;
import com.lens.epay.model.entity.Brand;
import com.lens.epay.model.resource.product.BrandResource;
import com.lens.epay.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Service
public class BrandService extends AbstractService<Brand, UUID, BrandDto, BrandResource> {

    @Autowired
    private BrandRepository repository;

    @Autowired
    private BrandMapper mapper;

    @Override
    public BrandRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<BrandDto, Brand, BrandResource> getConverter() {
        return mapper;
    }

}
