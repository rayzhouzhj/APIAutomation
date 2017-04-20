package com.auto.tools.ui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.auto.tools.ui.components.util.TableDataTemplateTransferer;

public class TemplateList extends JList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GlobalVarDataTable varTable;
	private TestCaseDataTable tcTable;
	
	public void loadTemplates()
	{
		DefaultListModel<String> listModel = new DefaultListModel<>();
		try
		{
			Files.list(Paths.get("./templates"))
					.map(f -> f.getFileName().toString())
					.filter(s -> s.endsWith("template.xml") && !s.endsWith("default.template.xml"))
					.forEach(e -> listModel.addElement(e));
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		this.setModel(listModel);
	}
	
	public TemplateList(JTable globalVarsTbl, JTable testCasesTbl)
	{
		varTable = (GlobalVarDataTable) globalVarsTbl;
		tcTable = (TestCaseDataTable) testCasesTbl;

		this.loadTemplates();
		
		this.setComponentPopupMenu(new TemplateListPopMenu(this));
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				
				if(SwingUtilities.isLeftMouseButton(paramMouseEvent)
						&& paramMouseEvent.getClickCount() == 2
						&& TemplateList.this.getSelectedValue() != null)
				{
					TableDataTemplateTransferer.TransferTemplateDataToTable(tcTable, TemplateList.this.getSelectedValue());
				}
			}

			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub

			}
		});
	}
	
//	public void loadDataToDataTable(String file)
//	{
//		try {
//			ScriptTemplate template = XMLUtils.readXML("templates/" + file);
//			tcTable.fillTable(template.getScripts());
//
//		} catch (ParserConfigurationException | SAXException
//				| IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
