package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.CategoryDto;
import com.lens.epay.model.entity.Category;
import com.lens.epay.model.resource.product.CategoryResource;
import com.lens.epay.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@RestController
@RequestMapping("/category")
@Api(value = "Category", tags = {"Category Operations"})
public class CategoryController extends AbstractController<Category, UUID, CategoryDto, CategoryResource> {

    @Autowired
    private CategoryService categoryService;

    @Override
    protected AbstractService<Category, UUID, CategoryDto, CategoryResource> getService() {
        return categoryService;
    }

    @Override
    public Role getSaveRole() {
        return Role.FIRM_ADMIN;
    }

    @Override
    public Role getGetRole() {
        return Role.NOT_AUTH;
    }

    @Override
    public Role getGetAllRole() {
        return Role.NOT_AUTH;
    }

    @Override
    public Role getUpdateRole() {
        return Role.FIRM_ADMIN;
    }

    @Override
    public Role getDeleteRole() {
        return Role.FIRM_ADMIN;
    }

}
