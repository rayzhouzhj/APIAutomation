package com.auto.tools.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.auto.tools.ui.components.handler.TableColumnAdjuster;

public class DataTable extends JTable{

	private static final long serialVersionUID = -4576894393634178700L;

	private JTableHeader header;
	private JPopupMenu renamePopup;
	private JTextField text;
	private TableColumn column;
	private List<TableColumn> cols = new ArrayList<>();
	Enumeration<TableColumn> colEnum;

	public DataTable(TableModel tableModel)
	{
		super(tableModel);

		//  allow drop anywhere in the view port 
		this.setFillsViewportHeight(true);
		this.setAutoscrolls(true);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		Enumeration<TableColumn> colEnum = this.getColumnModel().getColumns();
		while(colEnum.hasMoreElements())
		{
			cols.add(colEnum.nextElement());
		}

		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.setAutoCreateRowSorter(true);
		
		this.resizeColumnWidth();
	}

	public void resizeColumnWidth() {
		TableColumnAdjuster tca = new TableColumnAdjuster(this);
		tca.adjustColumns();

		this.getModel().addTableModelListener(tca);
	}

	private void editColumnAt(Point p)
	{
		int columnIndex = header.columnAtPoint(p);

		if (columnIndex != -1)
		{
			column = header.getColumnModel().getColumn(columnIndex);
			Rectangle columnRectangle = header.getHeaderRect(columnIndex);

			text.setText(column.getHeaderValue().toString());
			renamePopup.setPreferredSize(
					new Dimension(columnRectangle.width, columnRectangle.height - 1));
			renamePopup.show(header, columnRectangle.x, 0);

			text.requestFocusInWindow();
			text.selectAll();
		}
	}

	private void renameColumn()
	{
		column.setHeaderValue(text.getText());
		renamePopup.setVisible(false);
		header.repaint();
	}

	public void hideSelectedColumn()
	{
		this.hideColumn(this.getSelectedColumn());
	}

	public void hideColumn(int index)
	{
		this.removeColumn(this.getColumnModel().getColumn(index));
	}

	public void showHidedColumns()
	{
		// Remove existing columns
		Enumeration<TableColumn> existingColEnum = this.getColumnModel().getColumns();

		while(this.getColumnModel().getColumnCount() > 0)
		{
			this.removeColumn(this.getColumnModel().getColumn(0));
		}

		// Adding original elements back
		for(TableColumn tblCol : cols)
		{
			this.addColumn(tblCol);
		}
	}
}
