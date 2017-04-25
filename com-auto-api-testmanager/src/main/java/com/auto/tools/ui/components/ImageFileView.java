package com.auto.tools.ui.components;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

public class ImageFileView extends FileView {

//	public Boolean isTraversable(File file) {
//		if(FolderFilter.accept(file))
//		{
//			return false;
//		}
//		else
//		{
//			return null; //let the L&F FileView figure this out
//		}
//	}

	public Icon getIcon(File file) {
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);

		if(file.isFile() && file.getName().endsWith(".class"))
		{
			Image img;
			try {
				img = ImageIO.read(new File("icons\\junit.jpg"));
				Image resizedImage;
				resizedImage = img.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT);
				icon = new ImageIcon(resizedImage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return icon;
	}
}
