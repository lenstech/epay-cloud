package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.mapper.ProductMapper;
import com.lens.epay.model.dto.product.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Service
public class ProductService extends AbstractService<Product, UUID, ProductDto, ProductResource> {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Override
    public ProductRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<ProductDto, Product, ProductResource> getConverter() {
        return mapper;
    }
}
