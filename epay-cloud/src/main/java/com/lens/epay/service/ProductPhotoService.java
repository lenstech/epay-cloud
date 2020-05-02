package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.constant.HttpSuccessMessagesConstants;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.entity.ProductPhoto;
import com.lens.epay.repository.ProductPhotoRepository;
import com.lens.epay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */
@Service
public class ProductPhotoService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotoRepository repository;

    @Transactional
    public String uploadProductPhoto(MultipartFile file, UUID productId) {
        Product product = productRepository.findOneById(productId);
        if (product == null) {
            throw new UnauthorizedException(ErrorConstants.PRODUCT_NOT_EXIST);
        }
        ProductPhoto photo = new ProductPhoto();
        photo.setProduct(product);
        try {
            photo.setFile(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        repository.save(photo);
        return HttpSuccessMessagesConstants.PHOTO_SUCCESSFULLY_UPLOADED;
    }

    @Transactional
    public byte[] getPhoto(UUID productId) {
        ProductPhoto photo = repository.findProductPhotoByProductId(productId);
        if (photo == null) {
            return null;
        }
        return photo.getFile();
    }
}