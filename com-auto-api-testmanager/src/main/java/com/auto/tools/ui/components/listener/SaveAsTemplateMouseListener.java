package com.auto.tools.ui.components.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.auto.tools.ui.components.TemplateFileChooser;
import com.auto.tools.ui.components.TemplateList;
import com.auto.tools.ui.components.TestCaseDataTable;

public class SaveAsTemplateMouseListener implements MouseListener {

	private TestCaseDataTable table;
	private TemplateList list;
	
	public SaveAsTemplateMouseListener(JTable table, JList list)
	{
		this.table = (TestCaseDataTable)table;
		this.list = (TemplateList)list;
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {

		int result = JOptionPane.showConfirmDialog(null, "Do you want to clear test status when saving template?", "Clear Status", JOptionPane.YES_NO_OPTION);
		boolean flag = (result == JOptionPane.YES_OPTION)? true : false;
		
		new TemplateFileChooser(this.table, this.list, flag).showChooser();
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				
//				String defaultPath = "templates";
//				String file = "";
//				
//				try {
//					defaultPath = new File(defaultPath).getCanonicalPath();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				JFileChooser fileChooser = new JFileChooser();
//				
//				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//				fileChooser.setFileFilter(new FileNameExtensionFilter("Template XML File", "xml"));
//				fileChooser.setCurrentDirectory(new File(defaultPath));
//				fileChooser.setDialogTitle("Please Enter the file name");
//				
//				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//					file = fileChooser.getSelectedFile().getAbsolutePath();
//					
//					if(!file.toLowerCase().endsWith(".template.xml"))
//					{
//						if(!file.toLowerCase().endsWith(".xml"))
//						{
//							file = file + ".template.xml";
//						}
//						else
//						{
//							file = file.replace(".xml", ".template.xml");
//						}
//					}
//					
//					TableDataTemplateTransferer.TransferTableDataToTemplate(table, file);
//					
//					if(new File(file).exists())
//					{
//						JOptionPane.showMessageDialog(null, "Template file is saved successfully! \nFile Name:  " + file);
//						list.loadTemplates();
//					}
//					else
//					{
//						JOptionPane.showMessageDialog(null, "Error when saving Template file:\n " + file);
//					}
//				}
//			}
//		});
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
