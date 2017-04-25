/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.auto.tools.ui.components;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import com.auto.tools.context.Configurations;
import com.auto.tools.context.Constants;
import com.auto.tools.ui.components.handler.FileTreeTransferHandler;
import com.auto.tools.ui.components.renderer.FileTreeRenderer;
import com.auto.tools.util.JunitReader;

/**
 *
 * @author haojzhou
 */
public class FileTree  extends JTree {
	private static final long serialVersionUID = 1L;

	public TreePath mouseInPath;
	protected FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	public FileTree(){
		this.setRootVisible(true);
		this.setDragEnabled(true);
		this.setTransferHandler(new FileTreeTransferHandler());

		String rootPath = "." + Constants.CLASS_FILE_PATH;
		File rootFolder = new File(rootPath);
		FileTreeModel model = new FileTreeModel(
				new DefaultMutableTreeNode(
						new FileNode("Test Cases", null, rootFolder, false)),
						rootPath
				);
		this.setModel(model);
		this.setCellRenderer(new FileTreeRenderer());

		addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				DefaultMutableTreeNode lastTreeNode =(DefaultMutableTreeNode) event.getPath().getLastPathComponent();
				FileNode fileNode = (FileNode) lastTreeNode.getUserObject();
				if (!fileNode.isInit && !fileNode.file.getName().endsWith(".class")) 
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

						if(files[i].isDirectory())
						{
							// DO Nothing
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
					DefaultTreeModel treeModel1 = (DefaultTreeModel) getModel();
					treeModel1.nodeStructureChanged(lastTreeNode);
				}
				else if(fileNode.file.isFile() && fileNode.file.getName().endsWith(".class"))
				{
					Icon icon = fileSystemView.getSystemIcon(fileNode.file);
					FileNode childFileNode = null;

					String filePath = fileNode.file.getAbsolutePath();
					filePath = filePath.substring(filePath.indexOf("api" + File.separator + "test") + 9);
					String className = filePath.substring(0, filePath.length()-6);
				    className = Constants.CLASS_PATH + className.replace(File.separator, ".");
					List<String> testCasesList = null;
					try {
						testCasesList = JunitReader.getInstance().getJunitTestCases(className);
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					for (int i = 0; i < testCasesList.size(); i++) 
					{
						String testName = testCasesList.get(i);
						Image img;
						try {
							img = ImageIO.read(new File("icons" + File.separator + "junit.jpg"));
							Image resizedImage;
							resizedImage = img.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT);
							icon = new ImageIcon(resizedImage);

							// Mark the node as file(true) -> not expendable
							childFileNode = new FileNode(
									fileSystemView.getSystemDisplayName(new File(testName)),
									icon, new File(testName),
									false);
						} catch (IOException ex) {
							Logger.getLogger(FileTree.class.getName()).log(Level.SEVERE, null, ex);
						}
						
						DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childFileNode);
						lastTreeNode.add(childTreeNode);
					}
				}
				// TreeNode is Initilized
				fileNode.isInit = true;
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

			}
		});
	}
}


