package com.lms.admin.constants;

public interface AdminConstants {
	
	String LoginAdmin = "/api/v1.0/lms/admin/login";
	
	String CreateCourse = "/api/v1.0/lms/admin/courses/add/";

	String ViewParticularCourse = "/api/v1.0/lms/admin/courses/view/{courseName}";

	String ViewAllCourse = "/api/v1.0/lms/admin/courses/getall";

	String deleteCourse = "/api/v1.0/lms/admin/courses/delete/{courseName}";

}
