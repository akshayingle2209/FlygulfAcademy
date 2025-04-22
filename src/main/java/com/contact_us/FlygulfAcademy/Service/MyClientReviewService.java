package com.contact_us.FlygulfAcademy.Service;

import java.io.File; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contact_us.FlygulfAcademy.Entity.MyClientReviews;
import com.contact_us.FlygulfAcademy.Repository.MyClientReviewRepository;

@Service
public class MyClientReviewService {

	@Autowired
	private MyClientReviewRepository myClientReviewRepository;

	@Value("${app.image-reviews:ClientReviews}") // Ensure this directory exists
	private String IMAGE_DIR;

	public MyClientReviews saveClientReview(String name, String review, int rating, MultipartFile image)
			throws IOException {
		if (image == null || image.isEmpty()) {
			return null; // Validation: Image is required
		}

		MyClientReviews userReview = new MyClientReviews();
		userReview.setName(name);
		userReview.setReview(review);

		// Ensure rating is within the valid range (1 to 5)
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}
		userReview.setRating(rating);

		// Ensure image directory exists
		File imageDir = new File(IMAGE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}

		// Sanitize the file name
		String originalFileName = image.getOriginalFilename();
		String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");

		// Save image in the directory
		Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Set the stored image URL in the database
		userReview.setImageUrl("/api/review/image/" + sanitizedFileName); // Ensure this path matches the controller

		return myClientReviewRepository.save(userReview);
	}

	public List<MyClientReviews> getAllClientReview() {
		return myClientReviewRepository.findAll();
	}

	public MyClientReviews updateClientReviews(Long id, String name, String review, int rating, MultipartFile image)
			throws IOException {

		Optional<MyClientReviews> existingImage = myClientReviewRepository.findById(id);

		if (existingImage.isPresent()) {
			MyClientReviews exitImage = existingImage.get();

			exitImage.setName(name);
			exitImage.setReview(review);
			exitImage.setRating(rating);

			if (image != null && !image.isEmpty()) {

				File imageDir = new File(IMAGE_DIR);
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}

				String originalFileName = image.getOriginalFilename();
				String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
				Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
				Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				exitImage.setImageUrl("/api/review/image/" + sanitizedFileName);
			}
			return myClientReviewRepository.save(exitImage);
		} else {
			return null;
		}

	}

	public boolean deleteReview(Long id) {
		if (myClientReviewRepository.existsById(id)) {
			myClientReviewRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Optional<MyClientReviews> getReviewById(Long id) {
		return myClientReviewRepository.findById(id);
	}

}
