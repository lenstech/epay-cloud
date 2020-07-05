package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.lens.epay.constant.MailConstants.*;


/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Service
public class ConfirmationTokenService {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailUtil mailUtil;

    public void sendResetPasswordsToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new BadRequestException(ErrorConstants.MAIL_NOT_EXIST);
        }
        mailUtil.sendActivationMail(user,
                jwtGenerator.generateMailConfirmationToken(user.getId()),
                RESET_PASSWORD_HEADER,
                RESET_PASSWORD_BODY + "\n" + CLIENT_URL + RESET_PASSWORD_URL);
        // TODO: 20 Şub 2020 Maile gönderilen adres değiştirilecek ki kullanıcı o adrese frontendde gidecek ve sonrasında şifre değişecek.
    }
}

