package com.auto.tools.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ViewReportTableButton extends DefaultCellEditor implements TableCellEditor, TableCellRenderer  
{
	private String reportFilePath = null;
	private JButton button = new JButton("...");

	public ViewReportTableButton()
	{
		super(new JTextField());
		setClickCountToStart(1);
		
		button.setBackground(Color.GRAY);
	}

	@Override
	public void addCellEditorListener(CellEditorListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return reportFilePath;
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		reportFilePath = value.toString();


		return button;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean arg2, int arg3, int arg4) {
		reportFilePath = value.toString();

		fireEditingStopped();
		
		if(!reportFilePath.isEmpty())
		{
			new Thread(new Runnable(){

				@Override
				public void run() {
					File report = new File(reportFilePath);

					try {
						// For windows
						if(System.getProperty("os.name").contains("Windows"))
						{
						Runtime.getRuntime().exec("C:/Program Files/Internet Explorer/iexplore.exe \"" + report.getCanonicalPath() + "\"");
						}
						// For Mac
						else
						{
							Runtime.getRuntime().exec("open \"" + report.getCanonicalPath() + "\"");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}}).start();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No report is available for current script!");
		}


		return button;
	}


}
