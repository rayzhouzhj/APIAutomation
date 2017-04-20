package com.auto.tools.ui.components.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellEditor;

import com.auto.tools.context.Configurations;
import com.auto.tools.ui.components.ImageFileView;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class FileChooserCellEditor extends DefaultCellEditor implements TableCellEditor {

	/** Number of clicks to start editing */
	private static final int CLICK_COUNT_TO_START = 2;
	/** Editor component */
	private JButton button;
	/** File chooser */
	private JFileChooser fileChooser;
	/** Selected file */
	private String file = "";

	/**
	 * Constructor.
	 */
	public FileChooserCellEditor() {
		super(new JTextField());
		setClickCountToStart(CLICK_COUNT_TO_START);

		// Using a JButton as the editor component
		button = new JButton();
		button.setBackground(Color.white);
		button.setFont(button.getFont().deriveFont(Font.PLAIN));
		button.setBorder(null);

		// Dialog which will do the actual editing
		fileChooser = new JFileChooser();
	}

	@Override
	public Object getCellEditorValue() {
		return file;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		file = value.toString();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				String defaultPath = Configurations.getInstance().getAttribute("DEFAULTPATH");
				
				if(!"".equals(file.trim()))
				{
					defaultPath = file;
				}
				
				fileChooser.setFileView(new ImageFileView());
				
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Folder Only", "*"));
				fileChooser.setDialogTitle("Please select one folder");
				
				fileChooser.setSelectedFile(new File(defaultPath));
				if (fileChooser.showOpenDialog(button) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile().getAbsolutePath();
					
					// Update Name Column
					table.getModel().setValueAt(
							file.substring(file.lastIndexOf(File.separator) + 1), 
							table.convertRowIndexToModel(row), 
							TestCaseDataTableColumns.SCRIPT_NAME.INDEX);
				}
				
				fireEditingStopped();
			}
		});
		button.setText(file);
		
		return button;
	}
}