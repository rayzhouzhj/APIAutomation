package com.auto.tools.ui.components.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.auto.tools.ui.components.DataTable;
import com.auto.tools.ui.components.DataTableModel;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class DuplicateDataTableRowAction extends AbstractAction{

	private DataTable dataTable;

	public DuplicateDataTableRowAction(DataTable table)
	{
		this.dataTable = table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int[] rows = dataTable.getSelectedRows();
		int selectedRow = -1;
		
		if(rows.length == 0)
		{
			JOptionPane.showMessageDialog(null, "No testcase is selected for duplication!");
			return;
		}
		
		if(dataTable.getRowCount() == 0)
		{
			return;
		}

		int rowNum = 0;
		Object tempNum = null;

		try
		{
			tempNum = JOptionPane.showInputDialog(null, 
					"Rows of duplication?", 
							"Duplicate Row",
							JOptionPane.QUESTION_MESSAGE
							, null
							, null
							, 1);

			rowNum = Integer.parseInt(tempNum.toString());
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, tempNum + " is not a valid number!");
			return;
		}

		if(rowNum <= 0)
		{
			return;
		}

		DataTableModel tableModel = (DataTableModel)dataTable.getModel();

		for(int i=0; i<rows.length; i++)
		{
			selectedRow = dataTable.convertRowIndexToModel(rows[i] - i);
			System.out.println("Duplicate selected row: " + selectedRow);

			for(int j = 0; j < rowNum; j++)
			{
				Object[] rowData = tableModel.getRow(selectedRow).clone();
				
				// Erase Testing Data
				rowData[TestCaseDataTableColumns.STARTTIME.INDEX] = "";
				rowData[TestCaseDataTableColumns.ENDTIME.INDEX] = "";
				rowData[TestCaseDataTableColumns.STATUS.INDEX] = "NA";
				rowData[TestCaseDataTableColumns.COMMENT.INDEX] = "";
				rowData[TestCaseDataTableColumns.REPORT.INDEX] = "";
				
				tableModel.addRow(rowData);
			}
		}

		dataTable.clearSelection();
	}
}
