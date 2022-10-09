package com.lms.user.constants;

public interface UserConstants {
	
	String ViewParticularCourse = "/api/v1.0/lms/courses/view/{courseName}";

	String ViewAllCourse = "/api/v1.0/lms/courses/getall";

	String LoginUser = "/api/v1.0/lms/company/login";
	
	String createUser = "/api/v1.0/lms/company/register";

	String ViewCourseBasedTech = "/api/v1.0/lms/courses/info/{technology}";

	String ViewCourseBasedTechAndDuration = "/api/v1.0/lms/courses/info/"
			+ "{technology}/{durationFrom}/{durationTo}";
	
	String registerCourse = "/api/v1.0/lms/courses/register/{courseId}";
	
	String courseRegisterList = "/api/v1.0/lms/course/registerList";

	
	

}
