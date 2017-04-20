package com.auto.utils;
/**
 * Command Prompt - this class contains method to run windows and mac commands  
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.auto.test.TestContext;

public class CommandPrompt {

	Process process;
	ProcessBuilder builder;

	private static final String[] WIN_RUNTIME = { "cmd.exe", "/C" };
    private static final String[] OS_LINUX_RUNTIME = { "/bin/bash", "-l", "-c" };

    public CommandPrompt() {
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    
	public static Process executeCommand(String command) throws InterruptedException, IOException
	{
		Process tempProcess;
		ProcessBuilder tempBuilder;
		
		String os = System.getProperty("os.name");
		System.out.println("INFO: Run Command on [" + os + "]: " + command);

		String[] allCommand;
		// build cmd proccess according to os
		if(os.contains("Windows")) // if windows
		{
            allCommand = concat(WIN_RUNTIME, new String[]{command});
        } else 
        {
            allCommand = concat(OS_LINUX_RUNTIME, new String[]{command});
        }
		tempBuilder = new ProcessBuilder(allCommand);
		tempBuilder.redirectErrorStream(true);
		Thread.sleep(1000);
		tempProcess = tempBuilder.start();
		
		return tempProcess;
	}

	public void destory()
	{
		process.destroy();
	}

	class StreamDrainer implements Runnable {   
		private BufferedReader reader;   
		public StreamDrainer(BufferedReader ins) 
		{   
			this.reader = ins;   
		}   
		public void run(){   
			String line = null;
			FileWriter fw;
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
				fw = new FileWriter(TestContext.getInstance().getVariable("LogFolder") 
						+ File.separator + "./log_" + dateFormat.format(new Date()) +".txt");

				BufferedWriter bw = new BufferedWriter(fw);

				while ((line = reader.readLine()) != null) 
				{   
					bw.write(line);
					bw.newLine();
					if(!"ON".equalsIgnoreCase(TestContext.getInstance().getVariable("CMD_Mode")))
					{
						System.out.println(line);
					}
				}  

				bw.flush();
				fw.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
