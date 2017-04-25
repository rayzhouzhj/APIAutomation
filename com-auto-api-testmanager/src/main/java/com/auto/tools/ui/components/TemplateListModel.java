package com.auto.tools.ui.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractListModel;

public class TemplateListModel extends AbstractListModel<String> {

	private static final long serialVersionUID = 2775096617793375111L;
	
	private List<String> list = null;
	
	public TemplateListModel()
	{
		super();
		
		loadTemplates();
	}
	
	public void loadTemplates()
	{
		try
		{
			list = Files.list(Paths.get("./templates"))
					.map(f -> f.getFileName().toString())
					.filter(s -> s.endsWith("template.xml"))
					.collect(Collectors.toList());
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getElementAt(int paramInt) {
		// TODO Auto-generated method stub
		if(getSize() > 0 || paramInt >= 0)
			return list.get(paramInt);
		else
			return null;
	}
	
	public static void main(String...arg) throws IOException
	{
		System.out.println(Paths.get("./templates").toRealPath());	
		Files.list(Paths.get("./templates")).forEach(c -> System.out.println(c.getFileName()));
	}

}
