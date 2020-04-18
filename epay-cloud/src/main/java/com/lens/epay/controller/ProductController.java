package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.service.ProductService;
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
@RequestMapping("/product")
@Api(value = "Product", tags = {"Product Operations"})
public class ProductController extends AbstractController<Product, UUID, ProductDto, ProductResource> {

    @Override
    protected AbstractService<Product, UUID, ProductDto, ProductResource> getService() {
        return productService;
    }

    @Override
    public void setMinRole() {
        super.minRole = Role.FIRM_ADMIN;
    }

    @Autowired
    private ProductService productService;
}
