package com.contact_us.FlygulfAcademy.Controller;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.contact_us.FlygulfAcademy.Entity.MyClientReviews;
import com.contact_us.FlygulfAcademy.Service.MyClientReviewService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/review")
public class MyClientReviewController {

	@Autowired
	private MyClientReviewService myClientReviewService;

	@PostMapping("/saveReview")
	public ResponseEntity<Object> saveClientReview(@RequestParam String name, @RequestParam String review,
			@RequestParam int rating, @RequestParam(value = "image", required = false) MultipartFile image) {

		try {
			MyClientReviews savedReview = myClientReviewService.saveClientReview(name, review, rating, image);
			if (savedReview != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is required");
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving review: " + e.getMessage());
		}
	}

	@GetMapping("/image/{imageName}")
	public ResponseEntity<byte[]> getReviewImage(@PathVariable String imageName) {
		try {
			// Ensure the path matches the one used in saveClientReview
			Path imagePath = Path.of("ClientReviews", imageName);

			if (!Files.exists(imagePath)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			byte[] imageBytes = Files.readAllBytes(imagePath);

			return ResponseEntity.ok().header(org.springframework.http.HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.body(imageBytes);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/get-all")
	public List<MyClientReviews> getAllClientReview() {
		return myClientReviewService.getAllClientReview();
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateClientReviews(@PathVariable Long id, @RequestParam String name,
			@RequestParam String review, int rating,
			@RequestParam(value = "image", required = false) MultipartFile image) {

		try {
			MyClientReviews updatedReview = myClientReviewService.updateClientReviews(id, name, review, rating, image);

			if (updatedReview != null) {
				return ResponseEntity.ok(updatedReview);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating reviews");
			}

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating Courses: " + e.getMessage());
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteReview(@PathVariable Long id) {
		boolean deleted = myClientReviewService.deleteReview(id);
		if(deleted) {
			return ResponseEntity.ok("Review deleted succesfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
		}
	}
	
	@GetMapping("{id}")
	public Optional<MyClientReviews> getReviewById(@PathVariable long id) {
		return myClientReviewService.getReviewById(id);
	}

}
