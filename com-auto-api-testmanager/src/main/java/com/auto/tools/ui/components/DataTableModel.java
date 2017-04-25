package com.auto.tools.ui.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.auto.tools.models.TestCase;
import com.auto.tools.models.TestCaseStatus;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class DataTableModel extends AbstractTableModel {

	private String[] columnNames = null;
	private Object[][] data = null;
	private Set<Integer> nonEditableCol = new HashSet<>();

	public DataTableModel(String[] columns, Object[][] data)
	{
		this.columnNames = columns;
		this.data = data;
	}

	public void setNonEditableColumn(int colIndex)
	{
		nonEditableCol.add(colIndex);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if(nonEditableCol.contains(column))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public int getColumnCount() {
		if(columnNames == null)
		{
			return 0;
		}
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(data == null)
		{
			return 0;
		}
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}


	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	public Object[] getRow(int row)
	{
		return data[row];
	}
	
	public void AddColumn(String columnName, String defaultVal)
	{
		String[] newColumnNames = new String[columnNames.length + 1];
		for(int i = 0; i < columnNames.length; i++)
		{
			newColumnNames[i] = columnNames[i];
		}

		newColumnNames[newColumnNames.length - 1] = columnName;

		columnNames = newColumnNames;
		columnNames[0] = "Col 1";
		columnNames[1] = "Col 2";

		data = new Object[1][columnNames.length];
		data[0][0] = "Cell 1";
		data[0][1] = false;

		fireTableStructureChanged();
	}
	
	public void addEmptyRow()
	{
		if(data.length >= 1 && !data[0][TestCaseDataTableColumns.SCRIPT_NAME.INDEX].toString().isEmpty())
		{
			Object[][] newData = new Object[data.length + 1][TestCaseDataTableColumns.COLUMN_COUNT];

			for(int i = 0; i < data.length; i++)
			{
				newData[i] = data[i];
			}
			
			newData[data.length][TestCaseDataTableColumns.EXECUTION_FLAG.INDEX] = false;
			newData[data.length][TestCaseDataTableColumns.PRIORITY.INDEX] = 0;
			newData[data.length][TestCaseDataTableColumns.TIMEOUT.INDEX] = "00:30";
			newData[data.length][TestCaseDataTableColumns.STATUS.INDEX] = "NA";
			
			for(int i = 0; i < newData[data.length].length; i++)
			{
				if(newData[data.length][i] == null) newData[data.length][i] = "";
			}
			
			data = newData;
			
			fireTableDataChanged();
		}
	}
	
	public void removeRow(int index)
	{
		// If the current row is the only row shown, clear data and leave blank row
		if(index == 0 && data.length == 1)
		{
			data[0][TestCaseDataTableColumns.EXECUTION_FLAG.INDEX] = false;
			data[0][TestCaseDataTableColumns.PRIORITY.INDEX] = 0;
			data[0][TestCaseDataTableColumns.SCRIPT_NAME.INDEX] = "";
			data[0][TestCaseDataTableColumns.SCRIPT_PATH.INDEX] = "";
			data[0][TestCaseDataTableColumns.TIMEOUT.INDEX] = "00:30";
			data[0][TestCaseDataTableColumns.STARTTIME.INDEX] = "";
			data[0][TestCaseDataTableColumns.ENDTIME.INDEX] = "";
			data[0][TestCaseDataTableColumns.STATUS.INDEX] = "NA";
			data[0][TestCaseDataTableColumns.COMMENT.INDEX] = "";
			data[0][TestCaseDataTableColumns.REPORT.INDEX] = "";
			
			for(int i = 0; i < data[0].length; i++)
			{
				if(data[0][i] == null) data[0][i] = "";
			}
		}
		else
		{
			Object[][] updatedData = new Object[data.length - 1][data[0].length];
			int updatedArrayIndex = 0;
			for(int i = 0; i < data.length; i++)
			{
				if(i == index) continue;

				for(int j = 0; j < data[i].length; j++)
				{
					System.out.println(i + ":" + j + ": " + data[i][j]);
					updatedData[updatedArrayIndex][j] = data[i][j];
				}

				updatedArrayIndex++;
			}

			data = updatedData;
		}

		fireTableDataChanged();
	}

	public void addRow(Object[] newRow)
	{
		// If the first row is empty row
		if(data.length == 1 && data[0][1].toString().isEmpty())
		{
			data[0] = newRow;
		}
		else
		{
			Object[][] newData = new Object[data.length + 1][];

			for(int i = 0; i < data.length; i++)
			{
				newData[i] = data[i];
			}

			newData[data.length] = newRow;

			data = newData;
		}

		fireTableDataChanged();
	}

	public void addRow(TestCase testCase)
	{
		Object[] newRow = new Object[]{
				true,								// Flag
				testCase.getScriptName(),			// Script Name
				testCase.getTcPath(),				// Script Path
				testCase.getScriptPriority(),		// Priority
				testCase.getTimeout(),				// Timeout
				testCase.getStartTimeAsString(),	// StartTime
				testCase.getEndTimeAsString(),		// EndTime
				testCase.getStatus().toString(),	// Status
				testCase.getFirstErrorMessage(),	// Comment
				testCase.getTestData(),				// Test Data
				testCase.getResultFilePath()		// Report
		};
		
		this.addRow(newRow);
	}
	
	public void updateColumnValue(int col, String columnValue)
	{
		for(int i = 0; i < data.length; i++)
		{
			data[i][col] = columnValue;
		}
		
		fireTableDataChanged();
	}
	
	public void updateRow(TestCase testCase)
	{
		int row = testCase.getScriptId();
		
		data[row][TestCaseDataTableColumns.STARTTIME.INDEX] = testCase.getStartTimeAsString() == null? "" : testCase.getStartTimeAsString().toString();
		data[row][TestCaseDataTableColumns.ENDTIME.INDEX] = testCase.getEndTimeAsString() == null? "" : testCase.getEndTimeAsString().toString();
		data[row][TestCaseDataTableColumns.STATUS.INDEX] = testCase.getStatus().toString();
		data[row][TestCaseDataTableColumns.COMMENT.INDEX] = testCase.getFirstErrorMessage();
		data[row][TestCaseDataTableColumns.REPORT.INDEX] = testCase.getResultFilePath();
		
		fireTableDataChanged();
	}
}
