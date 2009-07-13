package com.n4systems.usage;

public class DiskUsage {
	private long totalSize;
	private long numberOfFiles;

	public DiskUsage() {
		this(0, 0);
	}

	public DiskUsage(long totalSize, long numberOfFiles) {
		super();
		this.totalSize = totalSize;
		this.numberOfFiles = numberOfFiles;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public long getNumberOfFiles() {
		return numberOfFiles;
	}
}
