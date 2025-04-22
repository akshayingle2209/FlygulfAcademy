package com.contact_us.FlygulfAcademy.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contact_us.FlygulfAcademy.Entity.ContactUs;
import com.contact_us.FlygulfAcademy.Service.ContactUsService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin("*")
@RequestMapping("/contact")
public class ContactUsController {

	@Autowired
	private ContactUsService contactUsService;


	@PostMapping("/submit")
	public ResponseEntity<String> saveAndNotify(@RequestBody ContactUs message) {
		try {
			contactUsService.saveAndNotify(message);
			return ResponseEntity.ok("Your message has been received. Thank you for contacting us!");
		} catch (MessagingException e) {
			return ResponseEntity.status(500).body("Failed to send email. Please try again later.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Something went wrong. Please try again.");
		}
	}

	@GetMapping("/getAll")
	public List<ContactUs> getAllContact() {
		return contactUsService.getAllContacts();
	}

	@GetMapping("/{id}")
	public Optional<ContactUs> getcontactByid(@PathVariable long id) {
		return contactUsService.getcontactByid(id);
	}

	@DeleteMapping("{id}")
	public String deleteContact(@PathVariable long id) {
		contactUsService.deleteContact(id);
		return "Contact Delete Successfully";
	}

}
