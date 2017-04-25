package com.auto.common.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtils {

	public static void writeToCSV(String csvFile, List<String> dataSource) 
	{
	        FileWriter writer;
	        BufferedWriter bufferWriter;
			try {
				writer = new FileWriter(csvFile);
				bufferWriter = new BufferedWriter(writer);
				for(String line : dataSource)
				{
					bufferWriter.write(line);
					bufferWriter.newLine();
				}
				
				bufferWriter.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
