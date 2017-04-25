package com.auto.tools.ui.components.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.auto.tools.models.TestCaseStatus;
import com.auto.tools.ui.components.TestCaseDataTable;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class StatusColumnCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {

		Component comp =  null; 

		comp = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus,
				row, column);

		int modelIdx = table.convertRowIndexToModel(row);
		String currentSts = table.getModel().getValueAt(modelIdx, TestCaseDataTableColumns.STATUS.INDEX).toString();
		if(currentSts.equalsIgnoreCase(TestCaseStatus.Passed.toString()))
		{
			comp.setBackground(Color.GREEN);
		}
		else if(currentSts.equalsIgnoreCase(TestCaseStatus.Failed.toString())
				|| currentSts.equalsIgnoreCase(TestCaseStatus.Error.toString())
				|| currentSts.equalsIgnoreCase(TestCaseStatus.Aborted.toString()))
		{
			comp.setBackground(Color.RED);
		}
		else if(currentSts.equalsIgnoreCase(TestCaseStatus.Cancelled.toString())
				|| currentSts.equalsIgnoreCase(TestCaseStatus.Timeout.toString()))
		{
			comp.setBackground(Color.WHITE);
		}
		else if(currentSts.equalsIgnoreCase(TestCaseStatus.Running.toString())
				|| currentSts.equalsIgnoreCase(TestCaseStatus.Warning.toString()))
		{
			comp.setBackground(Color.YELLOW);
		}
		else if(currentSts.equalsIgnoreCase(TestCaseStatus.Pending.toString()))
		{
			comp.setBackground(Color.CYAN);
		}
		else
		{
			comp.setBackground(Color.LIGHT_GRAY);

		}

		this.setHorizontalAlignment(SwingConstants.CENTER);

		return comp;
	}
}
