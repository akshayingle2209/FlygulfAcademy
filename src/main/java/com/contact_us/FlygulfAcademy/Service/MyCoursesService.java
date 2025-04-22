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
import com.contact_us.FlygulfAcademy.Repository.MyCoursesRepository;
import com.contact_us.FlygulfAcademy.Repository.MyGalleryRepository;

@Service
public class MyCoursesService {

	@Autowired
	private MyCoursesRepository myCoursesRepository;

	@Value("${app.image-courses:CoursesIcon}")
	private String IMAGE_DIR;

	public MyCourses saveCourses(String name, String description, MultipartFile icon) throws IOException {

		if (icon == null || icon.isEmpty()) {
			return null;
		}

		MyCourses courses = new MyCourses();
		courses.setName(name);
		courses.setDescription(description);

		File imageDir = new File(IMAGE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}

		String originalFileName = icon.getOriginalFilename();
		String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
		Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
		Files.copy(icon.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		courses.setIcon("/api/courses/icon/" + sanitizedFileName);

		return myCoursesRepository.save(courses);
	}

	public Optional<MyCourses> getCourseById(long id) {
		return myCoursesRepository.findById(id);

	}

	public MyCourses updateCourses(Long id, String name, String description, MultipartFile icon) throws IOException {
		Optional<MyCourses> existingIcon = myCoursesRepository.findById(id);

		if (existingIcon.isPresent()) {
			MyCourses existingCourse = existingIcon.get();

			existingCourse.setName(name);
			existingCourse.setDescription(description);

			if (icon != null && !icon.isEmpty()) {
				File imageDir = new File(IMAGE_DIR);
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}

				String originalFileName = icon.getOriginalFilename();
				String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
				Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);
				Files.copy(icon.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				existingCourse.setIcon("/api/course/icon/" + sanitizedFileName);
			}

			return myCoursesRepository.save(existingCourse); // Save the correct entity
		} else {
			return null;
		}
	}

	public boolean deleteCourse(Long id) {

		if (myCoursesRepository.existsById(id)) {
			myCoursesRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<MyCourses> getAllCourses() {

		return myCoursesRepository.findAll();

	}

}
