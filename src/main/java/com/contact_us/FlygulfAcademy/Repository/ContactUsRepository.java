package com.contact_us.FlygulfAcademy.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact_us.FlygulfAcademy.Entity.ContactUs;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {

	//ContactUs save(ContactUs contactUs);

	
	
}
