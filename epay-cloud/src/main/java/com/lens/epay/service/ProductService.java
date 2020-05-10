package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.SearchOperator;
import com.lens.epay.mapper.ProductMapper;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.other.SearchCriteria;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.repository.ProductRepository;
import com.lens.epay.repository.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.lens.epay.constant.GeneralConstants.PAGE_SIZE;

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

    public Page<ProductResource> search(String word, int pageNumber) {

        PageRequest pageable;

        ProductSpecification spec = new ProductSpecification();
        if (word != null) {
            spec.add(new SearchCriteria("category", word, SearchOperator.MATCH_IN_CATEGORY));
            spec.add(new SearchCriteria("name", word, SearchOperator.MATCH));
            spec.add(new SearchCriteria("description", word, SearchOperator.MATCH));
        }

        pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return repository.findAll(spec, pageable).map(getConverter()::toResource);
    }

    public Page<ProductResource> findProductByCategory(UUID categoryId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        return repository.findProductsByCategoryId(pageable, categoryId).map(getConverter()::toResource);
    }
}
