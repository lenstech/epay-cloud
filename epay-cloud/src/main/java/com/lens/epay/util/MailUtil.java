package com.lens.epay.util;

/**
 * Created by Emir Gökdemir
 * on 28 Haz 2020
 */

import com.lens.epay.constant.MailConstants;
import com.lens.epay.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;

import static com.lens.epay.constant.ErrorConstants.MAIL_SEND_FAILED;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTokenMail(String email, String activationToken, String subject, String text) {
        sendMail(email, subject, String.format(text, activationToken));
    }

    public void sendMail(String email, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            mimeMessage.setText(text, "UTF-8");
            mimeMessage.setSubject(subject, "UTF-8");
        } catch (MessagingException messagingException) {
            throw new BadRequestException(MAIL_SEND_FAILED);
        }
        mailSender.send(mimeMessage);
    }

    public void sendOrderInfoMailToCustomer(String email) {
        sendMail(email, MailConstants.CUSTOMER_ORDER_HEADER, MailConstants.CUSTOMER_ORDER_CONTENT);
    }

    public void sendOrderInfoMailToSeller(String email, String customerName, BigDecimal cost) {
        sendMail(email, MailConstants.SELLER_ORDER_HEADER,
                String.format(MailConstants.SELLER_ORDER_CONTENT, customerName, "₺ " + cost));
    }
}
