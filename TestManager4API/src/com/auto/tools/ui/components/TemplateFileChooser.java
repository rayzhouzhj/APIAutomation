package com.auto.tools.ui.components;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.auto.tools.ui.components.util.TableDataTemplateTransferer;

public class TemplateFileChooser {

	private TestCaseDataTable table;
	private TemplateList list;
	private boolean clearResultFlag;
	
	public TemplateFileChooser(JTable table)
	{
		this.table = (TestCaseDataTable)table;
		clearResultFlag = false;
	}
	
	public TemplateFileChooser(JTable table, JList list, boolean flag)
	{
		this.table = (TestCaseDataTable)table;
		this.list = (TemplateList)list;
		this.clearResultFlag = flag;
	}
	
	public void showChooser()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				String defaultPath = "templates";
				String file = "";
				
				try {
					defaultPath = new File(defaultPath).getCanonicalPath();
				} catch (IOException e) {
					e.printStackTrace();
				}
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Template XML File", "xml"));
				fileChooser.setCurrentDirectory(new File(defaultPath));
				fileChooser.setDialogTitle("Please Enter the file name");
				
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile().getAbsolutePath();
					
					if(!file.toLowerCase().endsWith(".template.xml"))
					{
						if(!file.toLowerCase().endsWith(".xml"))
						{
							file = file + ".template.xml";
						}
						else
						{
							file = file.replace(".xml", ".template.xml");
						}
					}
					
					TableDataTemplateTransferer.TransferTableDataToTemplate(table, file, TemplateFileChooser.this.clearResultFlag);
					
					if(new File(file).exists())
					{
						JOptionPane.showMessageDialog(null, "Template file is saved successfully! \nFile Name:  " + file);
						if(list != null)
						{
							list.loadTemplates();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Error when saving Template file:\n " + file);
					}
				}
			}
		});
	}
}
