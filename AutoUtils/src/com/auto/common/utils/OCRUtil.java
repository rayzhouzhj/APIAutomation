package com.auto.common.utils;

import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class OCRUtil {

	public static String doOCR(String filePath)
	{
		System.out.println("==================OCR Begins=================");  
		try {
			File imageFile = new File(filePath);
			imageFile = imageFile.getAbsoluteFile();
			ITesseract instance = new Tesseract();
			String result = instance.doOCR(imageFile);
			System.out.println(result);
			System.out.println("==================OCR Completed=================");  
			
			return result;
			
		} catch (TesseractException e) {
			e.printStackTrace();
		}  
		
		return null;		 
	}
	
}