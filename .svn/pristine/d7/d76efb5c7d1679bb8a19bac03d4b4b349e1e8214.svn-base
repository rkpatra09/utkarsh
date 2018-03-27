package com.beroe.connectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.beroe.utility.Utils;
 public class DBUtil {
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet result) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isDBConnectionActive(Connection connection) {
		if (connection != null)
			return true;
		else
			return false;
	}

	public static ResultSet executeQuery(String query) {
		Statement stmt;
		Connection connection = DBConnectionFactory.getConnection();
		try {
			Utils.debug("Executing query :" + query);
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utils.debug("error in executing the query :" + query);
		return null;
	}
}

