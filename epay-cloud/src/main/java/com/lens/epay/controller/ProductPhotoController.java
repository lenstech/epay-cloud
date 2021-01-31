package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.service.ProductPhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.lens.epay.constant.HttpSuccessMessagesConstants.SUCCESSFULLY_DELETED;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@RestController
@RequestMapping("/product-photo")
@Api(value = "Product Photo", tags = {"Product Photo"})
public class ProductPhotoController {

    private final Logger logger = LoggerFactory.getLogger(ProductPhotoController.class);
    @Autowired
    private ProductPhotoService service;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    @PostMapping("/upload")
    @ApiOperation("Upload photo of product by productId")
    public ResponseEntity<String> uploadProductPhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam UUID productId,
                                                     @RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        logger.info(String.format("Requesting uploadProductPhoto userId: %s.", userId));
        return ResponseEntity.ok(service.uploadProductPhoto(file, productId));
    }

    @GetMapping(value = "/get", produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation("Get photo of product by productId")
    public ResponseEntity<byte[]> getProductPhoto(@RequestParam("productId") UUID productId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Epay Product Photo\"" + productId)
                .body(service.getPhoto(productId));
    }

    @DeleteMapping
    @ApiOperation("Delete photo of product by productId")
    public ResponseEntity<String> deleteProductPhoto(@RequestParam("productId") UUID productId,
                                                     @RequestHeader(value = "Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        logger.info(String.format("Requesting deleteNewsPhoto with newsId: %s userId: %s ", productId, userId));
        service.deletePhotoByProductId(productId);
        return ResponseEntity.ok(SUCCESSFULLY_DELETED);
    }
}
