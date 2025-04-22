package com.contact_us.FlygulfAcademy.Service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contact_us.FlygulfAcademy.Entity.Admin;
import com.contact_us.FlygulfAcademy.Repository.AdminRepository;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
		return User.withUsername(admin.getUsername()).password(admin.getPassword()).build();
	}

}
