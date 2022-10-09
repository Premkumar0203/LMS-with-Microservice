package com.lms.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lms.user.Exception.LmsServiceException;
import com.lms.user.constants.*;
import com.lms.user.model.Users;
import com.lms.user.model.course;
import com.lms.user.model.register;
import com.lms.user.security.JwtTokenUtil;
import com.lms.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;
	
	private static final Logger logger = LogManager.getLogger(UserController.class);

	
	@Operation(summary = "Create User", description = "Create a User")
	@PostMapping(UserConstants.createUser)
	public ResponseEntity<?> CreateUser(@RequestBody Users user) throws LmsServiceException
	{
		logger.info("Create User --> Started  ");
		logger.debug("Create User --> Started  ");
		Users u= service.createUser(user);
		if(u != null)
		{
			logger.info("User creation - Successfully Done");
			return new ResponseEntity<>(u,HttpStatus.CREATED);
		}
		else
		{
			logger.info("Fail to create User");
			return new ResponseEntity<>("Fail to create User",HttpStatus.NOT_ACCEPTABLE);

		}
	}
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Operation(summary = "Register Course", description = "Register Course")
	@PostMapping(UserConstants.registerCourse)
	public ResponseEntity<?> CreateUser(HttpServletRequest request,@PathVariable String courseId) throws LmsServiceException
	{
		logger.info("Register Course --> Started  ");
		logger.debug("Register Course --> Started  ");
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		register u= service.RegisterCourse(courseId,username);
		if(u != null)
		{
			logger.info("Course Registration - Successfully Done");
			return new ResponseEntity<>(u,HttpStatus.CREATED);
		}
		else
		{
			logger.info("Fail to register course");
			return new ResponseEntity<>("Fail to register course",HttpStatus.NOT_ACCEPTABLE);

		}
	}
	
	@Operation(summary = "View Particular Course", description = " able to View any particular course")
	@GetMapping(UserConstants.ViewParticularCourse)
	public ResponseEntity<?> ViewParticularCourse(@PathVariable String courseId) throws LmsServiceException
	{
		logger.info("View Particular Course --> Started ");
		logger.debug("View ParticularCourse --> Started ");
		course course= service.ViewParticularCourse(courseId);
		if(course != null)
		{
			logger.info("View Particular Course ---> "+ course);
			return ResponseEntity.ok(course);
		}
		else
		{
			logger.info("Fail to view Particular course");
			return (ResponseEntity<?>) ResponseEntity.notFound();

		}
		
	}
	
	
	
	@Operation(summary = "View All Course Registration List", description = "View All Course Registration List")
	@GetMapping(UserConstants.courseRegisterList)
	public ResponseEntity<?> ViewAllCourseRegister() throws LmsServiceException
	{
		logger.info("View All Course registration --> Started ");
		logger.debug("View All Course registration --> Started ");
		List<register> course= service.ViewAllCourseRegister();
		logger.info("View All Course registration  - End");
		logger.debug("View All Course registration - End");
		return ResponseEntity.ok(course);
	}
	
	
	@Operation(summary = "View All Course", description = " able to View all course")
	@GetMapping(UserConstants.ViewAllCourse)
	public ResponseEntity<?> ViewAllCourse() throws LmsServiceException
	{
		logger.info("View All Course --> Started ");
		logger.debug("View All Course --> Started ");
		List<course> course= service.ViewAllCourse();
		logger.info("View All Course  - End");
		logger.debug("View All Course  - End");
		return ResponseEntity.ok(course);
	}
	
	@Operation(summary = "View Course based on Technology", description = "View Course based on Technology")
	@GetMapping(UserConstants.ViewCourseBasedTech)
	public ResponseEntity<?> ViewCourseBasedTechnology(@PathVariable String technology) throws LmsServiceException
	{
		logger.info("View Course based on Technology --> Started ");
		logger.debug("View Course based on Technology --> Started ");
		course course= service.ViewCourseBasedTechnology(technology);
		if(course != null)
		{
			logger.info("View Course based on Technology ---> "+ course);
			return ResponseEntity.ok(course);
		}
		else
		{
			logger.info("Fail to view course based on Technology");
			return (ResponseEntity<?>) ResponseEntity.notFound();

		}
		
	}
	
	@Operation(summary = "View Course based on Technology and Duration", description = "View Course based on Technology and Duration")
	@GetMapping(UserConstants.ViewCourseBasedTechAndDuration)
	public ResponseEntity<?> ViewCourseBasedTechnologyAndDuration(@PathVariable String technology,
			@PathVariable String durationFrom,@PathVariable String durationTo) throws LmsServiceException
	{
		logger.info("View Course based on Technology and Duration --> Started ");
		logger.debug("View Course based on Technology and Duration --> Started ");
		course course= service.ViewCourseBasedTechnologyAndDuration(technology,durationFrom,durationTo);
		if(course != null)
		{
			logger.info("View Course based on Technology and Duration ---> "+ course);
			return ResponseEntity.ok(course);
		}
		else
		{
			logger.info("Fail to view course based on Technology and Duration");
			return (ResponseEntity<?>) ResponseEntity.notFound();

		}
		
	}

}