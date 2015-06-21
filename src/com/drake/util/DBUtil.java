package com.drake.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static Connection connection = null;
	
	final static String url = "jdbc:mysql://127.0.0.1:3306/library";
	final static String user = "root";
	final static String password = "100912";
	
	
	static {	
		try {
			// load driver
			Class.forName("com.mysql.jdbc.Driver");
			// 打开connection
			connection = DriverManager.getConnection(url, user, password);	
			
		} catch (ClassNotFoundException e) {		
			e.printStackTrace();		
		} catch (SQLException e) {	
			e.printStackTrace();
		}			
	}
	
	
	public static  Connection getConnection() {
		return DBUtil.connection;
	}

}
