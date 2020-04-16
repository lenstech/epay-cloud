package com.lens.epay.repository;

import com.lens.epay.model.entity.ProfilePhoto;
import com.lens.epay.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfilePhotoRepository extends EpayRepository<ProfilePhoto, UUID> {
    ProfilePhoto findProfilePhotoById(UUID id);

    ProfilePhoto findProfilePhotoByUserId(UUID userId);

    Boolean existsByUser(User user);
}
