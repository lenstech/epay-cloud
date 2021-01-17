package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.constant.HttpSuccessMessagesConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.entity.ProductPhoto;
import com.lens.epay.repository.ProductPhotoRepository;
import com.lens.epay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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

    @Autowired
    private ProductService productService;

    @Transactional
    public String uploadProductPhoto(MultipartFile file, UUID productId) {
        ProductPhoto photo = repository.findProductPhotoByProductId(productId).orElse(new ProductPhoto(productService.fromIdToEntity(productId)));
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
        ProductPhoto photo = repository.findProductPhotoByProductId(productId).orElse(null);
        if (photo == null) {
            throw new BadRequestException(ErrorConstants.ID_IS_NOT_EXIST);
        }
        return photo.getFile();
    }

    @Transactional
    @Modifying
    public void deletePhotoByProductId(UUID productId) {
        if (repository.existsByProductId(productId)) {
            repository.deleteProductPhotoByProductId(productId);
        }
    }

    @Transactional
    public void deletePhoto(UUID photoId) {
        repository.deleteById(photoId);
    }


}
