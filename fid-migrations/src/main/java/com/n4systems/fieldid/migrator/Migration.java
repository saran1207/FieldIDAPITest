package com.n4systems.fieldid.migrator;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Migration implements Comparable<Migration> {
	protected static Logger logger = Logger.getLogger(Migration.class);
	private final Pattern versionPattern = Pattern.compile("^M(\\d{12})_.*$");

	protected abstract void up(Connection conn) throws Exception;

    public void migrate(Connection conn) throws Exception {
		long start = System.currentTimeMillis();
		logger.info("Running migration: " + getMigrationName());
		up(conn);
		updateCompletedVersion(conn);
		conn.commit();
		logger.info("Finished migration: " + getMigrationName() + " in " + ((System.currentTimeMillis() - start) / 1000.0f) + "s");
    }

    private void updateCompletedVersion(Connection conn) throws SQLException {
		PreparedStatement updateVersionStatement = null;
		try {
			updateVersionStatement = conn.prepareStatement("INSERT INTO schema_migrations (version) VALUES (?)");
			updateVersionStatement.setLong(1, getVersion());
			updateVersionStatement.executeUpdate();
		} finally {
			DbUtils.close(updateVersionStatement);
		}
	}

	public String getMigrationName() {
		return getClass().getSimpleName();
	}

	public long getVersion() {
		Matcher versionMatcher = versionPattern.matcher(getMigrationName());
		if (!versionMatcher.matches()) {
			throw new RuntimeException("Invalid migration name: " + getClass().getSimpleName());
		}
		return Long.parseLong(versionMatcher.group(1));
	}

	@Override
	public int compareTo(Migration other) {
		return Long.valueOf(this.getVersion()).compareTo(other.getVersion());
	}

}
