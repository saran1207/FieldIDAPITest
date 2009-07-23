package com.n4systems.util;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

/**
 * Thin wrapper around the File class, to handle directory specific functions.
 */
public class Directory {
	private final File path;
	
	/**
	 * Creates a Directory object denoted by path.  If path exists, it MUST be a directory, otherwise an IllegalArgumentException is thrown.
	 * @param path A File path representation of this Directory.
	 * @throws IllegalArgumentException when path exists and is not a directory.
	 */
	public Directory(File path) {
		if (path.exists() && !path.isDirectory()) {
			throw new IllegalArgumentException("Path must be a directory if it exists.");
		}
		this.path = path;
	}
	
	/**
	 * @return The File path denoted by this directory
	 */
	public File getPath() {
		return path;
	}
	
	/**
	 * @return True if this directory exists on the file system and is a directory.
	 */
	public boolean exists() {
		return path.isDirectory();
	}
	
	/**
	 * Creates this directory and any parent directories.
	 * @return	True if the directory was created.  False otherwise.
	 */
	public boolean create() {
		return path.mkdirs();
	}

	/**
	 * Returns a collection of Files contained within this directory.
	 * @param recursive	When true, returns all files recursively
	 * @return	A Collection of Files
	 */
	@SuppressWarnings("unchecked")
	public Collection<File> listFiles(boolean recursive) {
		return FileUtils.listFiles(path, null, recursive);
	}
	
	/**
	 * Calculates the total size in bytes of this directory
	 * @return size in bytes
	 */
	public long getTotalSize() {
		return exists() ? FileUtils.sizeOfDirectory(path) : 0;
	}

	/**
	 * Calculates the total number of files in this directory, recursively.
	 * @return Total number of files
	 */
	public long getNumberOfFiles() {
		return exists() ? listFiles(true).size() : 0;
	}
}
