package com.auto.common.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FileLock {

	private static FileLock instance = null;
	
	public static synchronized FileLock getInstance()
	{
		if(instance == null)
		{
			instance = new FileLock();
		}
		
		return instance;
	}
	
	private boolean lock(Path lockFile)
	{
		try
		{
			if(!Files.exists(lockFile))
			{
				Files.createFile(lockFile);
			}
			else
			{
				return false;
			}

			System.out.println("INFO: Created file lock: " + lockFile.toString());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean waitForUnlock(Path lockFile)
	{
		if(!Files.exists(lockFile))
		{
			return true;
		}
		else
		{
			System.out.println("INFO: File lock is locked.");
		}
		
		boolean result = false;
		
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = LocalDateTime.now();
		
		while(startTime.until(endTime, ChronoUnit.SECONDS) < 90)
		{
			System.out.println("INFO: " + lockFile.toFile().getName() + " is locked by other process, will re-try in 5 seconds.");
			try 
			{
				Thread.sleep(5000);
				if(!Files.exists(lockFile))
				{
					System.out.println("INFO: File lock is released.");
					result = true;
					
					break;
				}
			} 
			catch (InterruptedException e1)
			{
				// DO Nothing
			}
			endTime = LocalDateTime.now();
		}
		
		return result;
	}
	
	public boolean releaseLock(Path lockFile)
	{
		try
		{
			if(!Files.exists(lockFile))
			{
				System.out.println("WARN: Lock file was removed abnormally");
				return true;
			}
			else
			{
				System.out.println("INFO: Released file lock: " + lockFile.toString());
				Files.delete(lockFile);
			}

			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean createLock(Path lockFile)
	{
		boolean result = false;
		
		// Lock device
		if(this.lock(lockFile))
		{
			result = true;
		}
		else
		{
			LocalDateTime startTime = LocalDateTime.now();
			LocalDateTime endTime = LocalDateTime.now();

			while(startTime.until(endTime, ChronoUnit.SECONDS) < 90)
			{
				System.out.println("INFO: " + lockFile.toFile().getName() + " is locked by other process, will re-try in 5 seconds.");
				try 
				{
					Thread.sleep(5000);
					if(this.lock(lockFile))
					{
						System.out.println("INFO: File lock created successfully.");
						result = true;
						
						break;
					}
				} 
				catch (InterruptedException e1)
				{
					// DO Nothing
				}
				endTime = LocalDateTime.now();
			}
		}
		
		return result;
	}
}
