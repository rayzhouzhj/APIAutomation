package com.auto.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurations 
{
	private Properties m_configs;
	
	public Configurations(String configFile)
	{
		
		try 
		{
			System.out.println("Loading config from: " + configFile);
			m_configs = new Properties();
			FileInputStream fis = new FileInputStream(configFile);
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
