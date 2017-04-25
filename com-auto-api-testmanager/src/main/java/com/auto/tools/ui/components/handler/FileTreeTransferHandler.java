package com.auto.tools.ui.components.handler;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import com.auto.tools.context.Configurations;
import com.auto.tools.context.Constants;
import com.auto.tools.ui.components.FileTree;
import com.auto.tools.util.JunitReader;

public class FileTreeTransferHandler extends javax.swing.TransferHandler {

	public FileTreeTransferHandler()
	{

	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	protected Transferable createTransferable(JComponent c) {
		FileTree tree = (FileTree)c;

		List<String> selectedPaths = new ArrayList<>();

		String jTreeVarSelectedPath = "";
		TreePath[] paths = tree.getSelectionPaths();

		for(TreePath path : paths)
		{
			jTreeVarSelectedPath = Constants.CLASS_PATH + ".";
			Object[] fp = path.getPath();
			for (int i=1; i<fp.length; i++) 
			{
				jTreeVarSelectedPath += fp[i];

				if (i+1 <fp.length ) {
					jTreeVarSelectedPath += ".";
				}
			}

			// Class File is Selected
			if(jTreeVarSelectedPath.endsWith(".class"))
			{
				jTreeVarSelectedPath = jTreeVarSelectedPath.replace(".class", "");
				List<String> testCasesList = null;
				try 
				{
					testCasesList = JunitReader.getInstance().getJunitTestCases(jTreeVarSelectedPath);
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
				for(String testName : testCasesList)
				{
					selectedPaths.add(jTreeVarSelectedPath + "." + testName);
				}
			}
			// Junit Test is Selected
			else if(jTreeVarSelectedPath.contains(".class"))
			{
				jTreeVarSelectedPath = jTreeVarSelectedPath.replace(".class", "");
				selectedPaths.add(jTreeVarSelectedPath);
			}
			// Folder is selected
			else
			{
				selectedPaths.addAll(readTransferableFromFolder("." + File.separator + jTreeVarSelectedPath));
			}
		}

		if(selectedPaths.size() > 0)
		{
			System.out.println(selectedPaths.stream().collect(Collectors.joining(",")));
			return new StringSelection(selectedPaths.stream().collect(Collectors.joining(",")));
		}
		else
		{
			return null;
		}
	}

	private List<String> readTransferableFromFolder(String path)
	{
		List<String> testCases = new ArrayList<>();
		String jTreeVarSelectedPath = path.replace(".", File.separator);
		if(path.startsWith("."))
		{
			jTreeVarSelectedPath = "." + jTreeVarSelectedPath;
		}
		Path packageFile = Paths.get(jTreeVarSelectedPath);

		if(Files.isDirectory(packageFile))
		{
			try 
			{
				List<Path> list = Files.list(packageFile).collect(Collectors.toList());

				for(Path file : list)
				{
					if(Files.isDirectory(file))
					{
						testCases.addAll(readTransferableFromFolder(file.toString()));
					}
					else
					{
						String filePath = file.toString().replace(".class", "");
						String classPath = filePath.replace("." + File.separator, "");
						classPath = classPath.replace(File.separator, ".");
						List<String> testCasesList = JunitReader.getInstance().getJunitTestCases(classPath);
						for(String testName : testCasesList)
						{
							testCases.add(classPath + "." + testName);
						}
					}
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return testCases;
	}

	protected void exportDone(JComponent c, Transferable t, int action) {
		if (action == MOVE) {
			//	    	((FileTree)c).removes
		}
	}
}
