package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.constant.HttpSuccessMessagesConstants;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.model.entity.ProfilePhoto;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.ProfilePhotoRepository;
import com.lens.epay.repository.UserRepository;
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
public class ProfilePhotoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfilePhotoRepository repository;

    public String uploadImage(MultipartFile file, UUID idFromToken) {
        User user = userRepository.findUserById(idFromToken);
        if (user == null) {
            throw new UnauthorizedException(ErrorConstants.USER_NOT_EXIST);
        }
        ProfilePhoto photo = new ProfilePhoto();
        photo.setUser(user);


        try {
            photo.setFile(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        repository.save(photo);
        return HttpSuccessMessagesConstants.PHOTO_SUCCESSFULLY_UPLOADED;
    }

    @Transactional
    public byte[] getPhoto(UUID userId) {
        ProfilePhoto photo = repository.findProfilePhotoByUserId(userId);
        if (photo == null) {
            return null;
        }
        return photo.getFile();
    }
}
