package com.lens.epay.service;

import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.mapper.MinimalUserMapper;
import com.lens.epay.mapper.UserMapper;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.dto.user.UpdatePasswordDto;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinimalUserMapper minimalUserMapper;

    public CompleteUserResource getSelfProfile(UUID selfId) {
        User selfUser = userRepository.findUserById(selfId);
        if (selfUser == null) {
            throw new NotFoundException(USER_NOT_EXIST);
        } else {
            return userMapper.toResource(selfUser);
        }
    }

    public MinimalUserResource getOtherProfile(String email) {
        User otherUser = userRepository.findByEmail(email);
        if (otherUser == null) {
            throw new NotFoundException(USER_NOT_EXIST);
        }
        return minimalUserMapper.toResource(otherUser);
    }

    @Transactional
    public CompleteUserResource updateProfile(UUID userId, RegisterFirmUserDto dto) {
        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new NotFoundException(USER_NOT_EXIST);
        }
        User updatedUser = userMapper.toEntity(dto);
        updatedUser.setId(oldUser.getId());
        updatedUser.setPassword(oldUser.getPassword());
        updatedUser.setCreatedDate(oldUser.getCreatedDate());
        userRepository.save(updatedUser);
        return userMapper.toResource(updatedUser);
    }

    @Transactional
    public CompleteUserResource updatePassword(UUID userId, UpdatePasswordDto dto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(USER_NOT_EXIST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(OLD_PASSWORD_IS_WRONG);
        }
        String newPassword = dto.getNewPassword();
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException(NEW_PASSWORD_CANNOT_BE_SAME_AS_OLD);
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        return userMapper.toResource(user);
    }

}
