package com.onixx.apolloveiculos.api.Services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMail;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void enviarEmailSimples(String to, String subject, String mensagem) {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setTo(to);
        simpleMail.setSubject(subject);
        simpleMail.setText(mensagem);
        javaMail.send(simpleMail);
    }

    @Async
    public void emailTemplate(String to, String subject, Context variaveisEmail, String emailTemplate) {

        String process = templateEngine.process(emailTemplate, variaveisEmail);

        MimeMessage message = javaMail.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(process, true);
            FileSystemResource logo = new FileSystemResource(
                    new File("src/main/resources/static/images/logoApollo.png"));
            helper.addInline("logoApollo", logo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMail.send(message);
    }
}