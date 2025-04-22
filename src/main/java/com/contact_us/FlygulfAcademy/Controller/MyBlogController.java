package com.contact_us.FlygulfAcademy.Controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact_us.FlygulfAcademy.Entity.MyBlog;
import com.contact_us.FlygulfAcademy.Service.MyBlogService;

@Controller
@CrossOrigin("*")
@RequestMapping("/api/blogs")
public class MyBlogController {

	@Autowired
	private MyBlogService myBlogService;

	@PostMapping("/save")
	public ResponseEntity<Object> saveBlog(@RequestParam String title, @RequestParam String content,
			@RequestParam(value = "image", required = false) MultipartFile image) {

		try {
			MyBlog savedBlog = myBlogService.saveBlogWithImage(title, content, image);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBlog);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving blog: " + e.getMessage());
		}
	}

	@GetMapping("/images/{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
		try {
			Path imagePath = Path.of("BlogImages", imageName);
			byte[] imageBytes = Files.readAllBytes(imagePath);

			return ResponseEntity.ok()
					.header(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
					.body(imageBytes);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

////////////////////////////////////////////////////////////////////////////////

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBlogById(@PathVariable Long id) {
		Optional<MyBlog> blog = myBlogService.getBlogById(id);

		if (blog.isPresent()) {
			return ResponseEntity.ok(blog.get()); // Returns MyBlog object
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found"); // Returns String message
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateBlog(@PathVariable Long id, @RequestParam String title,
			@RequestParam String content, @RequestParam(value = "image", required = false) MultipartFile image) {

		try {
			MyBlog updatedBlog = myBlogService.updateBlog(id, title, content, image);
			if (updatedBlog != null) {
				return ResponseEntity.ok(updatedBlog);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating blog: " + e.getMessage());
		}
	}
	
	  @DeleteMapping("/delete/{id}")
	    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
	        boolean deleted = myBlogService.deleteBlog(id);
	        if (deleted) {
	            return ResponseEntity.ok("Blog deleted successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
	        }
	    }
	  
	   @GetMapping("/get-all")
	    public ResponseEntity<List<MyBlog>> getAllBlogs() {
	        List<MyBlog> blogs = myBlogService.getAllBlogs();
	        return ResponseEntity.ok(blogs);
	    }
	}


