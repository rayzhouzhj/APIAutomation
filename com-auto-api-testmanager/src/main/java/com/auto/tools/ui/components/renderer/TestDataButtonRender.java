package com.auto.tools.ui.components.renderer;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TestDataButtonRender  implements TableCellRenderer {
	private JPanel panel;

	private JButton button;
	
	public TestDataButtonRender()
    {
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
    }
	
	private void initPanel()
    {
        this.panel = new JPanel();

        // Not filling the whole cell
        this.panel.setLayout(null);
    }
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
        return this.panel;
    }

}
