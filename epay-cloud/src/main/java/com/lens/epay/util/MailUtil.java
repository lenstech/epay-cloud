package com.lens.epay.util;

/**
 * Created by Emir Gökdemir
 * on 28 Haz 2020
 */

import com.lens.epay.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.lens.epay.constant.ErrorConstants.MAIL_SEND_FAILED;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTokenMail(String email, String activationToken, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            mimeMessage.setText(String.format(text, activationToken), "UTF-8");
            mimeMessage.setSubject(subject);
        } catch (MessagingException messagingException) {
            throw new BadRequestException(MAIL_SEND_FAILED);
        }
        mailSender.send(mimeMessage);
    }
}
