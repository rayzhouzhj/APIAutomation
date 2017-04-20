package com.auto.tools.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.pattern.IntegerPatternConverter;

public class JUnitScript
{
	private String m_id;
	private String m_name;
	private String m_runtime;
	private String m_scriptPath;
	private LocalDateTime m_startTime;
	private LocalDateTime m_endTime;
	private String m_comment;
	private String m_report;
	private String m_parameters;
	private TestCaseStatus m_status = TestCaseStatus.NA;
	private boolean m_referenceFlag = true;

	private HashMap<String, String> m_globalVars;

	public JUnitScript(TestCase testCase)
	{
		this.setID("" + testCase.getScriptId());
		this.setName(testCase.getScriptName());
		this.setScriptPath(testCase.getTcPath());
		this.setParameters(testCase.getTestData());
		this.setRuntime(testCase.getTimeout());
		this.setScriptPath(testCase.getTcPath());
		this.setStatus(testCase.getStatus());
		this.setComment(testCase.getFirstErrorMessage());
		this.setReportFile(testCase.getResultFilePath());
		this.setStartTime(testCase.getStartTime());
		this.setEndTime(testCase.getEndTime());
	}

	//	public RTScript(String name, String description, String runtime, String filepath, TestCaseStatus status)
	//	{
	//		this.setName(name);
	//		this.setDescription(description);
	//		this.setRuntime(runtime);
	//		this.setFilePath(filepath);
	//		this.setRelease("{{Release}}");
	//		this.setBuild("{{Build}}");
	//		this.m_status = status;
	//	}

	public JUnitScript(String name, String description, String runtime, String filepath)
	{
		this.setName(name);
		this.setRuntime(runtime);
		this.setScriptPath(filepath);
	}

	public JUnitScript(String name, String description, String runtime)
	{
		this.setName(name);
		this.setRuntime(runtime);
		this.setScriptPath("{{Folder Path}}");
	}

	public JUnitScript(String name, String description, String runtime, String release, String build)
	{
		this.setName(name);
		this.setRuntime(runtime);
		this.setScriptPath("{{Folder Path}}");
	}

	public JUnitScript(String name, String description, String runtime, String filepath, String release, String buildNum)
	{
		this.setName(name);
		this.setRuntime(runtime);
		this.setScriptPath(filepath);
	}

	public JUnitScript(String name, 
			String description, 
			String runtime, 
			String filepath, 
			String zipfile, 
			String release, 
			String buildNum)
	{
		this.setName(name);
		this.setRuntime(runtime);
		this.setScriptPath(filepath);
	}

	public JUnitScript(
			String id,
			String name, 
			String classname, 
			String parameters, 
			String runtime, 
			String startTime,
			String endTime,
			String status,
			String comment,
			String report)
	{
		this.setID("" + id);
		this.setName(name);
		this.setScriptPath(classname);
		this.setRuntime(runtime);
		this.setParameters(parameters);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setStatus(status);
		this.setComment(comment);
		this.setReportFile(report);

	}

	public void setID(String id)
	{
		this.m_id = id;
	}
	
	public void increaseRunTime(int increment)
	{
		String[] timeArray = m_runtime.split(":");
		int hour = Integer.parseInt(timeArray[0].trim());
		int minute = Integer.parseInt(timeArray[1].trim());
		if(increment%5 > 0)
		{
			increment = increment + (5 - increment%5);
		}

		minute = minute + increment;
		while(minute >= 60)
		{
			hour = hour + 1;
			minute = minute - 60;
		}
		// if increment < 0
		if(increment < 0 && minute < 0 && hour > 0)
		{
			hour = hour - 1;
			minute = minute + 60;
		}
		// Total run time less than 0, keep the minimum time - 00:05
		else if(increment < 0 && minute < 0 && hour < 0)
		{
			hour = 0;
			minute = 5;
		}

		String hourStr = hour < 10? "0" + hour : "" + hour;
		String minuteStr = minute < 10? "0" + minute : "" + minute;

		m_runtime = hourStr + ":" + minuteStr;
	}

	private String resolveReference(String input)
	{
		String output = input;
		if(m_referenceFlag == false)
		{
			String refStr = "";
			String refVar = "";
			Pattern pattern = Pattern.compile("\\{\\{([^\\}]+)\\}\\}");
			Matcher matcher = pattern.matcher(output);
			if(matcher.find())
			{
				refStr = matcher.group(0);
				refVar = matcher.group(1);
				//				System.out.println("Matched: " + matcher.group(0));
				//				System.out.println("Matched String: " + matcher.group(1));

				if(this.m_globalVars.containsKey(refVar))
				{
					output = output.replace(refStr, m_globalVars.get(refVar));
				}
				else
				{
					System.out.println("No matching variable is found!");
				}
			}
		}

		return output;
	}

	public HashMap<String, String> getGlobalVars() {
		return m_globalVars;
	}

	public void setGlobalVars(HashMap<String, String> globalVars) {
		this.m_globalVars = globalVars;
	}

	public String getGlobalVar(String key) {
		return m_globalVars.get(key);
	}

	public void turnOnRef()
	{
		m_referenceFlag = true;
	}

	public void turnoffRef()
	{
		m_referenceFlag = false;
	}

	public boolean equals(Object script)
	{
		if(script instanceof JUnitScript)
		{
			return m_name.equals(((JUnitScript)script).getName());
		}
		else
		{
			return false;
		}
	}

	public void cleanupStatus()
	{
		this.setComment("");
		this.setEndTime("");
		this.setStartTime("");
		this.setReportFile("");
		this.setStatus(TestCaseStatus.NA);
	}
	
	public String getID()
	{
		return this.m_id;
	}
	
	public int getIDAsInt()
	{
		return Integer.parseInt(this.m_id);
	}
	
	public String getName() {
		return resolveReference(m_name);
	}

	public void setName(String name) {
		this.m_name = name;
	}

	public void setStatus(TestCaseStatus status)
	{
		this.m_status = status;
	}

	public void setStartTime(LocalDateTime time)
	{
		this.m_startTime = time;
	}
	
	
	public void setEndTime(LocalDateTime time)
	{
		this.m_endTime = time;
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
	
	public LocalDateTime getStartTime()
	{
		return this.m_startTime;
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
	
	public LocalDateTime getEndTime()
	{
		return this.m_endTime;
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

	public String getParameters()
	{
		return this.m_parameters;
	}
	
	public void setParameters(String parameters)
	{
		this.m_parameters = parameters;
	}
	
	public void setStatus(String status)
	{
		this.m_status = TestCaseStatus.valueOf(status);
	}

	public void setComment(String comment)
	{
		this.m_comment = comment;
	}

	public String getComment()
	{
		return this.m_comment;
	}

	public void setReportFile(String report)
	{
		this.m_report = report;
	}

	public String getReportFile()
	{
		return this.m_report;
	}

	public TestCaseStatus getStatus()
	{
		return this.m_status;
	}

	public String getRuntime() {
		return resolveReference(m_runtime);
	}

	public void setRuntime(String runtime) {
		this.m_runtime = runtime;
	}

	public String getScriptPath() {
		return resolveReference(m_scriptPath);
	}

	public void setScriptPath(String path) {
		this.m_scriptPath = path;	
	}
}