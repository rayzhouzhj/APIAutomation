package com.auto.tools.ui.components.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.auto.tools.ui.components.TestCaseDataTable;
import com.auto.tools.util.ReportWriter;

public class ExportButtonMouseListener implements MouseListener {

	public TestCaseDataTable table;
	public ExportButtonMouseListener(JTable table)
	{
		this.table = (TestCaseDataTable)table;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				ReportWriter rw = new ReportWriter();
				
				String defaultPath = rw.getFilePath();
				String file = "";
				
				try {
					defaultPath = new File(defaultPath).getCanonicalPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Report File", "htm"));
				fileChooser.setSelectedFile(new File(defaultPath));
				fileChooser.setDialogTitle("Please Enter the file name");
				
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile().getAbsolutePath();
					
					rw.showReport(ExportButtonMouseListener.this.table.getAllTestCases());
				}
			}
		});
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
