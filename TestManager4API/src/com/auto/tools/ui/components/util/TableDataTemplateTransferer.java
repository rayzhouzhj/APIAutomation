package com.auto.tools.ui.components.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.auto.tools.models.JUnitScript;
import com.auto.tools.models.ScriptTemplate;
import com.auto.tools.ui.components.TestCaseDataTable;
import com.auto.tools.util.XMLUtils;

public class TableDataTemplateTransferer {

	public static void TransferTableDataToTemplate(TestCaseDataTable table, String file, boolean clearStatusFlag)
	{
		List<JUnitScript> scriptList = table.getAllTestCases().stream()
				.map(tc -> new JUnitScript(tc)).collect(Collectors.toList());

		if(clearStatusFlag)
		{
			scriptList.forEach( script -> 
			{
				script.cleanupStatus();
			});
		}
		
		ScriptTemplate template = new ScriptTemplate();
		template.addScripts(scriptList);

		XMLUtils.writeXML(template, file);
	}
	
	public static void TransferTemplateDataToTable(TestCaseDataTable table, String file)
	{
		try 
		{
			ScriptTemplate template = XMLUtils.readXML("templates/" + file);
			table.fillTable(template.getScripts());

		} catch (ParserConfigurationException | SAXException
				| IOException e) 
		{
			e.printStackTrace();
			
			table.fillTable(new ScriptTemplate().getScripts());
		}
	}
}
