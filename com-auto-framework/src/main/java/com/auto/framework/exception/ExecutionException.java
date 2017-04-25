package com.auto.framework.exception;

import com.auto.framework.logs.AutoLogger;

public class ExecutionException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private AutoLogger log = new AutoLogger("ExecutionException");
	
	public ExecutionException(String step, String message)
	{
		super(message);
		
		log.error(step, message, this);
	}
}
