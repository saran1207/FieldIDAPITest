package com.n4systems.fieldid.migrator;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationFileFilter implements FileFilter {
	private static Logger logger = Logger.getLogger(MigrationFileFilter.class);
	private final Pattern versionPattern = Pattern.compile("^M(\\d{12})_\\w+\\.(sql||class)$");
	private final List<Long> completedMigrations;

	public MigrationFileFilter(List<Long> completedMigrations) {
		this.completedMigrations = completedMigrations;
	}

	@Override
	public boolean accept(File file) {
		logger.debug("Checking: " + file.getPath());
		Matcher versionMatcher = versionPattern.matcher(file.getName());
		if (versionMatcher.matches()) {
			long version = Long.parseLong(versionMatcher.group(1));
			if (!completedMigrations.contains(version)) {
				return true;
			}
		}
		return false;
	}
}
