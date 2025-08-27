package com.onixx.apolloveiculos.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMail;

    @Autowired
    private TemplateEngine templateEngine;
    