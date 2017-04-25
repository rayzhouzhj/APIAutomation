package com.auto.tools.ui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.auto.tools.models.JUnitScript;
import com.auto.tools.models.TestCase;
import com.auto.tools.models.TestCaseStatus;
import com.auto.tools.ui.components.editor.ReportButtonEditor;
import com.auto.tools.ui.components.editor.TestDataButtonEditor;
import com.auto.tools.ui.components.handler.TestCaseDataTableTransferHandler;
import com.auto.tools.ui.components.renderer.ReportButtonRender;
import com.auto.tools.ui.components.renderer.StatusColumnCellRenderer;
import com.auto.tools.ui.components.renderer.TestDataButtonRender;
import com.auto.tools.ui.components.util.TableDataTemplateTransferer;
import com.auto.tools.ui.constant.ExecutionOption;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class TestCaseDataTable extends DataTable {

	private static final long serialVersionUID = 6608543108079420803L;
	private DeviceControlPane deviceControlPane;

	public TestCaseDataTable(DeviceControlPane deviceControlPane) {
		super(
				new DataTableModel(
						new String[]{"",
								TestCaseDataTableColumns.SCRIPT_NAME.NAME, 
								TestCaseDataTableColumns.SCRIPT_PATH.NAME,
								TestCaseDataTableColumns.PRIORITY.NAME, 
								TestCaseDataTableColumns.TIMEOUT.NAME,
								TestCaseDataTableColumns.STARTTIME.NAME,
								TestCaseDataTableColumns.ENDTIME.NAME,
								TestCaseDataTableColumns.STATUS.NAME,
								TestCaseDataTableColumns.COMMENT.NAME,
								TestCaseDataTableColumns.TEST_DATA.NAME,
								TestCaseDataTableColumns.REPORT.NAME},
						new Object[][]{{false, "", "", "0", "00:30", "", "", "NA", "", "", ""}}
						)
				);

		this.deviceControlPane = deviceControlPane;

		this.setDropMode(DropMode.ON);
		this.setTransferHandler(new TestCaseDataTableTransferHandler(this.deviceControlPane, this));

		DataTableModel dtm = (DataTableModel)this.getModel();

		dtm.setNonEditableColumn(TestCaseDataTableColumns.SCRIPT_PATH.INDEX);
		dtm.setNonEditableColumn(TestCaseDataTableColumns.STARTTIME.INDEX);
		dtm.setNonEditableColumn(TestCaseDataTableColumns.ENDTIME.INDEX);
		dtm.setNonEditableColumn(TestCaseDataTableColumns.STATUS.INDEX);
		dtm.setNonEditableColumn(TestCaseDataTableColumns.COMMENT.INDEX);

		this.getColumnModel().getColumn(TestCaseDataTableColumns.EXECUTION_FLAG.INDEX).setHeaderRenderer(new CheckBoxHeader(new MyItemListener(this)));

		this.getColumnModel().getColumn(TestCaseDataTableColumns.TEST_DATA.INDEX).setCellEditor(new TestDataButtonEditor());
		this.getColumnModel().getColumn(TestCaseDataTableColumns.TEST_DATA.INDEX).setCellRenderer(new TestDataButtonRender());

		this.getColumnModel().getColumn(TestCaseDataTableColumns.REPORT.INDEX).setCellEditor(new ReportButtonEditor());
		this.getColumnModel().getColumn(TestCaseDataTableColumns.REPORT.INDEX).setCellRenderer(new ReportButtonRender());

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		this.getColumnModel().getColumn(TestCaseDataTableColumns.PRIORITY.INDEX).setCellRenderer(centerRenderer);
		this.getColumnModel().getColumn(TestCaseDataTableColumns.TIMEOUT.INDEX).setCellRenderer(centerRenderer);

		TableColumn timeColumn = this.getColumnModel().getColumn(TestCaseDataTableColumns.TIMEOUT.INDEX);
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.addItem("00:05");
		comboBox.addItem("00:10");
		comboBox.addItem("00:15");
		comboBox.addItem("00:20");
		comboBox.addItem("00:25");
		comboBox.addItem("00:30");
		comboBox.addItem("00:35");
		comboBox.addItem("00:40");
		comboBox.addItem("00:45");
		comboBox.addItem("00:50");
		comboBox.addItem("00:55");
		comboBox.addItem("01:00");
		comboBox.addItem("01:05");
		comboBox.addItem("01:10");
		comboBox.addItem("01:15");
		comboBox.addItem("01:20");
		comboBox.addItem("01:25");
		comboBox.addItem("01:30");
		comboBox.addItem("01:35");
		comboBox.addItem("01:40");
		comboBox.addItem("01:45");
		comboBox.addItem("01:50");
		comboBox.addItem("01:55");
		comboBox.addItem("02:00");
		timeColumn.setCellEditor(new DefaultCellEditor(comboBox));


		TableColumn statusColumn = this.getColumnModel().getColumn(TestCaseDataTableColumns.STATUS.INDEX);
		comboBox = new JComboBox<>();
		comboBox.addItem(TestCaseStatus.NA.toString());
		comboBox.addItem(TestCaseStatus.Timeout.toString());
		comboBox.addItem(TestCaseStatus.Aborted.toString());
		comboBox.addItem(TestCaseStatus.Failed.toString());
		comboBox.addItem(TestCaseStatus.Pending.toString());
		comboBox.addItem(TestCaseStatus.Running.toString());
		comboBox.addItem(TestCaseStatus.Passed.toString());
		comboBox.addItem(TestCaseStatus.Cancelled.toString());
		comboBox.addItem(TestCaseStatus.Warning.toString());
		statusColumn.setCellEditor(new DefaultCellEditor(comboBox));
		statusColumn.setCellRenderer(new StatusColumnCellRenderer());

		if(this.deviceControlPane.getDefaultTemplate() != null && !this.deviceControlPane.getDefaultTemplate().isEmpty())
		{
			TableDataTemplateTransferer.TransferTemplateDataToTable(this, this.deviceControlPane.getDefaultTemplate());
		}
	}

	public void fillTable(TreeMap<Integer, JUnitScript> scriptMap)
	{
		JUnitScript script = null;

		if(scriptMap.keySet().size() == 0)
		{
			((DataTableModel)this.getModel()).addEmptyRow();
		}

		TestCase tempTC;
		for(int id : scriptMap.keySet())
		{
			script = scriptMap.get(id);
			script.turnoffRef();

			tempTC = new TestCase(script);
			
			((DataTableModel)this.getModel()).addRow(tempTC);
		}
	}

	public List<TestCase> getFilteredTestCases(HashMap<String, String> map, String filer)
	{
		List<TestCase> list = new ArrayList<TestCase>();
		TableModel model = this.getModel();

		String testName;
		String tcPath;
		String parameters;
		String deviceID;
		int priority;
		String timeout;

		for(int i = 0; i < this.getModel().getRowCount(); i++)
		{
			testName = model.getValueAt(i, TestCaseDataTableColumns.SCRIPT_NAME.INDEX).toString();
			parameters = model.getValueAt(i, TestCaseDataTableColumns.TEST_DATA.INDEX).toString();
			tcPath = model.getValueAt(i, TestCaseDataTableColumns.SCRIPT_PATH.INDEX).toString();
			priority = "".equals(model.getValueAt(i, TestCaseDataTableColumns.PRIORITY.INDEX).toString())? 0 : Integer.parseInt(model.getValueAt(i, TestCaseDataTableColumns.PRIORITY.INDEX).toString());
			timeout = model.getValueAt(i, TestCaseDataTableColumns.TIMEOUT.INDEX).toString();

			if((filer.equals(ExecutionOption.SELECTED) && model.getValueAt(i, TestCaseDataTableColumns.EXECUTION_FLAG.INDEX).equals(true))
					|| (filer.equals(ExecutionOption.ABORTED) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.ABORTED))
					|| (filer.equals(ExecutionOption.CANCELLED) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.CANCELLED))
					|| (filer.equals(ExecutionOption.NA) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.NA))
					|| (filer.equals(ExecutionOption.PASSED) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.PASSED))
					|| (filer.equals(ExecutionOption.FAILED) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.FAILED))
					|| (filer.equals(ExecutionOption.TIMEOUT) && model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.TIMEOUT))
					|| (filer.equals(ExecutionOption.NON_PASSED) && !model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).equals(ExecutionOption.PASSED))
					)
			{
				if(testName == null || testName.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Empty Test Name is found in row: " + i);
					break;
				}

				list.add(new TestCase(
						i,	// Row id
						testName,
						tcPath, 
						parameters,
						priority, 
						timeout));
			}
		}

		return list;
	}

	public List<TestCase> getAllTestCases()
	{
		List<TestCase> list = new ArrayList<TestCase>();
		TableModel model = this.getModel();

		if(model.getRowCount() == 1 && model.getValueAt(0, TestCaseDataTableColumns.SCRIPT_NAME.INDEX).toString().isEmpty())
		{
			return list;
		}

		String testName;
		String tcPath;
		String parameters;
		int priority;
		String timeout;
		String status;
		String comment;
		String report;
		String startTime;
		String endTime;
		String deviceID;

		for(int i = 0; i < model.getRowCount(); i++)
		{
			testName = model.getValueAt(i, TestCaseDataTableColumns.SCRIPT_NAME.INDEX).toString();
			tcPath = model.getValueAt(i, TestCaseDataTableColumns.SCRIPT_PATH.INDEX).toString();
			parameters = model.getValueAt(i, TestCaseDataTableColumns.TEST_DATA.INDEX).toString();
			priority = "".equals(model.getValueAt(i, TestCaseDataTableColumns.PRIORITY.INDEX).toString())? 0 : Integer.parseInt(model.getValueAt(i, TestCaseDataTableColumns.PRIORITY.INDEX).toString());
			timeout = model.getValueAt(i, TestCaseDataTableColumns.TIMEOUT.INDEX).toString();
			status = model.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).toString();
			startTime = model.getValueAt(i, TestCaseDataTableColumns.STARTTIME.INDEX).toString();
			endTime = model.getValueAt(i, TestCaseDataTableColumns.ENDTIME.INDEX).toString();
			comment = model.getValueAt(i, TestCaseDataTableColumns.COMMENT.INDEX).toString();
			report = model.getValueAt(i, TestCaseDataTableColumns.REPORT.INDEX).toString();

			list.add(new TestCase(
					i,	// Row id
					testName,
					tcPath, 
					parameters,
					priority, 
					timeout, 
					startTime,
					endTime,
					status,
					comment,
					report));
		}

		return list;
	}
}

