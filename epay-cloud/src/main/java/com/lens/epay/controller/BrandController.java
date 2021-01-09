package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.BrandDto;
import com.lens.epay.model.entity.Brand;
import com.lens.epay.model.resource.product.BrandResource;
import com.lens.epay.service.BrandService;
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
@RequestMapping("/brand")
@Api(value = "Brand", tags = {"Brand Operations"})
public class BrandController extends AbstractController<Brand, UUID, BrandDto, BrandResource> {

    @Autowired
    private BrandService service;

    @Override
    protected AbstractService<Brand, UUID, BrandDto, BrandResource> getService() {
        return service;
    }

    @Override
    public void setSaveRole() {
        super.saveRole = Role.CUSTOMER;
    }

    @Override
    public void setGetRole() {
        super.getRole = Role.CUSTOMER;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = Role.FIRM_ADMIN;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.CUSTOMER;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.CUSTOMER;
    }

    @Override
    public void setEntityName() {
        super.entityName = "Brand";
    }

}
