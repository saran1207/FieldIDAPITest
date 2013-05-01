package com.n4systems.fieldid.migrator;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MigrationRunner {
	private static final String MigrationsPackage = "com.n4systems.fieldid.migrator.migrations";
	private static final String MigrationsPath = MigrationsPackage.replace('.', '/');
    private static Logger logger = Logger.getLogger(Migration.class);

	public static void main(String[] args) {
		try {
			MigrationRunner runner = new MigrationRunner();
			runner.execute();
		} catch (Exception e) {}
	}

    public void execute() throws Exception {
		Connection conn = null;
        try {
			long start = System.currentTimeMillis();
			logger.info("Running migrations");
			conn = getConnection();
			conn.setAutoCommit(false);
			for (Migration migration: loadMigrations(getCompletedMigrations(conn))) {
				migration.migrate(conn);
			}
			logger.info("Finished migrations in " + ((System.currentTimeMillis() - start) / 1000.0f) + "s");
        } catch (Exception e) {
			logger.error("Migrations Failed", e);
			DbUtils.rollback(conn);
			throw e;
        } finally {
			DbUtils.close(conn);
		}
    }

	private Connection getConnection() throws SQLException, NamingException {
		Connection conn;
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/fieldid");
			conn = ds.getConnection();
		} catch (NoInitialContextException e) {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/fieldid?user=root");
		}
		conn.setAutoCommit(false);
		return conn;
	}

	private List<Long> getCompletedMigrations(Connection conn) throws SQLException {
		List<Long> versions = new ArrayList<Long>();

		PreparedStatement versionStmt = null;
		ResultSet results = null;
		try {
			versionStmt = conn.prepareStatement("SELECT version FROM schema_migrations ORDER BY version");
			results = versionStmt.executeQuery();
			while (results.next()) {
				versions.add(results.getLong(1));
			}
		} finally {
			DbUtils.close(results);
			DbUtils.close(versionStmt);
		}
		return versions;
	}

	public SortedSet<? extends Migration> loadMigrations(List<Long> completedMigrations) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
		URL codeSourceUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
		File codeSourceFile = new File(URLDecoder.decode(codeSourceUrl.getFile(), "UTF-8"));
		MigrationFileFilter migrationFileFilter = new MigrationFileFilter(completedMigrations);

		List<File> migrationFiles;
		if (codeSourceFile.isDirectory()) {
			migrationFiles = loadMigrationsFromClassDirectory(codeSourceFile, migrationFileFilter);
		} else if (codeSourceFile.isFile() && codeSourceFile.getName().endsWith(".jar")) {
			migrationFiles = loadMigrationsFromJar(codeSourceUrl, migrationFileFilter);
		} else {
			throw new RuntimeException("Bad Code Source: " + codeSourceFile.getPath());
		}

		SortedSet<? extends Migration> migrations = instantiateMigrations(migrationFiles);
		return migrations;
	}

	private SortedSet<Migration> instantiateMigrations(List<File> migrationFiles) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		SortedSet<Migration> migrations = new TreeSet<Migration>();
		for (File migrationFile: migrationFiles) {
			logger.debug("Adding Migration: " + migrationFile.getName());
			if (migrationFile.getName().endsWith(".class")) {
				Class<?> migrationClass = Class.forName(MigrationsPackage + '.' + migrationFile.getName().replace(".class", ""));
				if (Migration.class.isAssignableFrom(migrationClass)) {
					migrations.add((Migration) migrationClass.newInstance());
				}
			} else if (migrationFile.getName().endsWith(".sql")) {
				migrations.add(new ScriptMigration('/' + MigrationsPath + '/' + migrationFile.getName()));
			}
		}
		return migrations;
	}

	public List<File> loadMigrationsFromClassDirectory(File codeSourceDir, FileFilter fileFilter) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
		File migrationPath = new File(codeSourceDir, MigrationsPath);

		logger.info("Scanning migrations in: " + migrationPath);
		File[] migrationFiles = migrationPath.listFiles(fileFilter);
		return Arrays.asList(migrationFiles);
	}

	public List<File> loadMigrationsFromJar(URL codeSourceJar, FileFilter fileFilter) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
		List<File> migrationFiles = new ArrayList<File>();

		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(codeSourceJar.openStream());
			ZipEntry ze;
			while ((ze = zip.getNextEntry()) != null) {
				if (!ze.isDirectory() && ze.getName().startsWith(MigrationsPath)) {
					File migrationFile = new File(ze.getName());
					if (fileFilter.accept(migrationFile)) {
						migrationFiles.add(migrationFile);
					}
				}
				zip.closeEntry();
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}
		return migrationFiles;
	}



}
