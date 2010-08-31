package com.n4systems.exporting.io;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public interface MapReader extends Closeable {
	/**
	 * Returns the column titles
	 */
	public String[] getTitles() throws IOException, ParseException;
	
	/**
	 * Reads a single row returning a map of headers to their values.
	 */
	public Map<String, Object> readMap() throws IOException, ParseException;
	
	/**
	 * Returns the current row number starting at 1
	 */
	public int getCurrentRow();
}
