package com.beroe.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.beroe.utility.ReadConfiguration;


public class DBConnectionFactory {
	private static String DbUrl = "jdbc:mysql://"
			+ ReadConfiguration.DB_SERVER_IP + ":"
			+ ReadConfiguration.DB_SERVER_PORT + "/"
			+ ReadConfiguration.DB_NAME;
	private static String User = ReadConfiguration.DB_SERVER_USER;
	private static String Pwd = ReadConfiguration.DB_SERVER_PASSWORD;
	private static String Driver = ReadConfiguration.DRIVER_CLASS;
	private static Connection connection = null;
	
	private DBConnectionFactory() {
	}

	private static Connection createConnection() {
		// Connection connection = null;
		try {
			System.out.println("Driver = " + Driver.toString());
			Class.forName(Driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("DbUrl = "+DbUrl);
			System.out.println("User = "+User);
			System.out.println("Pwd = "+Pwd);
			connection = DriverManager.getConnection(DbUrl, User, Pwd);
		//	connection = DriverManager.getConnection(DbUrl, User, "tvbsadmin");
		} catch (SQLException e) {
			System.out.println("UNABLE TO CONNECT TO DATABASE");
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection getConnection() {
		try {
			if ((connection == null) || (connection.isClosed())) {
				connection = DBConnectionFactory.createConnection();
			}
		} catch (SQLException e) {
			System.err.println("Unable to create the database connection ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
