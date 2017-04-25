package com.auto.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

	private String stagingurl = "";
	private String staginguser = "";
	private String stagingpwd = "";

	private String produrl = "";
	private String produser = "";
	private String prodpwd = "";

	private DBServer type;
	
	public DBUtils(DBServer type)
	{
		this.type = type;
	}
	
	public Connection getConnection()
	{
		if(type == DBServer.PRODUCTION)
		{
			System.out.println("Connecting to Production DB: " + produrl);
			return getConnection(produrl, produser, prodpwd);
		}
		else
		{
			System.out.println("Connecting to Staging DB: " + stagingurl);
			return getConnection(stagingurl, staginguser, stagingpwd);
		}
	}
	
	private Connection getConnection(String url, String user, String pwd)
	{
		try {
			return  DriverManager.getConnection(url, user, pwd);

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return null;
	}
	
	public Statement executeSQL(Connection conn, String sql)
	{
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stmt;
	}

}
