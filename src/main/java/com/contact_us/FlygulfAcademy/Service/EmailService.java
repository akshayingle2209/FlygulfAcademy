package com.contact_us.FlygulfAcademy.Service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.val;

@Service
public class EmailService {

	 @Autowired
	    private JavaMailSender mailSender;

	    public void sendEmail(String to, String subject, String body) throws MessagingException {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("themailfortesting7@gmail.com");
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }

}
