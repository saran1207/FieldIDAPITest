package com.n4systems.fieldid.migrator.ddl;

import com.n4systems.fieldid.migrator.DDLUtils;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.SQLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlterTable implements ExecutableStatement {
	protected static Logger logger = Logger.getLogger(Migration.class);

	private final String table;
	private final AlterStatement[] statements;

	public static AlterTable named(String table) {
		return new AlterTable(table, new AlterStatement[0]);
	}

	private AlterTable(String table, AlterStatement[] statements) {
		this.table = table;
		this.statements = statements;
	}

	public String getTable() {
		return table;
	}

	public AlterTable add(AlterStatement statement) {
		AlterStatement[] newStatements = Arrays.copyOf(statements, statements.length + 1);
		newStatements[newStatements.length - 1] = statement;
		return new AlterTable(table, newStatements);
	}

	public AlterTable addForeignKey(String name, String column, String referencedTable, String referencedColumn) {
		return add(new AddForeignKey(name, column, referencedTable, referencedColumn));
	}

	public AlterTable addForeignKey(String column, String referencedTable, String referencedColumn) {
		return addForeignKey(null, column, referencedTable, referencedColumn);
	}

	public AlterTable dropForeignKey(String name, String column, String referencedTable, String referencedColumn) {
		return add(new DropForeignKey(name, column, referencedTable, referencedColumn));
	}

	public AlterTable dropColumn(String column) {
		return add(new DropColumn(column));
	}

	public AlterTable addPrimaryKey(String...columns) {
		return add(new AddPrimaryKey(columns));
	}

	public AlterTable addIndex(String...columns) {
		return add(new AddIndex(columns));
	}

	public AlterTable modifyColumn(String name, String type, boolean notNull, boolean autoIncrement) {
		return add(new ModifyColumn(name, type, notNull, autoIncrement));
	}

	public AlterTable modifyColumn(String name, String type, boolean notNull) {
		return modifyColumn(name, type, notNull, false);
	}

	public AlterTable modifyColumn(String name, String type) {
		return modifyColumn(name, type, false, false);
	}

	public AlterTable addColumn(String name, String type, boolean notNull, boolean autoIncrement, boolean unique) {
		return add(new AddColumn(name, type, notNull, autoIncrement, unique));
	}

	@Override
	public void execute(Connection conn) throws SQLException {
		StringBuilder ddlBuilder = new StringBuilder("ALTER TABLE ");
		ddlBuilder.append(SQLUtils.escapeNames(table)).append(' ');

		Predicate<AlterStatement> filterFunction = (stmt) -> {
			try {
				boolean canExecute = stmt.canExecute(conn, table);
				if (!canExecute) {
					logger.warn("Ignoring: " + ddlBuilder.toString() + stmt.getDDL());
				}
				return canExecute;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		};

		// Remove any statements that cannot execute, convert statements to ddl, concat and comma separate.  Chop removed the trailing ','
		String statementDDL = StringUtils.chop(Arrays.stream(statements)
				.filter(filterFunction)
				.map(stmt -> stmt.getDDL())
				.reduce("", (result, stmt) -> result + "\n\t" + stmt + ','));

		// If all statements were unexecutable then the entire alter is a no-op
		if (statementDDL.length() == 0) {
			return;
		}

		String ddl = ddlBuilder.append(statementDDL).toString();

		logger.info("Executing:\n" + ddl);
		conn.createStatement().execute(ddl);
	}

	private abstract class AlterStatement {
		abstract boolean canExecute(Connection conn, String table) throws SQLException;
		abstract String getDDL();

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AlterStatement that = (AlterStatement) o;
			if (!getDDL().equals(that.getDDL())) return false;
			return true;
		}

		@Override
		public int hashCode() {
			return getDDL().hashCode();
		}
	}

	private class AddForeignKey extends AlterStatement {
		private final String name;
		private final String column;
		private final String referencedTable;
		private final String referencedColumn;

		public AddForeignKey(String name, String column, String referencedTable, String referencedColumn) {
			this.name = name;
			this.column = column;
			this.referencedTable = referencedTable;
			this.referencedColumn = referencedColumn;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return !DDLUtils.foreignKeyExists(conn, table, column, referencedTable, referencedColumn);
		}

		@Override
		public String getDDL() {
			StringBuilder sql = new StringBuilder("ADD FOREIGN KEY ");
			if (name != null) {
				sql.append(name).append(' ');
			}
			sql
				.append('(')
				.append(SQLUtils.escapeNames(column))
				.append(") REFERENCES ")
				.append(SQLUtils.escapeNames(referencedTable))
				.append(" (")
				.append(SQLUtils.escapeNames(referencedColumn))
				.append(')');
			return sql.toString();
		}
	}

	private class DropForeignKey extends AlterStatement {
		//You're right, I AM that lazy.
		private String name, column, referencedTable, referencedColumn;

		public DropForeignKey(String name, String column, String referencedTable, String referencedColumn) {
			this.name = name;
			this.column = column;
			this.referencedTable = referencedTable;
			this.referencedColumn = referencedColumn;
		}

		@Override
		boolean canExecute(Connection conn, String table) throws SQLException {
			return DDLUtils.foreignKeyExists(conn, table, column, referencedTable, referencedColumn);
		}

		@Override
		String getDDL() {
			StringBuilder sql = new StringBuilder("DROP FOREIGN KEY ");
			if(name != null) {
				sql.append(name);
			}

			//Yep, that's it... the other fields are only needed to make sure the key exists, so we don't explode when
			//we try to remove it.

			return sql.toString();
		}
	}

	private class AddPrimaryKey extends AlterStatement {
		private final String[] columns;

		public AddPrimaryKey(String... columns) {
			this.columns = columns;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return !DDLUtils.primaryKeyExists(conn, table);
		}

		@Override
		public String getDDL() {
			StringBuilder sql = new StringBuilder("ADD PRIMARY KEY (");
			sql.append(SQLUtils.escapeNames(columns)).append(')');
			return sql.toString();
		}
	}

	private class AddIndex extends AlterStatement {
		private final String[] columns;

		public AddIndex(String... columns) {
			this.columns = columns;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return true;
		}

		@Override
		public String getDDL() {
			StringBuilder sql = new StringBuilder("ADD INDEX (");
			sql.append(SQLUtils.escapeNames(columns)).append(')');
			return sql.toString();
		}
	}

	private class DropColumn extends AlterStatement {
		private final String name;

		public DropColumn(String name) {
			this.name = name;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return DDLUtils.columnExists(conn, table, name);
		}

		@Override
		public String getDDL() {
			return "DROP COLUMN " + SQLUtils.escapeNames(name);
		}
	}

	private class ModifyColumn extends AlterStatement {
		private final String name;
		private final String type;
		private final boolean notNull;
		private final boolean autoIncrement;

		private ModifyColumn(String name, String type, boolean notNull, boolean autoIncrement) {
			this.name = name;
			this.type = type;
			this.notNull = notNull;
			this.autoIncrement = autoIncrement;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return DDLUtils.columnExists(conn, table, name); // this should be improved to detect the data type and options
		}

		@Override
		public String getDDL() {
			return "MODIFY COLUMN " + SQLUtils.escapeNames(name) + ' ' + type + (notNull ? " NOT NULL" : "") + (autoIncrement ? " AUTO_INCREMENT" : "");
		}
	}

	private class AddColumn extends AlterStatement {
		private final String name;
		private final String type;
		private final boolean notNull;
		private final boolean autoIncrement;
		private final boolean unique;

		private AddColumn(String name, String type, boolean notNull, boolean autoIncrement, boolean unique) {
			this.name = name;
			this.type = type;
			this.notNull = notNull;
			this.autoIncrement = autoIncrement;
			this.unique = unique;
		}

		@Override
		public boolean canExecute(Connection conn, String table) throws SQLException {
			return !DDLUtils.columnExists(conn, table, name);
		}

		@Override
		public String getDDL() {
			return "ADD COLUMN " + SQLUtils.escapeNames(name) + ' ' + type + (notNull ? " NOT NULL" : "") + (autoIncrement ? " AUTO_INCREMENT" : "") + (unique ? " UNIQUE" : "");
		}
	}
}
