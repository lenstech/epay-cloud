package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void setSaveRole() {
        super.saveRole = Role.BASIC_USER;
    }

    @Override
    public void setGetRole() {
        super.getRole = null;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = null;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.BASIC_USER;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.BASIC_USER;
    }

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Search for Product", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/search/{word}/{pageNo}")
    public ResponseEntity<Page<ProductResource>> searchProducts(@PathVariable String word, @PathVariable int pageNo) {
        return ResponseEntity.ok(productService.search(word, pageNo));
    }

    @ApiOperation(value = "Get Products By category Id", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/get/category/{pageNo}")
    public ResponseEntity<Page<ProductResource>> getAddressesOfUser(@RequestParam UUID categoryId, @PathVariable int pageNo) {
        return ResponseEntity.ok(productService.findProductByCategory(categoryId, pageNo));
    }
}
