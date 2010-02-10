package com.n4systems.exporting.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface MapWriter extends Closeable {
	/**
	 * Writes a single map row
	 */
	public void write(Map<String, ?> row) throws IOException;
}
