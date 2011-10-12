package com.n4systems.exporting.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface MapWriter extends Closeable {
	/**
	 * Writes a single row to the stream.  The first call to write will define the titles.
	 * Titles are built from the keySet of the row in their iteration order.  This means that
	 * if column order is important, a LinkedHashMap should be used. Subsequent calls
	 * will write fields in the same order as the titles.
	 * @param row			A map of field titles to values.  Titles which do not have a corresponding 
	 * 						value in this map will be assumed null.  Values which 
	 * 						do not have a corresponding title will be silently ignored.
	 * @throws IOException
	 */
	public void write(Map<String, Object> row) throws IOException;
}
