package com.lms.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.lms.admin.Exception.LmsServiceException;
import com.lms.admin.constants.*;
import com.lms.admin.model.Users;
import com.lms.admin.model.course;
import com.lms.admin.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class AdminController {
	
	@Autowired
	private AdminService service;
	
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);

	@Operation(summary = "Login Admin", description = "Login using Admin Credientials")
	@PostMapping(AdminConstants.LoginAdmin)
	public ResponseEntity<?> LoginAdmin(@RequestBody User Users) throws LmsServiceException
	{
		logger.info("Admin Login Validation --> Started ");
		logger.debug("Admin Login Validation --> Started ");
		String Status=null;
		if(service.ValidateAdmin(Users))
		{
			Status = "Successfully Login ";
			logger.info("Admin Login Validation  - Successfully Done");
			return new ResponseEntity<>(Status,HttpStatus.OK);
		}
		else
		{
			Status = "Failed to Login";
			logger.info("Fail to validate AdminCredientials");
			return new ResponseEntity<>(Status,HttpStatus.NOT_ACCEPTABLE);
		}	
	}
	
	@Operation(summary = "Create Course", description = "Create a course by Admin")
	@PostMapping(AdminConstants.CreateCourse)
	public ResponseEntity<?> CreateCourse(@RequestBody course c) throws LmsServiceException
	{
		logger.info("Create Course --> Started  ");
		logger.debug("Create Course --> Started  ");
		course course= service.createCourse(c);
		if(course != null)
		{
			logger.info("course creation - Successfully Done");
			return new ResponseEntity<>(course,HttpStatus.CREATED);
		}
		else
		{
			logger.info("Fail to create course");
			return new ResponseEntity<>("Fail to create course",HttpStatus.NOT_ACCEPTABLE);

		}
	}
	
	@Operation(summary = "View Particular Course", description = "Admin able to View any particular course")
	@GetMapping(AdminConstants.ViewParticularCourse)
	public ResponseEntity<?> ViewParticularCourse(@PathVariable String courseName) throws LmsServiceException
	{
		logger.info("View Particular Course --> Started ");
		logger.debug("View ParticularCourse --> Started ");
		course course= service.ViewParticularCourse(courseName);
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
	
	@Operation(summary = "View All Course", description = "Admin able to View all course")
	@GetMapping(AdminConstants.ViewAllCourse)
	public ResponseEntity<?> ViewAllCourse() throws LmsServiceException
	{
		logger.info("View All Course --> Started ");
		logger.debug("View All Course --> Started ");
		List<course> course= service.ViewAllCourse();
		logger.info("View All Course  - End");
		logger.debug("View All Course  - End");
		return ResponseEntity.ok(course);
	}
	
	@Operation(summary = "Delete Particular Course", description = "Admin able to Delete Particular course ")
	@PostMapping(AdminConstants.deleteCourse)
	public ResponseEntity<?> DeleteParticularCourse(@PathVariable String courseName) throws LmsServiceException
	{
		logger.info("Delete Particular Course --> Started ");
		logger.debug("Delete Particular Course --> Started ");
		String status = service.DeleteParticularCourse(courseName);
		logger.info("Delete Particular Course  - End");
		logger.debug("Delete Particular Course  - End");
		return ResponseEntity.ok(status);
	}
	
	
	

}
