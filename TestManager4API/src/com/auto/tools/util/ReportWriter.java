package com.auto.tools.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Layout;

import com.auto.tools.models.TestCase;
import com.auto.tools.models.TestCaseStatus;
import com.auto.utils.CommandPrompt;

public class ReportWriter {

	private String defaultPath;
	
	public ReportWriter()
	{
		defaultPath = "." + File.separator 
				+ "report" + File.separator 
				+ "TestReport-" + DateTimeFormatter.ofPattern("dd-MMM-yyyy-HH-mm").format(LocalDateTime.now()) + ".htm";
	}
	
	public void setFilePath(String file)
	{
		defaultPath = file;
	}
	
	public String getFilePath()
	{
		return defaultPath;
	}
	
	public void showReport(List<TestCase> list)
	{

		FileWriter fw;
		try {
			fw = new FileWriter(defaultPath);

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(getHeader());
			for(int i = 0; i < list.size(); i++)
			{
				bw.write(format(list.get(i)));
			}

			bw.flush();
			fw.close();
			bw.close();
			
			File report = new File(defaultPath);
			// For windows
			if(System.getProperty("os.name").contains("Windows"))
			{
				CommandPrompt.executeCommand("C:/Program Files/Internet Explorer/iexplore.exe \"" + report.getCanonicalPath() + "\"");
			}
			// For Mac
			else
			{
				CommandPrompt.executeCommand("open \"" + report.getCanonicalPath() + "\"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void saveReport(boolean containFailedResult, List<TestCase> list)
	{

		FileWriter fw;
		try {
			if(containFailedResult)
			{
				this.defaultPath = this.defaultPath.replace("TestReport-", "Failed-TestReport-");
			}
			
			fw = new FileWriter(this.defaultPath);

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(getHeader());
			for(int i = 0; i < list.size(); i++)
			{
				bw.write(format(list.get(i)));
			}

			bw.flush();
			fw.close();
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String format(TestCase testCase) {     
		StringBuffer sbuf = new StringBuffer();     
		sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);     

		Path tcResultPath = Paths.get(testCase.getResultFilePath());
		
		sbuf.append("<td><a href=\"" + tcResultPath.toAbsolutePath().toString() + "\">");     
		sbuf.append(testCase.getScriptName());     
		sbuf.append("</a></td>" + Layout.LINE_SEP); 

		if (testCase.getStatus() == TestCaseStatus.Passed || testCase.getStatus() == TestCaseStatus.Failed || testCase.getStatus() == TestCaseStatus.Aborted) 
		{
			// Start Time
			sbuf.append("<td>");
			sbuf.append(testCase.getStartTimeAsString());     
			sbuf.append("</td>" + Layout.LINE_SEP);  
			// End Time
			sbuf.append("<td>");     
			sbuf.append(testCase.getEndTimeAsString());     
			sbuf.append("</td>" + Layout.LINE_SEP); 
		}
		else
		{
			// Start Time
			sbuf.append("<td>");
			sbuf.append("</td>" + Layout.LINE_SEP); 

			// End Time
			sbuf.append("<td>"); 
			sbuf.append("</td>" + Layout.LINE_SEP); 
		}

		// RT Status
		sbuf.append("<td>");
		if (testCase.getStatus() == TestCaseStatus.Passed) 
		{
			sbuf.append("<font color=\"#339933\"><strong>");
			sbuf.append("Passed");
			sbuf.append("</strong></font>");
		}
		else if(testCase.getStatus() == TestCaseStatus.Failed || testCase.getStatus() == TestCaseStatus.Error) 
		{
			sbuf.append("<font color=\"#993300\"><strong>");
			sbuf.append("Failed");
			sbuf.append("</strong></font>");
		} 
		else if(testCase.getStatus() == TestCaseStatus.Aborted) 
		{
			sbuf.append("<font color=\"#EEC900\"><strong>");
			sbuf.append("Aborted");
			sbuf.append("</strong></font>");
		} 
		else 
		{
			sbuf.append("<strong>");
			sbuf.append(testCase.getStatus().toString());
			sbuf.append("</strong>");
		}   
		sbuf.append("</td>" + Layout.LINE_SEP);     

		// Log message
		sbuf.append("<td>");
		String errorMsg = testCase.getFirstErrorMessage();
		if(errorMsg.length() > 70)
		{
			String text = "";
			String[] msg = errorMsg.split(" ");
			for(int i = 0; i < msg.length; i++)
			{
				text = text + msg[i] + " ";
				if(text.length() >= 70)
				{
					sbuf.append(text);
					sbuf.append("<br>");
					text = "";
				}
			}

			// Add the last error message
			sbuf.append(text);
		}
		else
		{
			sbuf.append(errorMsg);
		}
		sbuf.append("</td>" + Layout.LINE_SEP);     
		sbuf.append("</tr>" + Layout.LINE_SEP);     

		return sbuf.toString();     
	}

	/**   
	 * Returns appropriate HTML headers.   
	 */    
	public String getHeader() {     
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"  + Layout.LINE_SEP);
		sbuf.append("<html>" + Layout.LINE_SEP);
		sbuf.append("<head>" + Layout.LINE_SEP);
		sbuf.append("<title>Test Report</title>" + Layout.LINE_SEP);
		sbuf.append("<style type=\"text/css\">"  + Layout.LINE_SEP);
		sbuf.append("<!--"  + Layout.LINE_SEP);
		sbuf.append("body, table {font-family: arial,sans-serif; font-size: x-small;}" + Layout.LINE_SEP);
		sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
		sbuf.append("-->" + Layout.LINE_SEP);
		sbuf.append("</style>" + Layout.LINE_SEP);
		sbuf.append("</head>" + Layout.LINE_SEP);
		sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);
		sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
		sbuf.append("<br>" + Layout.LINE_SEP);
		sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">" + Layout.LINE_SEP);
		sbuf.append("<tr>" + Layout.LINE_SEP);    

		sbuf.append("<th>RT Name</th>" + Layout.LINE_SEP);     
		sbuf.append("<th>Start Time</th>" + Layout.LINE_SEP);     
		sbuf.append("<th>End Time</th>" + Layout.LINE_SEP); 
		sbuf.append("<th>Status</th>" + Layout.LINE_SEP); 
		sbuf.append("<th>Comment</th>" + Layout.LINE_SEP);     
		sbuf.append("</tr>" + Layout.LINE_SEP);     
		sbuf.append("<br></br>" + Layout.LINE_SEP);     
		return sbuf.toString();     
	}
}
