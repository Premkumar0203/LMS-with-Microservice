package com.lms.admin.Exception;

public class LmsServiceException extends Exception {

	public LmsServiceException() {
	}

	public LmsServiceException(String m) {
		super(m);
	}

	public LmsServiceException(Exception e) {
		super(e);
	}

	public LmsServiceException(String m, Exception e) {
		super(m, e);
	}

}
