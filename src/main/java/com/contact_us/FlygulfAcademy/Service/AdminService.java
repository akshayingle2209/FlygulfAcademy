package com.contact_us.FlygulfAcademy.Service;

import java.util.List;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.contact_us.FlygulfAcademy.Entity.Admin;
import com.contact_us.FlygulfAcademy.Repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	private final ConcurrentHashMap<String, Boolean> activeUsers = new ConcurrentHashMap<>();

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String registerUser(Admin admin) {
		Optional<Admin> existingUser = adminRepository.findByUsername(admin.getUsername());
		if (existingUser.isPresent()) {
			return "User already exists!";
		}

		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		adminRepository.save(admin);
		return "User registered successfully!";

	}

	// -------------------------------------------------------------------------------------------------------

	public String loginUser(Admin admin) {
		Admin existingUser = adminRepository.findByUsername(admin.getUsername()).orElse(null);

		if (existingUser == null) {
			return "Invalid username or password!";
		}

		if (!passwordEncoder.matches(admin.getPassword(), existingUser.getPassword())) {
			return "Invalid username or password!";
		}

		return "Login successful!";
	}

	// -------------------------------------------------------------------------------------------------------

	public String userLogin(Admin admin) {
		Admin existingUser = adminRepository.findByUsername(admin.getUsername()).orElse(null);

		if (existingUser == null || !passwordEncoder.matches(admin.getPassword(), existingUser.getPassword())) {
			return "Invalid username or password!";
		}

		activeUsers.put(admin.getUsername(), true); // Mark user as logged in
		return "Login successful!";
	}
	
	

	public String userLogout(String username) {
		if (activeUsers.containsKey(username)) {
			activeUsers.remove(username); // Remove user from active session
			return "Logout successful!";
		}
		return "User not logged in!";
	}

	// -------------------------------------------------------------------------------------------------------



	// -------------------------------------------------------------------------------------------------------

	public Admin createAdmin(Admin admin) {

		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		return adminRepository.save(admin);
	}
//-------------------------------------------------------------------------------------------------------

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

//-------------------------------------------------------------------------------------------------------

	public Optional<Admin> getAdminById(long id) {
		return adminRepository.findById(id);
	}

//-------------------------------------------------------------------------------------------------------

	public Admin updateAdmin(long id, Admin adminDetails) {
		Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		admin.setUsername(adminDetails.getUsername());
		admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
		return adminRepository.save(admin);

	}

//-------------------------------------------------------------------------------------------------------

	public void deleteAdmin(Long id) {

		adminRepository.deleteById(id);

	}

}
