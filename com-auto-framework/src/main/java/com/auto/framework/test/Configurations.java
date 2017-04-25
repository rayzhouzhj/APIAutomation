package com.auto.framework.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.auto.framework.logs.AutoLogger;

public class Configurations 
{
	private static Configurations m_instance = null;
	private AutoLogger m_logger = null;
	private Properties m_configs;
	private boolean m_status;
	
	private Configurations()
	{
		m_logger = new AutoLogger("Configurations");
		
		File directory  = new File(".");
		String configFilePath;
		try 
		{
			configFilePath = directory.getCanonicalPath() + File.separator + "configuration.properties";
			File configFile = new File(configFilePath);
			
			if(!configFile.exists())
			{
				m_logger.fatal("Initialization", "Cannot find file configuration.properties.");
				m_status = false;
				
				return;
			}
			
			m_configs = new Properties();
			FileInputStream fis = new FileInputStream(configFilePath);
			m_configs.load(fis);
			
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
	
	public boolean getInitilizationStatus()
	{
		return m_status;
	}
}
