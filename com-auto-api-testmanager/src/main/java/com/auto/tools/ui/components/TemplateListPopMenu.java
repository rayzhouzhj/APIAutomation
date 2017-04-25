package com.auto.tools.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class TemplateListPopMenu extends JPopupMenu {

	private JMenuItem refresh = new JMenuItem("Refresh");
	private JMenuItem remove = new JMenuItem("Remove Templates");

	public TemplateListPopMenu(TemplateList template)
	{
		super();

		this.add(refresh);
		this.add(remove);

		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				template.loadTemplates();
			}
		});

		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(template.getSelectedValue() != null)
				{
					DefaultListModel<String> model = (DefaultListModel<String>)template.getModel();

					int[] indeces = template.getSelectedIndices();
					for(int i = indeces.length - 1; i >= 0; i--)
					{
						if(new File("templates" + File.separator + model.getElementAt(indeces[i])).delete())
						{
							model.removeElementAt(indeces[i]);
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No Item is selected.");
				}
			}
		});
	}
}
