package com.contact_us.FlygulfAcademy.Controller;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.contact_us.FlygulfAcademy.Entity.MyCourses;
import com.contact_us.FlygulfAcademy.Service.MyCoursesService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/courses")
public class MyCoursesController {

	@Autowired
	private MyCoursesService myCoursesService;

	@PostMapping("/add")
	public ResponseEntity<Object> saveImage(@RequestParam String name, @RequestParam String description,
			@RequestParam(value = "icon", required = false) MultipartFile icon) {

		try {
			MyCourses saveCourses = myCoursesService.saveCourses(name, description, icon);

			if (saveCourses != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(saveCourses);
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
			Path iconPath = Path.of("CoursesIcon", iconName);
			byte[] iconBytes = Files.readAllBytes(iconPath);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
					.body(iconBytes);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@GetMapping("/{id}")
	public Optional<MyCourses> getCourseById(@PathVariable long id) {
		return myCoursesService.getCourseById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateCourses(@PathVariable Long id, @RequestParam String name,
			@RequestParam(value = "icon", required = false) MultipartFile icon) {

		try {
			MyCourses updatedCourses = myCoursesService.updateCourses(id, name, name, icon);

			if (updatedCourses != null) {

				return ResponseEntity.ok(updatedCourses);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courses not found");
			}

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating Courses: " + e.getMessage());
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
		boolean deleted = myCoursesService.deleteCourse(id);
		if(deleted) {
			return ResponseEntity.ok("Course Deleted successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
		}
	
	}
	
	@GetMapping("/get-all")
	public List<MyCourses> getAllCourses() {
	
		return myCoursesService.getAllCourses();
		
	}

}
