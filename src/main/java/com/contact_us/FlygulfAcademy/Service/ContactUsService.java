package com.contact_us.FlygulfAcademy.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.contact_us.FlygulfAcademy.Entity.ContactUs;
import com.contact_us.FlygulfAcademy.Repository.ContactUsRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ContactUsService {

	@Autowired
	private ContactUsRepository contactUsRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender javaMailSender;

	public void saveAndNotify(ContactUs message) throws MessagingException {
		// 1. Save contact form data to the database
		contactUsRepository.save(message);

		// ======================== A. Send Email to Admin // ========================
		MimeMessage adminMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper adminHelper = new MimeMessageHelper(adminMessage, true, "utf-8");

		adminHelper.setTo("Rahul.aher@flygulfacademy.com");
		adminHelper.setSubject("New Contact Form Request Received");

		String adminContent = String.format(
				"""
						<html>
						<body>
						Hello,<br><br>

						You’ve received a new inquiry via the contact form on your website. Here are the submitted details:<br><br>

						<b>Full Name</b>: %s<br>
						<b>Email Address</b>: %s<br>
						<b>Contact Number</b>: %s<br>
						<b>Service Type</b>: %s<br><br>

						<b>Message:</b><br>
						%s<br><br>

						Kindly follow up with the person at your earliest convenience.<br><br>

						Thank you,<br>
						<b>Website Contact Notification</b>
						</body>
						</html>
						""",
				message.getName(), message.getEmail(), message.getPhone(), message.getServiceType(),
				message.getMessage());

		adminHelper.setText(adminContent, true); // Set HTML content
		javaMailSender.send(adminMessage);

		// ======================== B. Send Auto-reply to User (Plain Text)
		// ========================
		MimeMessage userReply = javaMailSender.createMimeMessage();
		MimeMessageHelper userHelper = new MimeMessageHelper(userReply, false, "utf-8");

		userHelper.setFrom("Rahul.aher@flygulfacademy.com");
		userHelper.setTo(message.getEmail());
		userHelper.setSubject("Thank You for Contacting FlyGulf Academy");

		String userContent = String.format(
				"""
						Dear %s,

						Thank you for reaching out to FlyGulf Academy. We’ve received your message and our team will get back to you shortly.

						Here’s a copy of your message:
						-------------------------------
						%s

						If this was not sent by you, please ignore this email.

						Best regards,
						FlyGulf Academy Team
						""",
				message.getName(), message.getMessage());

		userReply.setText(userContent);
		javaMailSender.send(userReply);
	}


	public List<ContactUs> getAllContacts() {
		return contactUsRepository.findAll();
	}

	public Optional<ContactUs> getcontactByid(Long id) {
		return contactUsRepository.findById(id);
	}

	public void deleteContact(long id) {
		contactUsRepository.deleteById(id);
	}
}
