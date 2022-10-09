package com.lms.user.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lms.user.model.course;

@FeignClient("Admin-Service")
public interface CourseClients
{

	@GetMapping("/api/v1.0/lms/admin/courses/getall")
	List<course> getCourseList();


}
