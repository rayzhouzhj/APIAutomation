package com.auto.tools.models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.auto.tools.util.TaskManagerUtil;
import com.auto.utils.CommandPrompt;


public class TestCase implements Runnable {
	private int m_scriptId = 0;
	private String m_processID;
	private String m_tcPath;
	private String m_classPath;
	private String m_scriptDisplayName;
	private String m_junitTestName;
	private String m_description;
	private String m_timeout;
	private String m_testData = "";
	private int m_scriptPriority;
	private int m_timeout_sec;
	private int m_executionTime_sec = 0;
	private String m_resultfilePath = "";
	private String m_resultFolderPath = "";
	private String m_firstErrorMsg = "";
	private TestCaseStatus m_status = TestCaseStatus.NA;
	private Process m_process;
	private LocalDateTime m_startTime;
	private LocalDateTime m_endTime;
	private Logger m_logger = Logger.getLogger("TestCase");
	private AtomicBoolean m_stopExecutionFlag = new AtomicBoolean(false);
	private boolean m_isLockedByMe = false;
	
	private Object statusLock = new Object();

	public int getScriptId()
	{
		return m_scriptId;
	}

	public String getDescription()
	{
		return m_description;
	}

	public String getJunitTestName()
	{
		return m_junitTestName;
	}

	public void setJunitTestName(String junitTestName)
	{
		this.m_junitTestName = junitTestName;
	}

	public String getTestData()
	{
		return m_testData == null? "" : m_testData;
	}

	public void setTestData(String data)
	{
		this.m_testData = data;
	}

	public void setDescription(String description)
	{
		m_description = description;
	}

	public int getScriptPriority()
	{
		return m_scriptPriority;
	}

	public String getFirstErrorMessage()
	{
		return m_firstErrorMsg == null? "" : m_firstErrorMsg;
	}

	public String getStartTimeAsString()
	{
		if(m_startTime != null)
		{
			DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
			return format.format(m_startTime);
		}
		return "";
	}

	public LocalDateTime getStartTime()
	{
		return this.m_startTime;
	}

	public String getEndTimeAsString()
	{
		if(m_endTime != null)
		{
			DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
			return format.format(m_endTime);
		}
		return "";
	}

	public LocalDateTime getEndTime()
	{
		return this.m_endTime;
	}

	public void setStartTime(String startTime)
	{
		if(!startTime.isEmpty())
		{
			this.m_startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
		}
		else
		{
			this.m_startTime = null;
		}
	}

	public void setStartTime(LocalDateTime time)
	{
		this.m_startTime = time;
	}

	public void setEndTime(LocalDateTime time)
	{
		this.m_endTime = time;
	}

	public void setEndTime(String endTime)
	{
		if(!endTime.isEmpty())
		{
			this.m_endTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
		}
		else
		{
			this.m_endTime = null;
		}
	}

	public String getTcPath() {
		return m_tcPath;
	}

	public String getResultFilePath() {
		return m_resultfilePath;
	}

	public void increaseExecTime(int execTime)
	{
		m_executionTime_sec = m_executionTime_sec + execTime;
	}

	public boolean isTimeout()
	{
		if(m_timeout.isEmpty())
		{
			return false;
		}
		else
		{
			int timeoutBufferSec = 90;
			return m_executionTime_sec > (m_timeout_sec + timeoutBufferSec);
		}
	}

	public String getScriptName() {
		return m_scriptDisplayName == null? "" : m_scriptDisplayName;
	}

	public int getTimeoutSec() {
		return m_timeout_sec;
	}

	public String getTimeout() {
		return m_timeout;
	}

