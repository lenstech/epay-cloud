package com.lens.epay.util;

/**
 * Created by Emir Gökdemir
 * on 28 Haz 2020
 */

import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.lens.epay.constant.ErrorConstants.MAIL_SEND_FAILED;
import static com.lens.epay.constant.MailConstants.*;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendActivationMail(User user, String activationToken, String subject, String text) {
        String emailAddress = user.getEmail();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText( text);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(String.format(simpleMailMessage.getText(), activationToken), true);
            mimeMessageHelper.setTo(emailAddress);
            mimeMessage.setSubject(simpleMailMessage.getSubject());

//            FileSystemResource file= new FileSystemResource(new File("/home/ilkaygunel/Desktop/notlar.txt"));
//            mimeMessageHelper.addAttachment("Notes", file);

        } catch (MessagingException messagingException) {
            throw new BadRequestException(MAIL_SEND_FAILED);
        }
        mailSender.send(mimeMessage);
    }

}
