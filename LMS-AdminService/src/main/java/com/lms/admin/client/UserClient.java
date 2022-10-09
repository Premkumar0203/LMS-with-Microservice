package com.lms.admin.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.lms.admin.model.register;


@FeignClient("User-Service")
public interface UserClient {
	

	@GetMapping("/api/v1.0/lms/course/registerList")
	List<register> getCourseRegisterList();

}
