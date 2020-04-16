package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtGenerator;
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
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserRepository userRepository;


    public void sendActivationToken(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String confirmationToken = jwtGenerator.generateMailConfirmationToken(user.getId());
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(CONFIRM_ACCOUNT_HEADER);
        mailMessage.setText(CONFIRM_ACCOUNT_BODY
                + this.environment.getProperty("spring.url")
                + CONFIRM_ACCOUNT_URL + confirmationToken);
        sendMail(mailMessage);

    }

    public void sendResetPasswordsToken(String email) {
        User user = userRepository.findByEmail(email);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String confirmationToken = jwtGenerator.generateMailConfirmationToken(user.getId());
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(RESET_PASSWORD_HEADER);
        mailMessage.setText(RESET_PASSWORD_BODY
                + this.environment.getProperty("spring.url")
                + RESET_PASSWORD_URL + confirmationToken);
        // TODO: 20 Şub 2020 Maile gönderilen adres değiştirilecek ki kullanıcı o adrese frontendde gidecek ve sonrasında şifre değişecek.
        sendMail(mailMessage);
    }

    private void sendMail(SimpleMailMessage mailMessage){
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException exception) {
            throw new BadRequestException(ErrorConstants.MAIL_SEND_FAILED);
        }
    }
}

