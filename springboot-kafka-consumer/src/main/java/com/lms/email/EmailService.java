// Java Program to Illustrate Creation Of
// Service Interface

package com.lms.email;

import com.lms.Models.EmailDetails;

// Importing required classes

// Interface
public interface EmailService {

	// Method
	// To send a simple email
	String sendSimpleMail(EmailDetails details);

}
