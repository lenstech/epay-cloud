package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.service.ProductPhotoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@RestController
@RequestMapping("/product-photo")
@Api(value = "Product Photo", tags = {"Product Photo"})
public class ProductPhotoController {

    @Autowired
    private ProductPhotoService service;

    @Autowired
    private AuthorizationConfig authorizationConfig;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadProductPhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam UUID productId,
                                                     @RequestHeader("Authorization") String token) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(service.uploadProductPhoto(file, productId));
    }

    @GetMapping(value = "/get", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProductPhoto(@RequestParam("productId") UUID productId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Epay Product Photo\"" + productId)
                .body(service.getPhoto(productId));
    }
}
