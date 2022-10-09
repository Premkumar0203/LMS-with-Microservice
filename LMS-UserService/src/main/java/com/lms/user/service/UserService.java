package com.lms.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.lms.user.Exception.LmsServiceException;
import com.lms.user.clients.CourseClients;
import com.lms.user.model.Users;
import com.lms.user.model.course;
import com.lms.user.model.register;
import com.lms.user.respository.UserRespository;
import com.lms.user.respository.courseRespository;
import com.lms.user.respository.registerRespository;

@Service
public class UserService {

	@Autowired
	courseRespository CourseResp ;
	
	@Autowired
	UserRespository UserResp ;
	
	@Autowired
	CourseClients courseClient;
	
	private static final Logger logger = LogManager.getLogger(UserService.class);

	public static int lineNumber() {
		return new Throwable().getStackTrace()[2].getLineNumber();
	}
	
	public Users createUser(Users user) throws LmsServiceException
	{
		Optional<Users> u = UserResp.fetchUserName(user.getUserName());
		if(u.isPresent())
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =This UserName already exits");
			throw new LmsServiceException("This UserName already exits");
		}
		else
		{
			if( !(user.getEmailId().contains("@") && user.getEmailId().contains(".com")) )
			{
				throw new LmsServiceException("Invalid Email Format");
			}
			if( !user.getPassword().matches("[a-zA-Z0-9]{8}+") )
			{
				throw new LmsServiceException("Invalid Password,Password contains Alphanumeric and atleast 8 character");
			}
			Users us = new Users();
			us= UserResp.save(user);
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context = User Created");
			return us;
		}
	}
	
	
	public course ViewParticularCourse(String courseName) throws LmsServiceException 
	{
		List<course> u = courseClient.getCourseList();
		if(u.size() > 0)
		{
			List<course> c = u.stream().filter(i -> i.getCourseName().equals(courseName))
								.filter(i -> i.getActive()==true)
								.collect(Collectors.toList());
			if(!c.isEmpty())
				return c.get(0);
			else
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context = Unable to find that Particular Course : "+courseName);
				throw new LmsServiceException("Unable to find that Particular Course : "+ courseName);

			}
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context = No Course Available : "+courseName);
			throw new LmsServiceException("No Course Available"+ courseName);
		}
	}

	public List<course> ViewAllCourse() throws LmsServiceException 
	{
		List<course>  list= courseClient.getCourseList().stream().filter(i -> i.getActive()==true)
				.collect(Collectors.toList());
		if(list.isEmpty())
		{
			logger.error("No Course Found");
			throw new LmsServiceException("No Course Found");
		}
		else
			return list;
	}

	public course ViewCourseBasedTechnologyAndDuration(String technology, String durationFrom, String durationTo) throws LmsServiceException
	{
		List<course> u = courseClient.getCourseList();
		if(u.size() > 0)
		{
			List<course> c = u.stream().filter(i -> i.getTechnlogy().equals(technology))
									.filter( i -> ( i.getDuration() > Integer.parseInt(durationFrom) 
											&& i.getDuration() < Integer.parseInt(durationTo)))
									.filter(i -> i.getActive()==true)
								.collect(Collectors.toList());
			if(!c.isEmpty())
				return c.get(0);
			else
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context = Unable to find that Particular Course based on Technology and duration ");
				throw new LmsServiceException("Unable to find that Particular Course based on Technology and duration ");

			}
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context = No Course Available ");
			throw new LmsServiceException("No Course Available");
		}
	}

	public course ViewCourseBasedTechnology(String technology) throws LmsServiceException {
		List<course> u = courseClient.getCourseList();
		if(u.size() > 0)
		{
			List<course> c = u.stream().filter(i -> i.getTechnlogy().equals(technology))
									   .filter(i -> i.getActive()==true)
								.collect(Collectors.toList());
			if(!c.isEmpty())
				return c.get(0);
			else
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context = Unable to find that Particular Course based on Technology ");
				throw new LmsServiceException("Unable to find that Particular Course based on Technology: "+ technology);
			}
		}
		else
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context = No Course Available ");
			throw new LmsServiceException("No Course Available");
		}
	}
	
	
	@Autowired
	registerRespository resResp;

	public register RegisterCourse(String courseId, String username) throws LmsServiceException 
	{
		List<course>  list= courseClient.getCourseList().stream()
				.filter(i -> i.getActive()==true)
				.filter(i -> i.getCourseId() == Integer.parseInt(courseId) )
				.collect(Collectors.toList());
		if(list.isEmpty())
		{
			logger.error("No Course Found");
			throw new LmsServiceException("No Course Found");
		}
		else
		{
			Optional<Users> u = UserResp.fetchUserName(username);
			if(u.isPresent())
			{
				 
				if(resResp.findDuplicate(courseId,u.get().getUserId()) == 0)
				{
					register r = new register();
					r.setCourseId(courseId);
					r.setUserId(u.get().getUserId());
					r.setEmailId(u.get().getEmailId());
					return resResp.save(r);
				}
				else
				{
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context = Already User register this course ");
					throw new LmsServiceException("Already User register this course");
				}
				
			}
			else
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context = Unable to find user details ");
				throw new LmsServiceException("Unable to find user details");
			}
		}
		
		
		
	}

	public List<register> ViewAllCourseRegister()
	{
		return resResp.findAll();
	}

	


}
