package com.auto.tools.ui.constant;

public enum TestCaseDataTableColumns {

	EXECUTION_FLAG("", 0),
	SCRIPT_NAME("Test Name", 1),
	SCRIPT_PATH("Class Name", 2),
	PRIORITY("Priority", 3),
	TIMEOUT("Timeout", 4),
	STARTTIME("Start Time", 5),
	ENDTIME("End Time", 6),
	STATUS("Status", 7),
	COMMENT("Comment", 8),
	TEST_DATA("Data", 9),
	REPORT("...", 10);
	
	public String NAME;
	public int INDEX;
	public static int COLUMN_COUNT = 11;
	
	private TestCaseDataTableColumns(String name, int index)
	{
		NAME = name;
		INDEX = index;
	}
}
