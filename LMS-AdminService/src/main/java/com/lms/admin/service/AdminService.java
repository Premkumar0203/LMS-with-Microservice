package com.lms.admin.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.lms.admin.Exception.LmsServiceException;
import com.lms.admin.client.UserClient;
import com.lms.admin.model.*;
import com.lms.admin.respository.AdminRespository;


@Service
public class AdminService {

	@Autowired
	AdminRespository resp ;
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPIC = "Kafka-Email-topic";
	
	private static final Logger logger = LogManager.getLogger(AdminService.class);

	public static int lineNumber() {
		return new Throwable().getStackTrace()[2].getLineNumber();
	}
	
	public boolean ValidateAdmin(User user)
	{
		if(user.getName().equals("admin") && user.getPassword().equals("admin@123"))
			return true;
		else 
			return false;
	}

	public course createCourse(course c) throws LmsServiceException 
	{
		
		Optional<course> u = resp.fetchCourseName(c.getCourseName());
		if(u.isPresent())
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =This Course already exits");
			throw new LmsServiceException("This Course already exits");
		}
		else
		{
			if(c.getCourseName().length()< 30)
				throw new LmsServiceException("Course Name must be minimum of 30 character");
			if(c.getDescription().length()< 100)
				throw new LmsServiceException("Course Description must be minimum of 100 character");
			course us = new course();
			c.setActive(true);
			us= resp.save(c);
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =Course Created");
			return us;
		}
		
	}

	public course ViewParticularCourse(String courseName) throws LmsServiceException 
	{
		Optional<course> u = resp.fetchCourseName(courseName);
		if(u.isPresent())
		{
			 if(u.get().getActive())
			{
				 course c = u.get();
				 return c;

			}
			else
			 {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + ": This Course : "+ courseName + " is not active ");
				throw new LmsServiceException("This Course : "+ courseName + " is not active");
			 }
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + ": Unable to find that Particular Course :  "+ courseName);
			throw new LmsServiceException("Unable to find that Particular Course : "+ courseName);
		}
	}

	public List<course> ViewAllCourse() throws LmsServiceException 
	{
		List<course>  list= resp.findAll();
		if(list.isEmpty())
		{
			logger.error("No Course Found");
			throw new LmsServiceException("No Course Found");
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + ":  Course are available  "); 
			return list.stream().filter(course ->  course.getActive()== true).collect(Collectors.toList());

		}
	}

	
	@Autowired
	UserClient userClient;
	
	public String DeleteParticularCourse(String courseName) throws LmsServiceException {
		Optional<course> u = resp.fetchCourseName(courseName);
		if(u.isPresent())
		{
			if(u.get().getActive())
			{
				course c = u.get();
				resp.updateStatus(c.getCourseId());
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + "The Course "+ courseName + "is not active hereAfter");
				
				List<register> reglist = userClient.getCourseRegisterList().stream()
						.filter(e -> Integer.parseInt(e.getCourseId()) == c.getCourseId() ).collect(Collectors.toList());
				if(!reglist.isEmpty())
				{
					for(register r : reglist)
					{
						String Status = "The "+courseName + " Course is"+
							 " not available here after." + "|" + r.getEmailId();
//						EmailDetails details = new EmailDetails(r.getEmailId(),Status
//								,"Course Avaliability Status Update");
				        kafkaTemplate.send(TOPIC, Status);
					}
				}
				return "The Course is not active hereAfter"; 
			}
			else
			 {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + "The Course "+ courseName + " is not active, so unable to delete");
				throw new LmsServiceException("This Course : "+ courseName + " is not active, so unable to delete");
			 }
			
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber() + "Unable to find that Particular Course : "+ courseName + "");
			throw new LmsServiceException(""+ courseName);
		}
	}

}
