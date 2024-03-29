package com.lens.epay.service;

import com.lens.epay.constant.ErrorConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.entity.User;
import com.lens.epay.security.JwtGenerator;
import com.lens.epay.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lens.epay.constant.MailConstants.*;


/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Service
public class TokenService {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UserService userService;

    public void sendActivationTokenToMail(User user) {
        String confirmationToken = jwtGenerator.generateMailConfirmationToken(user.getId());
        mailUtil.sendTokenMail(user.getEmail(), confirmationToken, CONFIRM_ACCOUNT_HEADER,
                CONFIRM_ACCOUNT_BODY
                        + CLIENT_URL
                        + CONFIRM_ACCOUNT_URL);
    }

    public void sendResetPasswordTokenToMail(String email) {
        if (email == null) {
            throw new BadRequestException(ErrorConstants.PROVIDE_VALID_MAIL);
        }
        User user = userService.findUserByEmail(email);
        String resetToken = jwtGenerator.generateResetPasswordToken(user.getId());
        mailUtil.sendTokenMail(user.getEmail(), resetToken, RESET_PASSWORD_HEADER,
                RESET_PASSWORD_BODY + "\n"
                        + CLIENT_URL
                        + RESET_PASSWORD_URL);

    }
}

