package com.contact_us.FlygulfAcademy.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.contact_us.FlygulfAcademy.Entity.Admin;
import com.contact_us.FlygulfAcademy.Service.AdminService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/register")
	public String registerUser(@RequestBody Admin admin) {
		return adminService.registerUser(admin);
	}

//	@PostMapping("/login")
//	public ResponseEntity<String> loginUser(@RequestBody Admin admin) {
//		String response = adminService.loginUser(admin);
//
//		if (response.equals("Login successful!")) {
//			return ResponseEntity.ok(response);
//		}
//
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//	}
//	

	// ----------------------------------------------------------------------------

	@PostMapping("/login")
	public ResponseEntity<String> userLogin(@RequestBody Admin admin) {
		String response = adminService.loginUser(admin);

		if (response.equals("Login successful!")) {
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

//	@PostMapping("/logout")
//    public ResponseEntity<String> userLogout(@RequestParam String username) {
//        String response = adminService.userLogout(username);
//        return ResponseEntity.ok(response);
//    
//	}
	
	
	@PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        // Invalidate the session to log the user out
        session.invalidate();

        // Create a response map
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");

        // Return success response
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	//---------------------------------------------------------------------------------



	@GetMapping("/all")
	public List<Admin> getAllAdmins() {
		return adminService.getAllAdmins();
	}

	@GetMapping("/{id}")
	public Optional<Admin> getAdminById(@PathVariable long id) {
		return adminService.getAdminById(id);

	}

	@PutMapping("/{id}")
	public Admin updateAdmin(@PathVariable long id, @RequestBody Admin adminDetails) {

		return adminService.updateAdmin(id, adminDetails);

	}

	@DeleteMapping("/{id}")
	public String deleteAdmin(@PathVariable long id) {
		adminService.deleteAdmin(id);

		return "Admin deleted successfully";
	}

}
