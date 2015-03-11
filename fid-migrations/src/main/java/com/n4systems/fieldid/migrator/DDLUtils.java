package com.n4systems.fieldid.migrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DDLUtils {

	/*
	 * Checks if a foreign key exists from the specified table and column to the reference table and column.  The name of the key is ignored.
	 */
	public static boolean foreignKeyExists(Connection conn, String table, String column, String referencedTable, String referencedColumn) throws SQLException {
		PreparedStatement fkCheckStmt = null;
		try {
			fkCheckStmt = conn.prepareStatement(
					"SELECT count(*) " +
							"FROM `INFORMATION_SCHEMA`.`KEY_COLUMN_USAGE` " +
							"WHERE `TABLE_SCHEMA` = 'fieldid' " +
							"AND `TABLE_NAME` = ? " +
							"AND `COLUMN_NAME` = ? " +
							"AND `REFERENCED_TABLE_NAME` = ? " +
							"AND `REFERENCED_COLUMN_NAME` = ? "
			);
			fkCheckStmt.setString(1, table);
			fkCheckStmt.setString(2, column);
			fkCheckStmt.setString(3, referencedTable);
			fkCheckStmt.setString(4, referencedColumn);
			return DbUtils.executeExists(fkCheckStmt);
		} finally {
			DbUtils.close(fkCheckStmt);
		}
	}

	/*
	 * Checks if a column exists, ignoring type.
	 */
	public static boolean columnExists(Connection conn, String table, String column) throws SQLException {
		return columnExists(conn, table, column, null);
	}

	/*
	 * Checks if a column exists and that it is of the specified type.  Type is ignored if null.
	 */
	public static boolean columnExists(Connection conn, String table, String column, String type) throws SQLException {
		PreparedStatement fkCheckStmt = null;
		try {
			fkCheckStmt = conn.prepareStatement(
					"SELECT count(*) " +
							"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
							"WHERE `TABLE_SCHEMA` = 'fieldid' " +
							"AND `TABLE_NAME` = ? " +
							"AND `COLUMN_NAME` = ?" +
							((type == null) ? "" : " AND `COLUMN_TYPE` = ?")
			);
			fkCheckStmt.setString(1, table);
			fkCheckStmt.setString(2, column);
			if (type != null) {
				fkCheckStmt.setString(3, type);
			}
			return DbUtils.executeExists(fkCheckStmt);
		} finally {
			DbUtils.close(fkCheckStmt);
		}
	}

	/*
	 * Checks if a primary key exists from the specified table.  If columns are supplied, the check tests if the primary key matches the columns
	 * exactly.  If columns are empty, checks that ANY primary key exists for the table.
	 */
	public static boolean primaryKeyExists(Connection conn, String table, String...columns) throws SQLException {
		PreparedStatement fkCheckStmt = null;
		try {
			fkCheckStmt = conn.prepareStatement(
					"SELECT count(*) " +
							"FROM `INFORMATION_SCHEMA`.`KEY_COLUMN_USAGE` " +
							"WHERE `TABLE_SCHEMA` = 'fieldid' " +
							"AND `CONSTRAINT_NAME` = 'PRIMARY' " +
							"AND `TABLE_NAME` = ?" +
							((columns.length == 0) ? "" : " AND `COLUMN_NAME` IN (" + SQLUtils.buildInListParams(columns) + ")")
			);
			fkCheckStmt.setString(1, table);
			for (int i = 0; i < columns.length; i++) {
				fkCheckStmt.setString(i + 2, columns[i]);
			}
			return (columns.length == 0) ? DbUtils.executeExists(fkCheckStmt) : DbUtils.executeCount(fkCheckStmt) == columns.length;
		} finally {
			DbUtils.close(fkCheckStmt);
		}
	}

	/*
	 * Checks if a column exists and that it is of the specified type.  Type is ignored if null.
	 */
	public static boolean tableExists(Connection conn, String table) throws SQLException {
		PreparedStatement fkCheckStmt = null;
		try {
			fkCheckStmt = conn.prepareStatement(
					"SELECT count(*) " +
							"FROM `INFORMATION_SCHEMA`.`TABLES` " +
							"WHERE `TABLE_SCHEMA` = 'fieldid' " +
							"AND `TABLE_NAME` = ? "
			);
			fkCheckStmt.setString(1, table);
			return DbUtils.executeExists(fkCheckStmt);
		} finally {
			DbUtils.close(fkCheckStmt);
		}
	}

}
