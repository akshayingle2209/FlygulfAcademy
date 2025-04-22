package com.contact_us.FlygulfAcademy.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact_us.FlygulfAcademy.Entity.MyCourses;

@Repository
public interface MyCoursesRepository extends JpaRepository<MyCourses, Long> {	
}
