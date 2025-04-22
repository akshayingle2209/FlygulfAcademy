package com.contact_us.FlygulfAcademy.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.contact_us.FlygulfAcademy.Entity.MyServiceOffer;
import com.contact_us.FlygulfAcademy.Service.MyServiceOfferService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/service")
public class MyServiceOfferController {

	@Autowired
	private MyServiceOfferService myServiceOfferService;

	@PostMapping("/add")
	public ResponseEntity<Object> saveImage(@RequestParam String name,
			@RequestParam(value = "icon", required = false) MultipartFile icon) {

		try {
			MyServiceOffer saveService = myServiceOfferService.saveServices(name, icon);

			if (saveService != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(saveService);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Icon file is required");
			}

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Courses: " + e.getMessage());
		}

	}

	@GetMapping("/icon/{iconName}")
	public ResponseEntity<byte[]> getIcon(@PathVariable String iconName) {

		try {
			Path iconPath = Path.of("ServiceIcon", iconName);
			byte[] iconBytes = Files.readAllBytes(iconPath);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
					.body(iconBytes);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@GetMapping("/get-all")
	public List<MyServiceOffer> getAllServices() {

		return myServiceOfferService.getAllServices();
	}

	
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateService(@PathVariable Long id, @RequestParam String name,
			@RequestParam(value = "icon", required = false) MultipartFile icon) {

		try {
			MyServiceOffer updatedService = myServiceOfferService.updateService(id, name, icon);

			if (updatedService != null) {
				return ResponseEntity.ok(updatedService);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("service not found");
			}

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating service: " + e.getMessage());
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteService(@PathVariable Long id) {
		boolean deleted = myServiceOfferService.deleteService(id);
		if(deleted) {
			return ResponseEntity.ok("Service Deleted successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
		}
	
	}
	
	@GetMapping("{id}")
	public Optional<MyServiceOffer> getServiceById(@PathVariable Long id) {
		
		return myServiceOfferService.getServiceById(id);
		
	}

}
