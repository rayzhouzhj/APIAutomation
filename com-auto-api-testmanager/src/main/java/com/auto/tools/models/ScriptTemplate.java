package com.auto.tools.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class ScriptTemplate
{
	private TreeMap<Integer, JUnitScript> m_scriptMap;
	
	public ScriptTemplate()
	{
		m_scriptMap = new TreeMap<Integer, JUnitScript>();
	}

	public boolean existScript(JUnitScript script)
	{
		return m_scriptMap.containsKey(script.getName());
	}
	
	public void removeScripts(HashMap<String, JUnitScript> compScripts)
	{
		Iterator<String> iterator = compScripts.keySet().iterator();
		String key = "";
		while(iterator.hasNext())
		{
			key = iterator.next();
			if(m_scriptMap.containsKey(key))
			{
				m_scriptMap.remove(key);
			}
		}
	}
	
	public void increaseScriptsRunTime(int increment)
	{
		Iterator<JUnitScript> iterator = m_scriptMap.values().iterator();
		JUnitScript script;
		while(iterator.hasNext())
		{
			script = iterator.next();
			script.increaseRunTime(increment);
		}
	}

	public TreeMap<Integer, JUnitScript> getScripts() {
		return m_scriptMap;
	}

	public void setScripts(TreeMap<Integer, JUnitScript> scriptmap) {
		this.m_scriptMap = scriptmap;
	}
	
	public boolean containsScript(String scriptName) {
		return this.m_scriptMap.containsKey(scriptName);
	}
	
	public void addScript(JUnitScript script) {
		this.m_scriptMap.put(script.getIDAsInt(), script);
	}
	
	public void addScripts(List<JUnitScript> scriptList) {
		scriptList.forEach(script ->
		{
			this.m_scriptMap.put(script.getIDAsInt(), script);
		});
	}
	
	public void removeScript(String scriptName) {
		this.m_scriptMap.remove(scriptName);
	}
}
