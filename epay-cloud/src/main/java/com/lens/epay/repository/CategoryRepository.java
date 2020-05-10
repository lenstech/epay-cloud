package com.lens.epay.repository;

import com.lens.epay.model.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends EpayRepository<Category, UUID> {

    Integer countCategoriesByIdAndProductsNotNull(UUID categoryId);

}
