package com.auto.tools.context;

import java.util.HashMap;

public class Context {
	private static Context m_instance = null;
	private HashMap<String, String> m_context;
	
	private Context()
	{
		m_context =new HashMap<>();
	}
	
	public static Context getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new Context();
		}

		return m_instance;
	}
	
	public String getAttribute(String attribute)
	{
		if(m_context.containsKey(attribute))
		{
			return m_context.get(attribute);
		}
		else
		{
			return null;
		}
	}
	
	public void setAttribute(String attributeName, String attributeValue)
	{
		m_context.put(attributeName, attributeValue);
	}
}
