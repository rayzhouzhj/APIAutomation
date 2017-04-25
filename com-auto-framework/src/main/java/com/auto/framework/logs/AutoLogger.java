package com.auto.framework.logs;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.auto.framework.test.TestContext;

public class AutoLogger
{
	private Logger logger;
	
    public AutoLogger(String name) 
    {
        logger = Logger.getLogger(name);
    }
    
    public void debug(Object message) 
    {
    	logger.debug(message);
    }
    
    private void updateErrorMessage(String message)
    {
    	String firstErrMsg = TestContext.getInstance().getVariable("First_Error_Msg");
    	
    	if(firstErrMsg == null || firstErrMsg.isEmpty())
    	{
    		TestContext.getInstance().setVariable("First_Error_Msg", "ERROR: " + message.replaceAll("<a[^>]+>ScreenShot</a>", ""));
    	}
    	else if(firstErrMsg.startsWith("Warning"))
    	{
    		TestContext.getInstance().setVariable("First_Error_Msg", "WARNING: " + message.replaceAll("<a[^>]+>ScreenShot</a>", ""));
    	}
    	else
    	{
    		// DO NOTHING
    	}
    }
    
    public void error(String stepName, Object message) 
    {
    	updateErrorMessage(message.toString());
    	TestContext.getInstance().setVariable("Status", "Failed");
    	TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("ERROR: " + message);
		
    	logger.error(message);
    }
    
    public void error(String stepName, Object message, Throwable e) 
    {
    	updateErrorMessage(message.toString());
    	TestContext.getInstance().setVariable("Status", "Failed");
    	TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("ERROR: " + message);
		
    	logger.error(message, e);
    }
    
    public void fatal(String stepName, Object message) 
    {
    	updateErrorMessage(message.toString());
    	
    	TestContext.getInstance().setVariable("Status", "Failed");
		TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("FATAL ERROR: " + message);
    	logger.fatal(message);
    	
    	Assert.fail(message.toString());
    }
    
    public void fatal(String stepName, Object message, Throwable e) 
    {
    	updateErrorMessage(message.toString());
    	
    	TestContext.getInstance().setVariable("Status", "Failed");
		TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("FATAL ERROR: " + message);
    	logger.fatal(message, e);
    	
    	Assert.fail(message.toString());
    }
    
	public void info(Object message)
	{
		TestContext.getInstance().setVariable("Current_Step", "Log Message");
		System.out.println("INFO: " + message);
		logger.info(message);
	}
    
	public void info(String stepName, String message)
	{
		TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("INFO: " + message);
		logger.info(message);
	}
	
    public void warn(String stepName, Object message) 
    {
    	updateErrorMessage(message.toString());
    	
    	if(!"FAILED".equalsIgnoreCase(TestContext.getInstance().getVariable("Status")))
		{
			TestContext.getInstance().setVariable("Status", "Warning");
		}
    	TestContext.getInstance().setVariable("Current_Step", stepName);
		System.out.println("INFO: " + message);
		
    	logger.warn(message);
    }
}