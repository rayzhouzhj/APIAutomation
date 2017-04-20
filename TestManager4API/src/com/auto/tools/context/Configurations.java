package com.auto.tools.context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configurations 
{
	private static Configurations m_instance = null;
	private Logger m_logger = null;
	private Properties m_configs;
	private boolean m_status;
	
	private Configurations()
	{
		m_logger = Logger.getLogger("Configurations");
		
		File directory  = new File(".");
		String configFilePath;
		String logFilePath;
		try 
		{
			configFilePath = directory.getCanonicalPath() + "/configuration.properties";
			File configFile = new File(configFilePath);
			
			if(!configFile.exists())
			{
				m_logger.fatal("Cannot find file configuration.properties.");
				m_status = false;
				
				return;
			}
			
			m_configs = new Properties();
			FileInputStream fis = new FileInputStream(configFilePath);
			m_configs.load(fis);
			
			// Set log file path
			logFilePath = directory.getCanonicalPath() + "\\log\\" + System.getProperty("user.name") + "_log.html";
			m_configs.put("LOG_FILE", logFilePath);
			
			m_status = true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static Configurations getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new Configurations();
		}

		return m_instance;
	}
	
	public String getAttribute(String attribute)
	{
		if(m_configs.containsKey(attribute))
		{
			return m_configs.getProperty(attribute);
		}
		else
		{
			return null;
		}
	}
	
	public void setAttribute(String attributeName, String attributeValue)
	{
		m_configs.setProperty(attributeName, attributeValue);
	}
	
	public boolean getInitilizationStatus()
	{
		return m_status;
	}
}
