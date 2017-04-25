package com.auto.tools.ui.components.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.auto.tools.context.Configurations;
import com.auto.tools.models.TestCase;
import com.auto.tools.ui.components.DataTableModel;
import com.auto.tools.ui.components.FileTree;
import com.auto.tools.ui.components.TestCaseDataTable;

public class AddTestCaseAction  extends AbstractAction{

	TestCaseDataTable table;
	FileTree tree;
	
	public AddTestCaseAction(JTable testCasesTbl, JTree directory)
	{
		super("Add TestCases");
		this.table = (TestCaseDataTable)testCasesTbl;
		this.tree = (FileTree)directory;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		String jTreeVarSelectedPath = "";
		TreePath[] paths = tree.getSelectionPaths();
		
		if(paths == null)
		{
			JOptionPane.showMessageDialog(null, "No script is selected.");
			return;
		}
		
		TestCase testCase;
		for(TreePath path : paths)
		{
			jTreeVarSelectedPath = "com.tinklabs.handy.testcases.";
			Object[] fp = path.getPath();
			for (int i=0; i<fp.length; i++) {
				
				jTreeVarSelectedPath += fp[i];
				if (i+1 <fp.length ) {
					jTreeVarSelectedPath += ".";
				}
			}

			testCase = new TestCase(jTreeVarSelectedPath);
			((DataTableModel)this.table.getModel()).addRow(testCase);
		}
	}

}
