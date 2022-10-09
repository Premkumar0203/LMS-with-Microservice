package com.lms.admin.respository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lms.admin.model.Users;
import com.lms.admin.model.course;

public interface AdminRespository extends JpaRepository<course, Integer>
{

	@Query("select c from course c where c.courseName = ?1 and c.active=true")
	Optional<course> fetchCourseName(String courseName);

	@Transactional 
    @Modifying
	@Query("update course c set c.active=false where c.courseId = ?1")
	void updateStatus(int courseId);


}
