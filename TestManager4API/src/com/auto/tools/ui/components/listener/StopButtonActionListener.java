package com.auto.tools.ui.components.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.auto.tools.TestManager;
import com.auto.tools.models.TestCase;
import com.auto.tools.ui.components.DeviceControlPane;
import com.auto.tools.ui.components.GlobalVarDataTable;
import com.auto.tools.ui.components.TestCaseDataTable;

public class StopButtonActionListener implements ActionListener {

	private JComboBox action;
	private DeviceControlPane deviceControlPane;
	private GlobalVarDataTable globalvarTable;
	private TestCaseDataTable testCaseDataTable;

	public StopButtonActionListener(DeviceControlPane deviceControlPane) {
		this.action = deviceControlPane.getStopOption();
		this.deviceControlPane = deviceControlPane;
		this.globalvarTable = (GlobalVarDataTable) deviceControlPane.getGlobalDataTable();
		this.testCaseDataTable = (TestCaseDataTable) deviceControlPane.getTestCaseDataTable();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<TestCase> list = this.testCaseDataTable.getFilteredTestCases(this.globalvarTable.getGlobalVars(), this.action.getSelectedItem().toString());

		if(list.size() == 0)
		{
			JOptionPane.showMessageDialog(null, "No Script is selected.");

			return;
		}
		
		TestManager tm = TestManager.getInstance();
//		if(tm.isPendingQueueEmpty())
//		{
//			return;
//		}
		
		if(action.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
			new Thread(()->
			{
				tm.stopBatchExecution();	
			}).start();
		}
		else
		{
			new Thread(()->
			{
				tm.stopTestExecution(list);
			}).start();
		}
	}

}
