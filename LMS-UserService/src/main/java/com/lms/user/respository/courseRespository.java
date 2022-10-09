package com.lms.user.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lms.user.model.Users;
import com.lms.user.model.course;

public interface courseRespository extends JpaRepository<course, Integer>
{

	@Query("select c from course c where c.courseName = ?1 ")
	Optional<course> fetchCourseName(String courseName);

}
