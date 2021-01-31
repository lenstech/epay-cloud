package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.ProductDto;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@RestController
@RequestMapping("/product")
@Api(value = "Product", tags = {"Product Operations"})
public class ProductController extends AbstractController<Product, UUID, ProductDto, ProductResource> {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    protected AbstractService<Product, UUID, ProductDto, ProductResource> getService() {
        return productService;
    }

    @Override
    public Role getSaveRole() {
        return Role.BASIC_USER;
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
        return Role.BASIC_USER;
    }

    @Override
    public Role getDeleteRole() {
        return Role.BASIC_USER;
    }

    @ApiOperation(value = "Search for Product", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/search/{word}/{pageNo}")
    public ResponseEntity<Page<ProductResource>> searchProducts(@PathVariable String word, @PathVariable int pageNo) {
        return ResponseEntity.ok(productService.search(word, pageNo));
    }

    @ApiOperation(value = "Get All stocked products with page ", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/all/stocked/{pageNo}")
    public ResponseEntity<Page<ProductResource>> getAllWithPageStocked(@PathVariable int pageNo,
                                                                       @RequestParam(required = false) String sortBy,
                                                                       @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ResponseEntity.ok(productService.getAllWithPageStocked(pageNo, sortBy, desc));
    }

    @ApiOperation(value = "Get All stocked products ", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/all/stocked")
    public ResponseEntity<List<ProductResource>> getAllStocked() {
        return ResponseEntity.ok(productService.getAllStocked());
    }

    @ApiOperation(value = "Get Products By category Id", response = ProductResource.class, responseContainer = "List")
    @GetMapping("/get/category/{pageNo}")
    public ResponseEntity<Page<ProductResource>> findProductByCategory(@RequestParam UUID categoryId, @PathVariable int pageNo) {
        return ResponseEntity.ok(productService.findProductByCategory(categoryId, pageNo));
    }

    @ApiOperation(value = "Change Stock Status of Product", response = ProductResource.class)
    @PutMapping("/change-stock-status")
    public ResponseEntity<ProductResource> changeStockStatus(@RequestHeader("Authorization") String token, @RequestParam UUID productId, @RequestParam Boolean stockStatus) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.FIRM_ADMIN);
        logger.info(String.format("Requesting changeStockStatus userId: %s.", userId));
        return ResponseEntity.ok(productService.changeStockStatus(productId, stockStatus));
    }
}
