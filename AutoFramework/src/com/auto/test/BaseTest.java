package com.auto.test;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.auto.logs.FormatHTMLLayout;
import com.auto.logs.AutoLogger;
import com.auto.logs.NewLogForEachRunFileAppender;
import com.auto.utils.LogSetting;

public abstract class BaseTest {
	
	@Rule 
	public TestName name = null;
	public AutoLogger logger = null;
	private String logFolder = null;
	private String logFile = null;

	@Rule
	public TestWatcherMan watchman = new TestWatcherMan();

	class TestWatcherMan extends TestWatcher
	{
		@Override
		protected void failed(Throwable e, Description description) {

			super.failed(e, description);

			TestContext.getInstance().setVariable("Status", "Failed");
			logger.error("End Test", "Test flow is completed with error.");
			// Update Log status
			LogSetting.updateLogStatus();
		}

		@Override
		protected void succeeded(Description description) {
			super.succeeded(description);

			if("RUNNING".equalsIgnoreCase(TestContext.getInstance().getVariable("Status")))
			{
				TestContext.getInstance().setVariable("Status", "Passed");
			}

			logger.info("End Test", "Test flow completed.");
			// Update Log status
			LogSetting.updateLogStatus();
		}
	}

	public String getLogFolder()
	{
		return this.logFolder;
	}

	public String getLogFile()
	{
		return this.logFile;
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("INFO: Setting up test environment...");
		initilize();
	}

	@After
	public void tearDown() throws Exception {

		System.out.println("INFO: Tearing down test environment...");

		try
		{
			

		} catch (Exception e) {

			//			info("Tear Down", "Fail to Tear down test environment", false);
		}

		if("Failed".equalsIgnoreCase(TestContext.getInstance().getVariable("Status")))
		{
			Assert.fail();
		}
	}

	public BaseTest()
	{
		name = new TestName();
	}

	public void initilize()
	{
		InitTestData();
		// Set Screen Timeout
		setLogger();
	}

	public void InitTestData()
	{
		TestContext.getInstance().setVariable("Status", "Running");
	}

	public void setLogger()
	{
		logger = new AutoLogger(name.getMethodName());

		Logger rootLogger = Logger.getRootLogger();

		Enumeration<?> appenders = rootLogger.getAllAppenders();
		FileAppender appender = null;
		while(appenders.hasMoreElements())
		{
			Appender currAppender = (Appender) appenders.nextElement();
			if(currAppender instanceof NewLogForEachRunFileAppender)
			{
				appender = (FileAppender) currAppender;
			}
		}
		if(appender != null)
		{
			rootLogger.removeAppender(appender);
		}

		appender = new NewLogForEachRunFileAppender();


		logFolder = "log"
				+ File.separator + DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(LocalDate.now())
				+ File.separator + this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1) 
				+ File.separator + name.getMethodName();

		appender.setName("FileLogger");
		appender.setFile(logFolder);
		appender.setLayout(new FormatHTMLLayout(this.getClass().getName() + "." + name.getMethodName()));
		appender.setAppend(false);
		//		appender.setThreshold(Priority.WARN);
		appender.activateOptions();

		rootLogger.addAppender(appender);

		logFolder = Paths.get(appender.getFile()).getParent().toString();
		logFile = appender.getFile();

		TestContext.getInstance().setVariable("LogFolder", logFolder);
		TestContext.getInstance().setVariable("LogFile", logFile);

		LogSetting.createLogNavigator();
	}

	public void info(String message)
	{
		logger.info(message);
	}

	public void info(String stepName, String message)
	{
		logger.info(stepName, message);
	}

	public void warn(String stepName, String message)
	{
		logger.warn(stepName, message);
	}

	public void fail(String stepName, String message)
	{
		logger.error(stepName, message);
	}

	public void failAndExit(String stepName, String message)
	{
		logger.fatal(stepName, message);
		Assert.fail(message);
	}

	public void sleep(long millionSeconds)
	{
		try 
		{
			Thread.sleep(millionSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
