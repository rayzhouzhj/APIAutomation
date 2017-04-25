package com.auto.tools.ui.components.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import com.auto.tools.ui.components.DeviceControlPane;

public class RunScriptsOnAllDeviceAction extends AbstractAction{

	private static final long serialVersionUID = -4843477089798303334L;
	private JTabbedPane tabbedPane;

	public RunScriptsOnAllDeviceAction(String title, JTabbedPane deviceControlPane)
	{
		super(title);
		this.tabbedPane = deviceControlPane;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		int tabCount = this.tabbedPane.getTabCount();

		for(int index = 0; index < tabCount; index++)
		{
			JButton runBtn = ((DeviceControlPane)tabbedPane.getComponentAt(index)).getRunButton();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run()
				{
					runBtn.doClick();
				}
			});
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
