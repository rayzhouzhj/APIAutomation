package com.auto.framework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Layout;

import com.auto.framework.test.TestContext;

public class LogSetting {

	public static void createLogNavigator()
	{
		FileWriter fw;
		try 
		{
			Path logFolderPath = Paths.get(TestContext.getInstance().getVariable("LogFolder"));
			Path reportPath = logFolderPath.getParent().resolve("Report.html");
			int sessionNum = Integer.parseInt(TestContext.getInstance().getVariable("LogSession"));

			StringBuffer sbuf = new StringBuffer();
			sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"  + Layout.LINE_SEP);
			sbuf.append("<html>" + Layout.LINE_SEP);
			sbuf.append("<head>" + Layout.LINE_SEP);
			sbuf.append("<title>Report</title>" + Layout.LINE_SEP);
			sbuf.append("</head>" + Layout.LINE_SEP);
			sbuf.append("<FRAMESET cols='10%, 90%'>" + Layout.LINE_SEP);
			sbuf.append("<FRAME src='Navigator.html' name='navigator'>" + Layout.LINE_SEP);
			sbuf.append("<FRAME src='session" + sessionNum + File.separator + "result.html' name='main'>" + Layout.LINE_SEP);
			sbuf.append("</FRAMESET></HTML>" + Layout.LINE_SEP);

			fw = new FileWriter(reportPath.toFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sbuf.toString());
			bw.flush();
			bw.close();
			fw.close();

			Path navigatorPath = logFolderPath.getParent().resolve("Navigator.html");
			fw = new FileWriter(navigatorPath.toFile(), true);
			bw = new BufferedWriter(fw);
			bw.write("<li><a href='session" + sessionNum + File.separator + "result.html' target='main'>Session" + sessionNum + "</a><br></li>\n");
			bw.flush();
			bw.close();
			fw.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateLogStatus()
	{
		String filePath = TestContext.getInstance().getVariable("LogFile");
		String status = TestContext.getInstance().getVariable("Status");
		String displayedColor = "";
		if("Passed".equals(status))
		{
			displayedColor = "#006400";
		}
		else if("Failed".equals(status))
		{
			displayedColor = "#993300";
		}
		else
		{
			displayedColor = "#f4d742";
		}
			
		if(filePath != null)
		{
			try 
			{
				File logFile = new File(filePath);
				BufferedReader reader = new BufferedReader(new FileReader(logFile));
				List<String> lines = new ArrayList<>();
				String line;
				while((line = reader.readLine()) != null)
				{
					if(line.contains("<B>Execution Status:"))
					{
						line = "<B>Execution Status: <font color=\"" + displayedColor + "\">" + status + "</font></B><br>";
					}
					
					lines.add(line);
				}
				
				reader.close();
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8"));
				for(String output : lines)
				{
					bw.write(output);
					bw.newLine();
				}
				
				bw.close();
				
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
}
