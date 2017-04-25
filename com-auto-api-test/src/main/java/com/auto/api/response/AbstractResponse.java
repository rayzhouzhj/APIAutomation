package com.auto.api.response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.auto.framework.logs.AutoLogger;
import com.auto.framework.test.TestContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractResponse {

	private static AutoLogger log = new AutoLogger("Response");

	public AbstractResponse()
	{
	}

	public void print()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String response = gson.toJson(this, this.getClass());
		System.out.println("Response:\n "+ response);

		File logFile = new File(TestContext.getInstance().getVariable("LogFolder") 
				+ File.separator + this.getClass() +".json");
		
		log.info("<a href='" + logFile.getAbsolutePath() + "'>View Response</a>");
		
		// Write it to files
		FileWriter fw;
		try {
			fw = new FileWriter(logFile);

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(response);

			bw.flush();
			fw.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
