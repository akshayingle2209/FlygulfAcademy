package com.contact_us.FlygulfAcademy.Service;

import java.io.File; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DomainCombiner;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contact_us.FlygulfAcademy.Entity.MyBlog;
import com.contact_us.FlygulfAcademy.Repository.MyBlogRepository;

@Service
public class MyBlogService {

	@Autowired
	private MyBlogRepository myBlogRepository;

	@Value("${app.image-blog:BlogImages}")
	private String IMAGE_DIR;

	public MyBlog saveBlogWithImage(String title, String content, MultipartFile image) throws IOException {
		MyBlog myBlog = new MyBlog();
		myBlog.setTitle(title);
		myBlog.setContent(content);

		if (image != null && !image.isEmpty()) {
			File imageDir = new File(IMAGE_DIR);
			if (!imageDir.exists()) {
				imageDir.mkdirs();
			}

			String originalFileName = image.getOriginalFilename();
			String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
			Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);

			Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// Store relative path to access image
			myBlog.setImageUrl("/api/blogs/images/" + sanitizedFileName);
		}

		return myBlogRepository.save(myBlog);
	}

	public Optional<MyBlog> getBlogById(Long id) {
		return myBlogRepository.findById(id);
	}

	public MyBlog updateBlog(Long id, String title, String content, MultipartFile image) throws IOException {
		Optional<MyBlog> existingBlogOpt = myBlogRepository.findById(id);

		if (existingBlogOpt.isPresent()) {
			MyBlog existingBlog = existingBlogOpt.get();
			existingBlog.setTitle(title);
			existingBlog.setContent(content);

			if (image != null && !image.isEmpty()) {
				File imageDir = new File(IMAGE_DIR);
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}

				String originalFileName = image.getOriginalFilename();
				String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
				Path filePath = Path.of(IMAGE_DIR, sanitizedFileName);

				Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				existingBlog.setImageUrl("/api/blogs/images/" + sanitizedFileName);
			}

			return myBlogRepository.save(existingBlog);
		} else {
			return null;
		}
	}
	
	public boolean deleteBlog(Long id) {
        if (myBlogRepository.existsById(id)) {
            myBlogRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
	public List<MyBlog> getAllBlogs() {
        return myBlogRepository.findAll();
    }
	

}
