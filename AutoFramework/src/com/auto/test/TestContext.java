package com.auto.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class TestContext {
	private Properties variables;
	private static TestContext instance;

	private TestContext()
	{
		variables = new Properties();
	}

	public synchronized static TestContext getInstance()
	{
		if(instance == null)
		{
			instance = new TestContext();
		}

		return instance;
	}

	public void setVariable(String variableName, String value)
	{
		variables.setProperty(variableName, value);
		if(variables.getProperty("LogFolder") != null)
		{
			try
			{
				File propertiesFile = new File(variables.getProperty("LogFolder") + File.separator + "variables.properties");
				OutputStream out = new FileOutputStream(propertiesFile);
				variables.store(out, "Variables From Current Test Execution");
			}
			catch (Exception e ) {
				e.printStackTrace();
			}
		}
	}

	public String getVariable(String variableName)
	{
		return variables.getProperty(variableName);
	}

	public static void main(String...strings)
	{
		TestContext.getInstance().setVariable("Test", "Value");
		TestContext.getInstance().setVariable("LogFolder", ".");
		System.out.println(TestContext.getInstance().getVariable("Empty"));
	}
}
