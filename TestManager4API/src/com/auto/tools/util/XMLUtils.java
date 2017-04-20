package com.auto.tools.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.auto.tools.models.JUnitScript;
import com.auto.tools.models.ScriptTemplate;
import com.auto.tools.models.TestCaseStatus;

public class XMLUtils {
	
	/**
	 * Read the XML file that with the predifined structure and script data
	 * @param xmlFilePath - XML file path
	 * @return template - The XML file with predefined structure and data for each script
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static ScriptTemplate readXML(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException
	{
		ScriptTemplate template = new ScriptTemplate();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new File(xmlFilePath));
		
		Element scriptlistNode = (Element)document.getElementsByTagName("script-list").item(0);
		NodeList scriptlist = scriptlistNode.getElementsByTagName("script");
		
		JUnitScript newScript;
		Node id;
		Node name;
		Node classname;
		Node runtime;
		Node parameters;
		Node status;
		String statusString;
		Node comment;
		String commentString;
		Node reportFile;
		String reportFileString;
		Node startTime;
		String startTimeString;
		Node endTime;
		String endTimeString;
		
		for(int i = 0 ; i < scriptlist.getLength(); i++)
		{
			id = ((Element)scriptlist.item(i)).getElementsByTagName("id").item(0);
			name = ((Element)scriptlist.item(i)).getElementsByTagName("name").item(0);
			classname = ((Element)scriptlist.item(i)).getElementsByTagName("classname").item(0);
			runtime = ((Element)scriptlist.item(i)).getElementsByTagName("runtime").item(0);
			parameters = ((Element)scriptlist.item(i)).getElementsByTagName("parameters").item(0);
			
			startTime = ((Element)scriptlist.item(i)).getElementsByTagName("starttime").item(0);
			if(startTime == null)
			{
				startTimeString = "";
			}
			else
			{
				startTimeString = startTime.getTextContent();
			}
			
			endTime = ((Element)scriptlist.item(i)).getElementsByTagName("endtime").item(0);
			if(endTime == null)
			{
				endTimeString = "";
			}
			else
			{
				endTimeString = endTime.getTextContent();
			}
			status = ((Element)scriptlist.item(i)).getElementsByTagName("status").item(0);
			if(status == null)
			{
				statusString = TestCaseStatus.NA.toString();
			}
			else
			{
				statusString = status.getTextContent();
			}
			comment = ((Element)scriptlist.item(i)).getElementsByTagName("comment").item(0);
			if(comment == null)
			{
				commentString = "";
			}
			else
			{
				commentString = comment.getTextContent();
			}
			reportFile = ((Element)scriptlist.item(i)).getElementsByTagName("report").item(0);
			if(reportFile == null)
			{
				reportFileString = "";
			}
			else
			{
				reportFileString = reportFile.getTextContent();
			}
			
			System.out.println("Read Script: " 
					+ name.getTextContent() + "\t" 
					+ classname.getTextContent() + "\t" 
					+ runtime.getTextContent() + "\t" 
					+ parameters.getTextContent() + "\t"
					+ startTimeString + "\t"
					+ endTimeString + "\t"
					+ statusString + "\t"
					+ commentString + "\t"
					+ reportFileString + "\t"
					);
			
			newScript = new JUnitScript(
					id.getTextContent(),
					name.getTextContent(), 
					classname.getTextContent(), 
					parameters.getTextContent(), 
					runtime.getTextContent(), 
					startTimeString,
					endTimeString,
					statusString,
					commentString,
					reportFileString);
			
			template.addScript(newScript);
		}
		
		return template;
	}
	
	/**
	 * Write the script data into XML file in predifined structure
	 * @param template - The XML file with predefined structure and data for each script 
	 * @param filePath
	 * @throws AbstractScriptException 
	 */
	public static void writeXML(ScriptTemplate template, String filePath) {
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("template");
			doc.appendChild(rootElement);

			// script elements
			Element scriptlist = doc.createElement("script-list");
			rootElement.appendChild(scriptlist);

			// script element
			Element script;
			Element scriptid;
			Element scriptname;
			Element className;
			Element parameters;
			Element runtime;
			Element status;
			Element comment;
			Element report;
			Element startTime;
			Element endTime;
			JUnitScript scriptInfo;
			
			TreeMap<Integer, JUnitScript> scripts = template.getScripts();
			Iterator<Integer> scriptIterator = scripts.keySet().iterator();
			while(scriptIterator.hasNext())
			{
				scriptInfo = scripts.get(scriptIterator.next());
				scriptInfo.turnOnRef();
				
				script = doc.createElement("script");
				scriptlist.appendChild(script);
				
				// id elements
				scriptid = doc.createElement("id");
				scriptid.appendChild(doc.createTextNode(scriptInfo.getID()));
				script.appendChild(scriptid);
				
				// name elements
				scriptname = doc.createElement("name");
				scriptname.appendChild(doc.createTextNode(scriptInfo.getName()));
				script.appendChild(scriptname);

				// Class Name elements
				className = doc.createElement("classname");
				className.appendChild(doc.createTextNode(scriptInfo.getScriptPath()));
				script.appendChild(className);
				
				// run-time elements
				runtime = doc.createElement("runtime");
				runtime.appendChild(doc.createTextNode(scriptInfo.getRuntime()));
				script.appendChild(runtime);

				// Parameters elements
				parameters = doc.createElement("parameters");
				parameters.appendChild(doc.createTextNode(scriptInfo.getParameters()));
				script.appendChild(parameters);
				
				// status elements
				status = doc.createElement("status");
				status.appendChild(doc.createTextNode(scriptInfo.getStatus().toString()));
				script.appendChild(status);
				
				// comment elements
				comment = doc.createElement("comment");
				comment.appendChild(doc.createTextNode(scriptInfo.getComment().toString()));
				script.appendChild(comment);
				
				// report elements
				report = doc.createElement("report");
				report.appendChild(doc.createTextNode(scriptInfo.getReportFile().toString()));
				script.appendChild(report);
				
				// start time elements
				startTime = doc.createElement("starttime");
				startTime.appendChild(doc.createTextNode(scriptInfo.getStartTimeAsString()));
				script.appendChild(startTime);
				
				// end time elements
				endTime = doc.createElement("endtime");
				endTime.appendChild(doc.createTextNode(scriptInfo.getEndTimeAsString()));
				script.appendChild(endTime);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));

			transformer.transform(source, result);

			System.out.println("File saved! - Path: " + filePath);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}

