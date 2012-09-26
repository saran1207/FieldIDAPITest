package com.n4systems.fieldid.migrator;


import org.apache.log4j.Logger;

import java.sql.*;

public class DbUtils {
	private static Logger logger = Logger.getLogger(DbUtils.class);

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warn("Failed closing Statement", e);
			}
		}
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.warn("Failed closing Connection", e);
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.warn("Failed closing ResultSet", e);
			}
		}
	}

	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				logger.info("Rolling back");
				conn.rollback();
			} catch (SQLException e) {
				logger.error("Failed rolling back Connection", e);
			}
		}
	}
}
