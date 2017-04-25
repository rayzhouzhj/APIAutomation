package com.auto.tools.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import com.auto.tools.ui.ScheduleControlFrame;
import com.auto.tools.ui.TestManagerFrame;
import com.auto.tools.ui.components.action.ExitAction;
import com.auto.tools.ui.components.action.RunScriptsOnAllDeviceAction;
import com.auto.tools.ui.components.action.StopScriptsOnAllDeviceAction;

public class ToolMenuBar extends JMenuBar {

	private JMenu fileMenu = new JMenu();
	private JMenuItem closeAppMenuItem = new JMenuItem();

	private JMenu toolMenu = new JMenu();
	private JMenuItem schedulerMenuItem = new JMenuItem();

	private JMenu actionMenu = new JMenu();
	private JMenuItem runAllMenuItem = new JMenuItem();
	private JMenuItem stopAllMenuItem = new JMenuItem();

	private JMenu helpMenu = new JMenu();

	private JTabbedPane tabbedControlPane;
	private TestManagerFrame frame;

	public ToolMenuBar(TestManagerFrame frame)
	{
		super();
		this.frame = frame;
		this.tabbedControlPane = frame.getTabbedPane();

		initComponent();
	}

	private void initComponent()
	{
		//=============File Menu=====================
		fileMenu.setText("File");
		this.add(fileMenu);

		closeAppMenuItem.setAction(new ExitAction());
		fileMenu.add(closeAppMenuItem);


		//=============Tools Menu=====================
		toolMenu.setText("Tools");
		schedulerMenuItem.setText("Scheduler");
		schedulerMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ScheduleControlFrame scheduler = ToolMenuBar.this.frame.getScheduler();
				scheduler.setVisible(true);
			}
		});
		toolMenu.add(schedulerMenuItem);
		this.add(toolMenu);

		//=============Help Menu=====================
		actionMenu.setText("Actions");
		runAllMenuItem.setAction(new RunScriptsOnAllDeviceAction("Run Scripts on All Devices", ToolMenuBar.this.frame.getTabbedPane()));
		stopAllMenuItem.setAction(new StopScriptsOnAllDeviceAction("Stop Scripts on All Devices", ToolMenuBar.this.frame.getTabbedPane()));
		actionMenu.add(runAllMenuItem);
		actionMenu.add(stopAllMenuItem);
		this.add(actionMenu);

		//=============Help Menu=====================
		helpMenu.setText("Help");
		this.add(helpMenu);

	}
}
