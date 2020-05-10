package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.CategoryMapper;
import com.lens.epay.model.dto.sale.CategoryDto;
import com.lens.epay.model.entity.Category;
import com.lens.epay.model.resource.product.CategoryResource;
import com.lens.epay.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.CATEGORY_CANNOT_BE_DELETED_WHEN_HAS_PRODUCT;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Service
public class CategoryService extends AbstractService<Category, UUID, CategoryDto, CategoryResource> {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryMapper mapper;

    @Override
    public CategoryRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<CategoryDto, Category, CategoryResource> getConverter() {
        return mapper;
    }

    @Override
    protected void deleteOperations(UUID uuid) {
        if(repository.countCategoriesByIdAndProductsNotNull(uuid)>0){
            throw new BadRequestException(CATEGORY_CANNOT_BE_DELETED_WHEN_HAS_PRODUCT);
        }
    }
}
