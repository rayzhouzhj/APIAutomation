package com.auto.tools.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class FileFilter{
	private String m_target;
	private String[] m_criterias;
	private HashMap<String, List<File>> m_results;
	private int m_totalSize = 0;
	private boolean m_isCompleted = false;
	private boolean m_isStopped = false;

	private Logger m_logger = Logger.getLogger("FileFilter");
	
	public FileFilter()
	{
		super();
		m_results = new HashMap<String, List<File>>();
	}
	
	public FileFilter(String target, String[] criterias)
	{
		super();
		m_target = target;
		m_criterias = criterias;
		m_results = new HashMap<String, List<File>>();
	}

	public void filter()
	{
		m_isCompleted = false;
		m_isStopped = false;
		m_logger.info("Filtering on path: " + m_target);
		
		for(String criteria : m_criterias)
		{
			m_results.put(criteria, new ArrayList<File>());
		}
		
		filter(m_target);
		
		if(m_isStopped)
		{
			m_logger.info("Filtering has been stopped.");
		}
		else
		{
			m_logger.info("Completed Filtering on path: " + m_target);
			m_logger.info("Totally [ " + m_totalSize + " ] files are found.");
			m_isCompleted = true;
		}
	}

	/**
	 * 
	 * @param target
	 */
	private void filter(String target)
	{
		if(isStopped()) return;
		
		System.out.println("Checking file path: " + target);
		
		File file = new File(target);
		
		if(!file.exists())
		{
			m_logger.warn("Path does not existed: " + target);
			return;
		}
		if(check(file))
		{
			m_logger.debug("Found file/Folder: " + target);
			return;
		}
		
		if (file.isDirectory())
		{
			File[] fileList = file.listFiles();
			
			// For System Volume Information folder
			if(fileList == null)
			{
				return;
			}
			
			for (File tempfile : fileList)
			{
				if (!tempfile.isHidden())
				{
					filter(tempfile.getAbsolutePath());
				}
			}
		}
	}

	public boolean check(File file)
	{
		Pattern pattern;
		boolean isMatched = false;
		
		for(String criteria : m_criterias)
		{
			pattern = Pattern.compile(criteria);
			if(pattern.matcher(file.getAbsolutePath().replace("\\", "/")).find())
			{
				m_results.get(criteria).add(file);
				m_totalSize++;
				isMatched = true;
				break;
			}
		}
		
		return isMatched;
	}

	public void clearResult()
	{
		m_results.clear();
		m_totalSize = 0;
	}
	
	public String getTarget()
	{
		return m_target;
	}

	public void setTarget(String target)
	{
		m_target = target;
	}

	public HashMap<String, List<File>> getResults()
	{
		return m_results;
	}
	
	public String[] getCriteria() {
		return m_criterias;
	}

	public void setCriteria(String criteria) {
		this.m_criterias = criteria.split(";");
	}
	
	public boolean isCompleted() {
		return m_isCompleted;
	}

	public void isCompleted(boolean isCompleted) {
		this.m_isCompleted = isCompleted;
	}

	public boolean isStopped() {
		return m_isStopped;
	}

	public void Stop() {
		this.m_isStopped = true;
	}
	
	public int getTotalSize() {
		return m_totalSize;
	}

	public void printSearchResult()
	{
		Iterator<File> iterator = null;
		List<File> fileList = null;
		for(String criteria : m_criterias)
		{
			fileList = m_results.get(criteria);
			iterator = fileList.iterator();
			m_logger.info("Totally [ " + fileList.size() + " ] files were found for " + criteria);
			while (iterator.hasNext())
			{
				m_logger.info(iterator.next().getAbsolutePath());
			}
		}
	}
	
	public static void main(String[] args)
	{
//		Logger m_logger = Logger.getLogger(FileFilter.class);
//		m_logger.info("Start testing..");
//		m_logger.info("initialize the object");
//		FileFilter test = new FileFilter();
//		test.setTarget("O:\\EBS ITT\\Ray\\TestFolder");
//		test.setCriteria(".+.+results\\\\Session\\d*");
//		test.filter();
//		m_logger.debug("Search Completed");
//		test.printSearchResult();
		
		String path = "O:\\EBS ITT\\Ray\\TestFolder\\ofoe\\ofoe_ap_1\\MASTERDRIVER\\results\\Session2";
//		String path = "O:\\EBS ITT\\Ray\\TestFolder\\New Folder";
//		File file = new File(path);
//		File[] filelist = file.listFiles();
//		System.out.println(filelist.length);
//		System.out.println(new File(path).delete());
		Pattern pattern = Pattern.compile("results\\\\Session\\d+");
		if(pattern.matcher(path).find())
		{
			System.out.println("Found");
		}
		
	}
}
