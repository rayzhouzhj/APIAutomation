package com.auto.logs;

import java.text.SimpleDateFormat;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import com.auto.test.TestContext;

public class FormatHTMLLayout extends HTMLLayout {     
    
    public FormatHTMLLayout(String testName) {
    	super();
    	
    	this.testName = testName;
    }     
    
    protected final int BUF_SIZE = 256;     
    
    protected final int MAX_CAPACITY = 1024;     
    
    static String TRACE_PREFIX = "<br>    ";     
    
    private StringBuffer sbuf = new StringBuffer(BUF_SIZE);     
         
    String title="Log Messages";     
    
    String testName = null;
    
    /**   
     * A string constant used in naming the option for setting the the HTML   
     * document title. Current value of this string constant is <b>Title</b>.   
     */    
    public static final String TITLE_OPTION = "Title";     
    
    // Print no location info by default     
    boolean locationInfo = false;     
         
    public String format(LoggingEvent event) {     
        if (sbuf.capacity() > MAX_CAPACITY) {     
            sbuf = new StringBuffer(BUF_SIZE);     
        } else {     
            sbuf.setLength(0);     
        }     
        sbuf.append(Layout.LINE_SEP + "\n<tr>" + Layout.LINE_SEP);     
                     
        sbuf.append("<td>");     
        sbuf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));     
        sbuf.append("</td>" + Layout.LINE_SEP);     
    
        if (locationInfo) 
 	   {     
             LocationInfo locInfo = event.getLocationInformation();     
             sbuf.append("<td>");
             sbuf.append(Transform.escapeTags(locInfo.getFileName()));    
             sbuf.append(':');    
             sbuf.append(locInfo.getLineNumber());    
             sbuf.append("</td>" + Layout.LINE_SEP);    
 	    }
 	   else
 	   {
            sbuf.append("<td>");
            sbuf.append(TestContext.getInstance().getVariable("Current_Step"));    
            sbuf.append("</td>" + Layout.LINE_SEP);    
 	   }
        
        sbuf.append("<td>");
        if (event.getLevel().equals(Level.DEBUG)) {
            sbuf.append("<font color=\"#339933\">");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</font>");
          }
        else if(event.getLevel().equals(Level.WARN)) {
            sbuf.append("<font color=\"#f4d742\"><strong>");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</strong></font>");
        } 
        else if(event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            sbuf.append("<font color=\"#993300\"><strong>");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</strong></font>");
        } else {
        	sbuf.append("<font color=\"#006400\">");
            sbuf.append(Transform.escapeTags("Passed"));
            sbuf.append("</font>");
        }   
        sbuf.append("</td>" + Layout.LINE_SEP);     
             
        sbuf.append("<td>");
        sbuf.append(event.getRenderedMessage());     
        sbuf.append("</td>" + Layout.LINE_SEP);     
        sbuf.append("</tr>" + Layout.LINE_SEP);     
    
        if (event.getNDC() != null) {     
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");     
            sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));     
            sbuf.append("</td></tr>" + Layout.LINE_SEP);     
        }     
    
        String[] s = event.getThrowableStrRep();     
        if (s != null) {     
            sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"4\">");     
            appendThrowableAsHTML(s, sbuf);     
            sbuf.append("</td></tr>" + Layout.LINE_SEP);     
        }     
        return sbuf.toString() + "\n";
    }     
    
    private void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {     
        if (s != null) {     
            int len = s.length;     
            if (len == 0)     
                return;     
            sbuf.append(Transform.escapeTags(s[0]));     
            sbuf.append(Layout.LINE_SEP);     
            for (int i = 1; i < len; i++) {     
                sbuf.append(TRACE_PREFIX);     
                sbuf.append(Transform.escapeTags(s[i]));     
                sbuf.append(Layout.LINE_SEP);     
            }     
        }     
    }     
    
    /**   
     * Returns appropriate HTML headers.   
     */    
    public String getHeader() {     
    	StringBuffer sbuf = new StringBuffer();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"  + Layout.LINE_SEP);
        sbuf.append("<html>" + Layout.LINE_SEP);
        sbuf.append("\n<head>" + Layout.LINE_SEP);
        sbuf.append("\n<title>" + title + "</title>" + Layout.LINE_SEP);
        sbuf.append("\n<style type=\"text/css\">"  + Layout.LINE_SEP);
        sbuf.append("<!--"  + Layout.LINE_SEP);
        sbuf.append("body, table {font-family: arial,sans-serif; font-size: x-small;}" + Layout.LINE_SEP);
        sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
        sbuf.append("-->" + Layout.LINE_SEP);
        sbuf.append("</style>" + Layout.LINE_SEP);
        sbuf.append("\n</head>" + Layout.LINE_SEP);
        sbuf.append("\n<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);
        sbuf.append("\n<hr size=\"1\" noshade>" + Layout.LINE_SEP);
        sbuf.append("\n<B>Script</B>: " + testName + "<br>" + Layout.LINE_SEP);
        sbuf.append("\n<B>Execution Status: <font color=\"#f4d742\">RUNNING</font></B><br>" + Layout.LINE_SEP);
        sbuf.append("\n<br>" + Layout.LINE_SEP);
        
        sbuf.append("\nLog session start time " + new java.util.Date() + Layout.LINE_SEP);
//        sbuf.append("<br>" + Layout.LINE_SEP);
        sbuf.append("\n<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">" + Layout.LINE_SEP);
        sbuf.append("\n<tr>" + Layout.LINE_SEP);    
         
        sbuf.append("\n<th>Time</th>" + Layout.LINE_SEP);
        sbuf.append("\n<th>Test Step</th>" + Layout.LINE_SEP);     
        
        if (locationInfo) 
        {     
            sbuf.append("\n<th>File:Line</th>" + Layout.LINE_SEP);     
        }     
        else
        {
        	sbuf.append("\n<th>Status</th>" + Layout.LINE_SEP); 
        }
     
        sbuf.append("\n<th>Message</th>" + Layout.LINE_SEP);     
        sbuf.append("\n</tr>" + Layout.LINE_SEP);     
        sbuf.append("\n<br></br>" + Layout.LINE_SEP);     
        return sbuf.toString() + "\n";
    }     
    
}