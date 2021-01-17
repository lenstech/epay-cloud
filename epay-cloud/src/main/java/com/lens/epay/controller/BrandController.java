package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.BrandDto;
import com.lens.epay.model.entity.Brand;
import com.lens.epay.model.resource.product.BrandResource;
import com.lens.epay.service.BrandService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@RestController
@RequestMapping("/brand")
@Api(value = "Brand", tags = {"Brand Operations"})
public class BrandController extends AbstractController<Brand, UUID, BrandDto, BrandResource> {

    private final Logger logger = LoggerFactory.getLogger(BrandController.class);
    @Autowired
    private BrandService service;

    @Override
    protected AbstractService<Brand, UUID, BrandDto, BrandResource> getService() {
        return service;
    }

    @Override
    public Role getSaveRole() {
        return Role.CUSTOMER;
    }

    @Override
    public Role getGetRole() {
        return Role.CUSTOMER;
    }

    @Override
    public Role getGetAllRole() {
        return Role.FIRM_ADMIN;
    }

    @Override
    public Role getUpdateRole() {
        return Role.CUSTOMER;
    }

    @Override
    public Role getDeleteRole() {
        return Role.CUSTOMER;
    }

}
