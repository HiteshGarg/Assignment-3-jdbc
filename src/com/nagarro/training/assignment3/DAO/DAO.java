package com.nagarro.training.assignment3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.nagarro.training.assignment3.Constants.Constants;

public class DAO {

	{
		 try {
			Class.forName(Constants.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		
	      Connection conn = null;
		try {
			conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	      return conn;
	}
}
