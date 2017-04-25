package com.auto.tools.ui.components.listener;

import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.auto.tools.context.Context;
import com.auto.tools.ui.components.TestCaseDataTable;
import com.auto.tools.ui.components.util.TableDataTemplateTransferer;

public class ToolWindowListener extends WindowAdapter{

	public ToolWindowListener()
	{
	}

	@Override
	public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		int result = JOptionPane.showConfirmDialog(null, 
				"Are you sure to close this window?\n"
						+ "Click YES to exit.\n"
						+ "", "Really Closing?", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
		else if(result == JOptionPane.NO_OPTION)
		{
//			System.exit(0);
		}

	}
}
