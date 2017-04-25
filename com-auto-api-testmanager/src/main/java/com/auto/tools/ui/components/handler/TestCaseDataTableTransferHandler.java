package com.auto.tools.ui.components.handler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.auto.tools.models.TestCase;
import com.auto.tools.ui.components.DataTableModel;
import com.auto.tools.ui.components.DeviceControlPane;
import com.auto.tools.ui.components.TestCaseDataTable;

public class TestCaseDataTableTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -859729261862388543L;
	
	private TestCaseDataTable table;
	private DeviceControlPane deviceControlPane;

	public TestCaseDataTableTransferHandler(DeviceControlPane deviceControlPane, TestCaseDataTable table)
	{
		this.table = table;
		this.deviceControlPane = deviceControlPane;
	}

	public boolean canImport(TransferSupport support) {
		
		// for the demo, we'll only support drops (not clipboard paste)
		if (!support.isDrop()) {
			return false;
		}

		// we only import Strings
		if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return false;
		}

		return true;
	}

	public boolean importData(TransferSupport support) {
		// if we can't handle the import, say so
		if (!canImport(support)) {
			return false;
		}

		// fetch the drop location
		JTable.DropLocation dl = (JTable.DropLocation) support
				.getDropLocation();

		int row = dl.getRow();

		// fetch the data and bail if this fails
		String data;
		try {
			data = (String) support.getTransferable().getTransferData(
					DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		String[] rowData = data.split(",");
		
		TestCase testCase;
		for(String path : rowData)
		{
			testCase = new TestCase(path);
			((DataTableModel)this.table.getModel()).addRow(testCase);
		}

		return true;
	}
}
