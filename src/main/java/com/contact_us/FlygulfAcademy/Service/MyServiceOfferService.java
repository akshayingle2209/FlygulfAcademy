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

import com.contact_us.FlygulfAcademy.Entity.MyCourses;
import com.contact_us.FlygulfAcademy.Entity.MyServiceOffer;
import com.contact_us.FlygulfAcademy.Repository.MyServiceOfferRepository;

@Service
public class MyServiceOfferService {

	@Autowired
	private MyServiceOfferRepository myServiceOfferRepository;

	@Value("${app.image-services:ServiceIcon}")
	private String IMAGE_DIR;

	public MyServiceOffer saveServices(String name, MultipartFile icon) throws IOException {

		if (icon == null || icon.isEmpty()) {
			return null;
		}
		MyServiceOffer myServiceOffer = new MyServiceOffer();
		myServiceOffer.setName(name);

		File imageDir = new File(IMAGE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}

		String originalFileName = icon.getOriginalFilename();
		String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
		Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
		Files.copy(icon.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		myServiceOffer.setIcon("/api/service/icon/" + sanitizedFileName);

		return myServiceOfferRepository.save(myServiceOffer);

	}

	public List<MyServiceOffer> getAllServices() {

		return myServiceOfferRepository.findAll();
	}

	public MyServiceOffer updateService(Long id, String name, MultipartFile icon) throws IOException {
		Optional<MyServiceOffer> existingIcon = myServiceOfferRepository.findById(id);

		if (existingIcon.isPresent()) {
			MyServiceOffer existingService = existingIcon.get();
			existingService.setName(name);

			if (icon != null && !icon.isEmpty()) {
				File imageDir = new File(IMAGE_DIR);
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}
				String originalFileName = icon.getOriginalFilename();
				String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
				Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
				Files.copy(icon.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				existingService.setIcon("/api/service/icon/" + sanitizedFileName);

			}

			return myServiceOfferRepository.save(existingService);

		} else {

			return null;
		}

	}
	
	public boolean deleteService(Long id) {

		if (myServiceOfferRepository.existsById(id)) {
			myServiceOfferRepository.deleteById(id);
			return true;
		}
		return false;
	}

	
	public Optional<MyServiceOffer> getServiceById(Long id) {
		return myServiceOfferRepository.findById(id);
	}
	
	
	
	
	
}