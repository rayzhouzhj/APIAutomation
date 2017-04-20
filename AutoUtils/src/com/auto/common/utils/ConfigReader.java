package com.auto.common.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigReader 
{
	private Properties m_configs;
	
	public ConfigReader(String configFilePath)
	{
		try 
		{
			File configFile = new File(configFilePath);
			
			if(!configFile.exists())
			{
				System.out.println("Cannot find config file: " + configFilePath);
				return;
			}
			
			m_configs = new Properties();
			FileInputStream fis = new FileInputStream(configFilePath);
			m_configs.load(fis);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
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
}
