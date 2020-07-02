package com.lens.epay.util;

/**
 * Created by Emir Gökdemir
 * on 28 Haz 2020
 */

import com.lens.epay.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationMail(User user, String activationToken) {
        String emailAddress = user.getEmail();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Hesabınızı Aktifleştirin");
        simpleMailMessage.setText("Hesabınızın aktifleştirilmesi için bu linke tıklayınız: \nhttp://localhost:8080/MemberRestAPIProject/activateMemberWebServiceEndpoint/activateMember?activationToken=%s/");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(String.format(simpleMailMessage.getText(), activationToken), true);
            mimeMessageHelper.setTo(emailAddress);
            mimeMessage.setSubject(simpleMailMessage.getSubject());

//            FileSystemResource file= new FileSystemResource(new File("/home/ilkaygunel/Desktop/notlar.txt"));
//            mimeMessageHelper.addAttachment("Notes", file);

        } catch (MessagingException messagingException) {

        }
        mailSender.send(mimeMessage);
    }

}
