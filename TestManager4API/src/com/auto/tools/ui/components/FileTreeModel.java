/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.auto.tools.ui.components;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.auto.tools.context.Configurations;


/**
 *
 * @author haojzhou
 */
public class FileTreeModel extends DefaultTreeModel {

	protected FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	public FileTreeModel(TreeNode root, String path) {
		super(root);

		listNodes((DefaultMutableTreeNode)root);

	}

	/**
	 * 
	 * @param lastTreeNode
	 */
	public void listNodes(DefaultMutableTreeNode lastTreeNode)
	{
		FileNode fileNode = (FileNode) lastTreeNode.getUserObject();
		if (!fileNode.isInit && !(fileNode.file.isFile() && fileNode.file.getName().endsWith(".class")))
		{
			File[] files;
			if (fileNode.isDummyRoot) 
			{
				files = fileSystemView.getRoots();
			} 
			else 
			{
				files = fileSystemView.getFiles(
						((FileNode) lastTreeNode.getUserObject()).file, false);
			}

			Icon icon;
			for (int i = 0; i < files.length; i++) 
			{
				icon = fileSystemView.getSystemIcon(files[i]);
				FileNode childFileNode = new FileNode(
						fileSystemView.getSystemDisplayName(files[i]),
						icon, files[i], false);

				String filter = Configurations.getInstance().getAttribute("FOLDER_FILTER");
				if(filter != null && !filter.contains(files[i].getName()))
				{
					continue;
				}
				
				if(files[i].isDirectory())
				{
					// Do Nothing
				}
				else if(files[i].isFile() && files[i].getName().endsWith(".class"))
				{
					Image img;
					try {
						img = ImageIO.read(new File("icons" + File.separator + "testset.jpg"));
						Image resizedImage;
						resizedImage = img.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT);
						icon = new ImageIcon(resizedImage);

						// Mark the node as file(true) -> not expendable
						childFileNode = new FileNode(
								fileSystemView.getSystemDisplayName(files[i]),
								icon, files[i],
								true);
					} catch (IOException ex) {
						Logger.getLogger(FileTree.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				else
				{
					continue;
				}

				DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childFileNode);
				lastTreeNode.add(childTreeNode);
			}
			
			// Tree Node changes
			this.nodeStructureChanged(lastTreeNode);
		}
		// TreeNode is Initilized
		fileNode.isInit = true;
	}

	@Override
	public boolean isLeaf(Object node) {
		DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode)node;
		FileNode fileNode=(FileNode)treeNode.getUserObject();
		if(fileNode.isDummyRoot)return false;
		return fileNode.file.isFile();
	}
}
