package com.contact_us.FlygulfAcademy.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact_us.FlygulfAcademy.Entity.MyServiceOffer;

@Repository
public interface MyServiceOfferRepository extends JpaRepository<MyServiceOffer, Long>{
	
}
