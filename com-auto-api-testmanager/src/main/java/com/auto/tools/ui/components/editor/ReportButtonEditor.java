package com.auto.tools.ui.components.editor;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.auto.framework.utils.CommandPrompt;

public class ReportButtonEditor extends DefaultCellEditor {
	/** 
	 * serialVersionUID 
	 */  
	private static final long serialVersionUID = -6546334664166791132L;  

	private JPanel panel;  

	private JButton button;  

	private String filePath = "";

	public ReportButtonEditor()  
	{  
		super(new JTextField());  

		this.setClickCountToStart(1);  

		this.initButton();  

		this.initPanel();  

		this.panel.add(this.button);  
	}

	private void initButton()  
	{  
		this.button = new JButton();  

		Image img;
		Icon icon;
		try {
			img = ImageIO.read(new File("icons" + File.separator + "report.png"));
			Image resizedImage;
			resizedImage = img.getScaledInstance(20, 15, Image.SCALE_DEFAULT);
			icon = new ImageIcon(resizedImage);

			this.button.setIcon(icon);

			// Mark the node as file(true) -> not expendable
		} catch (IOException ex) {
		}

		this.button.setBounds(0, 0, 20, 15);  

		// Only Action Listener works
		this.button.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  
				ReportButtonEditor.this.fireEditingCanceled();  

				if(!filePath.isEmpty())
				{
					File report = new File(filePath);

					if(report.exists())
					{
						try 
						{
							// For windows
							if(System.getProperty("os.name").contains("Windows"))
							{
								CommandPrompt.executeCommand("call \"C:/Program Files/Internet Explorer/iexplore.exe\" \"" + report.getCanonicalPath() + "\"");
							}
							// For Mac
							else
							{
								CommandPrompt.executeCommand("open \"" + report.getCanonicalPath() + "\"");
							}
						} catch (IOException | InterruptedException ioe) {
							ioe.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "No report is available for current script!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No report is available for current script!");
				}
			}  
		});  

	}

	private void initPanel()  
	{  
		this.panel = new JPanel();  

		this.panel.setLayout(null);  
	}  

	@Override
	public Object getCellEditorValue() {
		return filePath;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
	{  
		this.filePath = value.toString();

		return this.panel;  
	}  
}
