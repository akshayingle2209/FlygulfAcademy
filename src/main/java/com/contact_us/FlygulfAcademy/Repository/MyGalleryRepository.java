package com.contact_us.FlygulfAcademy.Repository;

import java.util.function.ToLongFunction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact_us.FlygulfAcademy.Entity.MyGallery;

@Repository
public interface MyGalleryRepository extends JpaRepository<MyGallery, Long> {

}
