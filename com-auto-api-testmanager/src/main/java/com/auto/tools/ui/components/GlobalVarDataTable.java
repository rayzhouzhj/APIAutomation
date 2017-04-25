package com.auto.tools.ui.components;

import java.util.HashMap;

import javax.swing.table.TableModel;

public class GlobalVarDataTable extends DataTable {

	public GlobalVarDataTable(TableModel tableModel) {
		super(tableModel);
	}
	
	public GlobalVarDataTable() {
		super(new DataTableModel(
				new String[]{""},
				new Object[][]{{"", "", ""}}));
		
	}
	
	public HashMap<String, String> getGlobalVars()
	{
		HashMap<String, String> map = new HashMap<>();
		// TODO add logic to retrieve global variables
		
		return map;
	}
}
