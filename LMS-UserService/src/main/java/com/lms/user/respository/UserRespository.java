package com.lms.user.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lms.user.model.Users;
import com.lms.user.model.course;

public interface UserRespository extends JpaRepository<Users, Integer>
{

	@Query("select c from Users c where c.userName = ?1  and c.password = ?2")
	Optional<Users> getUserDetails(String username, String password);

	
	@Query("select c from Users c where c.userName = ?1")
	Optional<Users> fetchUserName(String username);


}
