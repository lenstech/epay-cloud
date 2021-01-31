package com.lens.epay.service;

import com.lens.epay.common.AbstractService;
import com.lens.epay.common.Converter;
import com.lens.epay.enums.SearchOperator;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.mapper.ProductMapper;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.other.SearchCriteria;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.repository.BasketRepository;
import com.lens.epay.repository.ProductPhotoRepository;
import com.lens.epay.repository.ProductRepository;
import com.lens.epay.repository.specifications.ProductSpecification;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.ID_IS_NOT_EXIST;

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

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Override
    public ProductRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<ProductDto, Product, ProductResource> getConverter() {
        return mapper;
    }

    @Override
    public List<ProductResource> getMultiple(List<UUID> ids) {
        List<Product> entities;
        try {
            entities = repository.findAllByActiveTrueAndIdIn(ids);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
        return mapper.toResources(entities);
    }

    @Override
    public List<ProductResource> getAll(UUID userId) {
        return mapper.toResources(repository.findAllByActiveTrue());
    }

    @Override
    public Page<ProductResource> getAllWithPage(int pageNumber, String sortBy, Sort.Direction direction, UUID userId) {
        return repository.findAllByActiveTrue(getPageable(pageNumber, sortBy, direction)).map(mapper::toResource);
    }

    public Page<ProductResource> search(String word, int pageNumber) {
        ProductSpecification spec = new ProductSpecification();
        if (word != null) {
            spec.add(new SearchCriteria("category", word, SearchOperator.MATCH_IN_CATEGORY));
            spec.add(new SearchCriteria("name", word, SearchOperator.MATCH));
            spec.add(new SearchCriteria("description", word, SearchOperator.MATCH));
        }
        PageRequest pageable = getPageable(pageNumber);
        return repository.findAll(spec, pageable).map(getConverter()::toResource);
    }

    public List<ProductResource> getAllStocked() {
        return mapper.toResources(getRepository().findProductsByStockedTrueAndActiveTrue());
    }

    public Page<ProductResource> getAllWithPageStocked(int pageNumber, String sortBy, boolean desc) {
        PageRequest pageable = getPageable(pageNumber, sortBy, desc);
        return repository.findProductsByStockedTrueAndActiveTrue(pageable).map(mapper::toResource);
    }

    public Page<ProductResource> findProductByCategory(UUID categoryId, int pageNo) {
        Pageable pageable = getPageable(pageNo);
        return repository.findProductsByCategoryId(pageable, categoryId).map(getConverter()::toResource);
    }

    @Override
    protected void deleteOperations(UUID productId, UUID userId) {
        productPhotoRepository.deleteProductPhotoByProductId(productId);
    }

    @Override
    @Transactional
    public void delete(UUID productId, UUID userId) {
        if (basketRepository.countBasketObjectsByProductId(productId) <= 0) {
            super.delete(productId, userId);
        }
        Product product = fromIdToEntity(productId);
        product.setActive(false);
        repository.save(product);
    }

    public ProductResource changeStockStatus(UUID productId, Boolean stocked) {
        Product product = repository.getOne(productId);
        product.setStocked(stocked);
        return mapper.toResource(repository.save(product));
    }

    @Named("getPriceFromId")
    public Float getPriceFromId(UUID id) {
        try {
            return getRepository().findOneById(id).getDiscountedPrice();
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
    }
}
