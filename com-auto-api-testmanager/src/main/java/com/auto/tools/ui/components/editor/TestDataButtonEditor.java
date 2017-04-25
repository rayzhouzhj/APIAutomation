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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.auto.tools.ui.NewViewTestDataFrame;

public class TestDataButtonEditor extends DefaultCellEditor {
	/** 
	 * serialVersionUID 
	 */  
	private static final long serialVersionUID = -6546334664166791132L;  

	private JPanel panel;  

	private JButton button;  

	private String paramString = "";

	private JTable table = null;
	private TableModel rowData = null;
	private int currentRow = 0;
	private int currentCol = 0;

	public TestDataButtonEditor()  
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
			img = ImageIO.read(new File("icons" + File.separator + "data.png"));
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
				TestDataButtonEditor.this.fireEditingCanceled();  

				NewViewTestDataFrame dataFrame = new NewViewTestDataFrame(TestDataButtonEditor.this);
				dataFrame.setLocationRelativeTo(null);
				dataFrame.setVisible(true);
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
		return paramString;
	}

	public void setCellEditorValue(Object input) {
		this.rowData.setValueAt(
				input, 
				this.table.convertRowIndexToModel(this.currentRow), 
				this.table.convertColumnIndexToModel(this.currentCol)
				);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
	{  
		this.table = table;
		this.rowData = table.getModel();
		this.currentRow = row;
		this.currentCol = column;

		this.paramString = value.toString();

		return this.panel;  
	}  
}
