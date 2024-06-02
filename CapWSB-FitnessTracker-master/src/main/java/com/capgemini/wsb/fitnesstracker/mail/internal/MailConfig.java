package com.capgemini.wsb.fitnesstracker.mail.internal;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // set mailSender properties (host, port, username, password, etc.)
        mailSender.setHost("sandbox.smtp.mailtrap.io");
        mailSender.setPort(2525);
        mailSender.setUsername("UserName");
        mailSender.setPassword("UserPassword");
        //mailSender.setProtocol("smtps");

        Properties props = mailSender.getJavaMailProperties();
        //props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");

        //System.out.println("MailConfig...");

        return mailSender;
    }

}
