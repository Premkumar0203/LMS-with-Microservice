package com.lms.user.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lms.user.model.register;


public interface registerRespository extends JpaRepository<register, Integer> 
{

	@Query("select count(c) from register c where c.courseId = ?1  and c.UserId = ?2")
	int findDuplicate(String courseId, int userId);

}
