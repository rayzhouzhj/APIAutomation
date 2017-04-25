package com.auto.framework.logs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

import com.auto.framework.test.TestContext;

public class NewLogForEachRunFileAppender extends FileAppender {

	public NewLogForEachRunFileAppender() {
		this.setEncoding("UTF-8");
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append, boolean bufferedIO, int bufferSize)
					throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
		
		this.setEncoding("UTF-8");
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append) throws IOException {
		super(layout, filename, append);
		
		this.setEncoding("UTF-8");
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename)
			throws IOException {
		super(layout, filename);
		
		this.setEncoding("UTF-8");
	}

	public void activateOptions() {
		if (fileName != null) {
			try {
				fileName = getNewLogFileName();
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (Exception e) {
				errorHandler.error("Error while activating log options", e,
						ErrorCode.FILE_OPEN_FAILURE);
			}
		}
	}

	private String getNewLogFileName() {
		if (fileName != null) {
			String newFileName = "";

			Path logFolder = Paths.get(fileName);
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

			TestContext.getInstance().setVariable("LogSession", "" + nextSession);
			newFileName = fileName +  File.separator +  "session" + nextSession + File.separator + "result.html";

			return newFileName;
		}
		return null;
	}
}
