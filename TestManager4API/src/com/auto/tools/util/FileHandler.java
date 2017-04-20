package com.auto.tools.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileHandler {
	private static FileHandler m_instance = null;
	private List<File> m_fileList;
	private Logger m_logger = null;
	
	private FileHandler()
	{
		m_fileList = new ArrayList<File>();
		m_logger = Logger.getLogger("FileHandler");
	}
	
	public static FileHandler getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new FileHandler();
		}

		return m_instance;
	}
	
	public List<File> getFileList()
	{
		return m_fileList;
	}
	
	public void clearRecords()
	{
		m_fileList.clear();;
	}
	
	public boolean removeFile(File file)
	{
		if(remove(file))
		{
			m_fileList.add(file);
			m_logger.info("Removed file: " + file.getAbsolutePath());
			return true;
		}
		else
		{
			m_logger.error("Unable to remove file: " + file.getAbsolutePath());
			return false;
		}
	}
	
	private boolean remove(File file)
	{
		if(!file.exists())
		{
			m_logger.warn("Path does not existed: " + file.getAbsolutePath());
			
			return false;
		}
		
		if (file.isDirectory())
		{
			File[] fileList = file.listFiles();
			
			// For System Volume Information folder
			if(fileList == null)
			{
				return false;
			}
						
			// For non-empty folder, remove all sub-files
			if(fileList.length > 0)
			{
				for (File tempfile : fileList)
				{
					if (!tempfile.isHidden())
					{
						remove(tempfile);
					}
				}
			}
			
			// Remove the folder
			return file.delete();
			
		}
		else
		{
			// Remove the file
			return file.delete();
		}
	}

}
