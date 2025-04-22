package com.contact_us.FlygulfAcademy.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.contact_us.FlygulfAcademy.Entity.MyGallery;
import com.contact_us.FlygulfAcademy.Service.MyGalleryService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/gallery")
public class MyGalleryController {

	@Autowired
	private MyGalleryService myGalleryService;

	@PostMapping("/add")
	public ResponseEntity<Object> saveImage(@RequestParam String title,
			@RequestParam(value = "image", required = false) MultipartFile image) {
		try {
			MyGallery saveImage = myGalleryService.saveImage(title, image);

			if (saveImage != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(saveImage); // Return MyGallery object
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is required");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving gallery: " + e.getMessage());
		}
	}

	@GetMapping("/images/{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
		try {
			Path imagePath = Path.of("GalleryImages", imageName);
			byte[] imageBytes = Files.readAllBytes(imagePath);

			return ResponseEntity.ok()
					.header(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
					.body(imageBytes);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("{id}")
	public Optional<MyGallery> getImageById(@PathVariable long id) {
		return myGalleryService.getImageById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateGallery(@PathVariable Long id, @RequestParam String title,
			@RequestParam(value = "image", required = false) MultipartFile image) {
		try {
			MyGallery updatedGallery = myGalleryService.updateGallery(id, title, image);

			if (updatedGallery != null) {
				return ResponseEntity.ok(updatedGallery); // ✅ Return updated entity
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gallery not found"); // ✅ Return 404 if not
																								// found
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating gallery: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
		boolean deleted = myGalleryService.deleteBlog(id);
		if (deleted) {
			return ResponseEntity.ok("Blog deleted successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
		}
	}
	
	@GetMapping("/get-all")
	public List<MyGallery> getAllGallery() {
		return myGalleryService.getAllGallery();
	}

}