	public void setTimeout(String timeout) {
		Pattern pattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}");
		if(pattern.matcher(timeout).matches())
		{
			m_timeout = timeout;
			String[] timeArray = m_timeout.split(":");
			int hour = Integer.parseInt(timeArray[0].trim());
			int minute = Integer.parseInt(timeArray[1].trim());

			m_timeout_sec = hour * 3600 + minute * 60;
		}
		else
		{
			m_timeout = "";
			m_logger.info("Timeout String is invalid: " + timeout);
		}
	}

	public TestCaseStatus getStatus() {
		synchronized (statusLock) 
		{
			return m_status;
		}
	}

	public void setStatus(TestCaseStatus m_status) {
		synchronized (statusLock) 
		{
			this.m_status = m_status;
		}
	}

	public TestCase(JUnitScript script)
	{
		script.turnoffRef();
		this.m_scriptId = script.getIDAsInt();
		this.setTestData(script.getParameters());
		this.m_firstErrorMsg = script.getComment();
		this.setStartTime(script.getStartTime());
		this.setEndTime(script.getEndTime());

		this.initTestCaseData(script.getName(), 
				script.getScriptPath(), 
				0, 
				script.getRuntime(), 
				script.getStatus());

		this.m_resultfilePath = script.getReportFile();
	}

	public TestCase(String tcPath)
	{
		this.initTestCaseData(tcPath.substring(tcPath.lastIndexOf(".") + 1), 
				tcPath,
				0,
				"00:30",
				TestCaseStatus.NA);
	}


	public TestCase(int id, String testName, String tcPath, String testData, int priority, String timeout)
	{
		this.m_scriptId = id;
		this.m_testData = testData;
		this.initTestCaseData(testName, tcPath, priority, timeout, TestCaseStatus.NA);
	}

	public TestCase(
			int id, 
			String testName, 
			String tcPath, 
			String testData,
			int priority, 
			String timeout, 
			String startTime,
			String endTime,
			String status, 
			String comment, 
			String report)
	{
		this.m_scriptId = id;
		this.m_testData = testData;
		this.m_firstErrorMsg = comment;
		this.m_resultfilePath = report;
		this.setStartTime(startTime);
		this.setEndTime(endTime);

		this.initTestCaseData(testName, tcPath, priority, timeout, TestCaseStatus.valueOf(status));
	}

	private void initTestCaseData(String testName, String tcPath, int priority, String timeout, TestCaseStatus status)
	{
		this.m_scriptDisplayName = testName;
		this.m_junitTestName = tcPath.substring(tcPath.lastIndexOf(".") + 1);
		m_scriptPriority = priority;
		m_tcPath = tcPath;
		m_status = status;
		m_classPath = tcPath.substring(0, m_tcPath.lastIndexOf("."));
		this.setTimeout(timeout);

		//		calculateResultFilePath();
	}

	public void calculateResultFilePath()
	{
		// Skip this step if test case is not associated with device
		String logFolderPath = "log" + File.separator 
				+ File.separator + DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(LocalDate.now())
				+ File.separator + m_classPath.substring(m_classPath.lastIndexOf(".") + 1)
				+ File.separator + this.getJunitTestName();

		Path logFolder = Paths.get(logFolderPath);
		int nextSession = 1;

		if(Files.exists(logFolder))
		{
			try {
				List<String> sessionList = Files.list(logFolder).filter(path -> path.getFileName().toString().startsWith("session"))
						.map(path -> path.getFileName().toString()).collect(Collectors.toList());

				for(String name : sessionList)
				{
					try
					{
						int currentSession = Integer.valueOf(name.substring(7));
						if(currentSession >= nextSession)
						{
							nextSession = currentSession + 1; 
						}
					}
					catch(Exception e)
					{
						// DO Nothing
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.m_resultFolderPath = logFolderPath +  File.separator +  "session" + nextSession;
		this.m_resultfilePath = this.m_resultFolderPath + File.separator + "result.html";
	}

	public String generateExecutionCommand()
	{
		String statement = "java -cp \"libs" + File.separator +"*\" "
                + "com.auto.executor.TestExecutor "
				+ this.getTcPath().substring(0, this.getTcPath().lastIndexOf(".")) + " "
				+ this.getJunitTestName() + " "
				+ 1 + " "
				+ this.getTestData();

		return statement;
	}


	public void run()
	{ 
		m_executionTime_sec = 0;
		m_process = null;

		try 
		{
			m_logger.info("Start Running Test Script: " + m_scriptDisplayName);

			m_status = TestCaseStatus.Running;

			this.lockJVM();

			List<String> existingProcessList,newProcessList;

			// For windows
			if(System.getProperty("os.name").contains("Windows"))
			{
				existingProcessList = TaskManagerUtil.findProcess("java.exe");
			}
			// For MAC
			else
			{
				existingProcessList = TaskManagerUtil.findProcess("com.auto.executor.TestExecutor");
			}

			// Set Start Time
			m_startTime = LocalDateTime.now();

			// Get execution command
			String statement = generateExecutionCommand();
			m_logger.info("Exec Command: " + statement);

			// Exit if script is stopped before real execution
			if(this.m_stopExecutionFlag.get()) return;

			this.calculateResultFilePath();
			
			// Start execution
			m_process = CommandPrompt.executeCommand(statement); 

			Thread.sleep(3000);

			// For windows
			if(System.getProperty("os.name").contains("Windows"))
			{
				newProcessList = TaskManagerUtil.findProcess("java.exe");
			}
			// For MAC
			else
			{
				newProcessList = TaskManagerUtil.findProcess("com.auto.executor.TestExecutor");
			}

			for(String process : existingProcessList)
			{
				newProcessList.remove(process);
			}

			if(newProcessList.size() > 0)
			{
				m_processID = newProcessList.get(0);
				System.out.println("INFO: New java.exe process is running, PID: " + m_processID);
			}
			else
			{
				System.out.println("INFO: No new java.exe is created.");
			}

			this.releaseJVMProcessLock();

			new Thread(new StreamDrainer(m_process.getInputStream())).start();

			m_process.waitFor();
			m_process.destroy();

			// Set End Time
			m_endTime = LocalDateTime.now();

			m_logger.info("Complete Running Test Script: " + m_scriptDisplayName);

			readTestResult();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopExecution()
	{
		// If still locking JVM, release the lock
		releaseJVMProcessLock();
		
		m_stopExecutionFlag.set(true);

		if(m_process == null)
		{
			m_logger.info("No active running process");
			return;
		}

		try
		{
			m_logger.info("Stop the current test execution: " + this.m_scriptDisplayName);

			this.m_status = TestCaseStatus.Aborted;
			
			// Set End Time
			m_endTime = LocalDateTime.now();

			System.out.println("INFO: Stopping TestCase...");
			if(m_processID != null)
			{
				TaskManagerUtil.killProcess(m_processID);
				m_processID = null;
			}

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public boolean createJVMProcessLock()
	{
		try
		{
			Path lockFile = Paths.get("process.lck");

			if(!Files.exists(lockFile))
			{
				Files.createFile(lockFile);
			}
			else
			{
				return false;
			}

			System.out.println("INFO: Created Process lock.");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean releaseJVMProcessLock()
	{
		if(!this.m_isLockedByMe) return true;
		
		try
		{
			Path lockFile = Paths.get("process.lck");
			if(!Files.exists(lockFile))
			{
				System.out.println("WARN: Lock file was removed abnormally");
				return true;
			}
			else
			{
				System.out.println("INFO: Released Process lock.");
				Files.delete(lockFile);
			}

			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public void lockJVM()
	{
		// Lock device
		if(this.createJVMProcessLock())
		{
			System.out.println("INFO: Lock JVM successfully.");
		}
		else
		{
			LocalDateTime startTime = LocalDateTime.now();
			LocalDateTime endTime = LocalDateTime.now();

			while(!this.m_stopExecutionFlag.get() && startTime.until(endTime, ChronoUnit.SECONDS) < 90)
			{
				System.out.println("INFO: JVM is locked, wait for 5 seconds and try again.");
				try 
				{
					Thread.sleep(5000);
					if(this.createJVMProcessLock())
					{
						System.out.println("INFO: Lock JVM successfully.");
						break;
					}
				} 
				catch (InterruptedException e1)
				{
					// DO Nothing
				}
				endTime = LocalDateTime.now();
			}
		}
		
		this.m_isLockedByMe = true;
	}

	public void readTestResult()
	{
		String finalStatus = "";
		try 
		{
			File variableFile = new File(this.m_resultFolderPath + File.separator + "variables.properties");

			if(!variableFile.exists())
			{
				if(this.m_status != TestCaseStatus.Aborted)
				{
					this.m_status = TestCaseStatus.Failed;
				}

				return;
			}

			Properties variables = new Properties();
			FileInputStream fis = new FileInputStream(this.m_resultFolderPath + File.separator + "variables.properties");
			variables.load(fis);

			this.m_firstErrorMsg = variables.getProperty("First_Error_Msg");
			finalStatus = variables.getProperty("Status");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(this.m_status == TestCaseStatus.Aborted)
			{
				// Do Nothing
			}
			else if("Passed".equalsIgnoreCase(finalStatus))
			{
				this.m_status = TestCaseStatus.Passed;
			}
			else if("Failed".equalsIgnoreCase(finalStatus))
			{
				this.m_status = TestCaseStatus.Failed;
			}
			else if("Warning".equalsIgnoreCase(finalStatus))
			{
				this.m_status = TestCaseStatus.Warning;
			}
			else
			{
				this.m_status = TestCaseStatus.NA;
			}
		}
	}


	class StreamDrainer implements Runnable {   
		private InputStream ins;   
		public StreamDrainer(InputStream ins) 
		{   
			this.ins = ins;   
		}   
		public void run(){   
			try 
			{   
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins));   
				String line = null;   
				int lineNum = 0;
				while ((line = reader.readLine()) != null) 
				{   
					System.out.println(line);

					lineNum++;
					if(lineNum == 100)
					{
						lineNum = 0;
						System.out.flush();
					}
				}   
			} catch (Exception e) 
			{   
				e.printStackTrace();
			}

			System.out.flush();
		}
	}

	public static void main(String[] args) {
		//		File file = new File("O:\\EBS ITT\\Ray\\Scripts\\R12.2.5\\ofoe\\ofoe_ap_1\\MASTERDRIVER\\results");
		//		long latest = 0L;
		//		int index = 0;
		//		if(file.isDirectory())
		//		{
		//			File[] fileList = file.listFiles();
		//			for(int i = 0; i < fileList.length; i ++)
		//			{
		//				System.out.println(fileList[i].getAbsolutePath() + ":: Last Modified-" + fileList[i].lastModified());
		//				if(fileList[i].lastModified() >= latest)
		//				{
		//					latest = fileList[i].lastModified();
		//					index = i;
		//				}
		//			}
		//
		//			System.out.println("Last Session: " + fileList[index].getAbsolutePath());
		//
		//			String logfilePath;
		//			try 
		//			{
		//				logfilePath = fileList[index].getCanonicalPath() + "\\BasicReport.htm";
		//				System.out.println("File Name: " + logfilePath);
		//				//				readOverallResult(logfilePath);
		//				Runtime.getRuntime().exec("C:/Program Files/Internet Explorer/iexplore.exe " + logfilePath);
		//			}
		//			catch (IOException e)
		//			{
		//				e.printStackTrace();
		//			}
		//		}
	}
}
