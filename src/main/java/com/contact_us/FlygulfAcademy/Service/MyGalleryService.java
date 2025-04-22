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
import com.contact_us.FlygulfAcademy.Entity.MyGallery;
import com.contact_us.FlygulfAcademy.Repository.MyGalleryRepository;

@Service
public class MyGalleryService {

	@Autowired
	private MyGalleryRepository myGalleryRepository;

	@Value("${app.image-gallery:GalleryImages}")
	private String IMAGE_DIR;

	public MyGallery saveImage(String title, MultipartFile image) throws IOException {
		if (image == null || image.isEmpty()) {
			return null; // If no image is provided, return null
		}

		MyGallery gallery = new MyGallery();
		gallery.setTitle(title);

		// Ensure the directory exists
		File imageDir = new File(IMAGE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}

		// Sanitize and save image
		String originalFileName = image.getOriginalFilename();
		String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
		Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);

		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Set image URL
		gallery.setImageUrl("/api/gallery/images/" + sanitizedFileName);

		return myGalleryRepository.save(gallery);
	}

	public Optional<MyGallery> getImageById(long id) {
		return myGalleryRepository.findById(id);
	}

	public MyGallery updateGallery(Long id, String title, MultipartFile image) throws IOException {
		Optional<MyGallery> existingGallery = myGalleryRepository.findById(id);

		if (existingGallery.isPresent()) {
			MyGallery existingImage = existingGallery.get();
			existingImage.setTitle(title);

			if (image != null && !image.isEmpty()) {
				// Ensure directory exists
				File imageDir = new File(IMAGE_DIR);
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}

				// Save new image
				String originalFileName = image.getOriginalFilename();
				String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
				Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);

				Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				// Update image URL
				existingImage.setImageUrl("/api/gallery/images/" + sanitizedFileName);
			}

			// Save updated entity
			return myGalleryRepository.save(existingImage);
		} else {
			return null; // Handle in the controller with proper HTTP response
		}
	}

	public boolean deleteBlog(Long id) {
		if (myGalleryRepository.existsById(id)) {
			myGalleryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<MyGallery> getAllGallery() {

		return myGalleryRepository.findAll();

	}

}
