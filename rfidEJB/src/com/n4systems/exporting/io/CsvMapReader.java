package com.n4systems.exporting.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvMapReader extends BufferedReader implements MapReader {
	private static final String DEFAULT_FIELD_SEP = ",";
	private static final String DEFAULT_QUOTE_CHAR = "\"";
	
	private final String fieldSep;
	private final String quoteChar;

	private int parseIndex;
	private String[] titles;
	private boolean titleLineRead = false;
	private int currentRow = 0;
	
	/**
	 * Convenience constructor opening a buffered FileWriter as the underlying stream and using
	 * the default quotation and separation characters.
	 */
	public CsvMapReader(File file) throws IOException {
		this(new FileReader(file), DEFAULT_FIELD_SEP, DEFAULT_QUOTE_CHAR);
	}
	
	/**
	 * Creates an ArrayReader using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #ArrayReader(Writer, String, String)
	 */
	public CsvMapReader(Reader in) {
		this(in, DEFAULT_FIELD_SEP, DEFAULT_QUOTE_CHAR);
	}

	/**
	 * Creates an ArrayReader with a custom field separator and quotation character.
	 * @param in			The underlying stream
	 * @param fieldSep		Separator to use between fields.  May be null for no separation	
	 * @param quoteChar		Character to use to quote fields.  May be null for no quoting
	 */
	public CsvMapReader(Reader in, String fieldSep, String quoteChar) {
		super(in);
		this.fieldSep = (fieldSep != null) ? fieldSep : "";
		this.quoteChar = (quoteChar != null) ? quoteChar : "";
	}
	
	public String[] getTitles() throws IOException, ParseException {
		if (!readTitleLine()) {
			return null;
		}
		return titles;
	}
	
	@Override
	public String readLine() throws IOException {
		currentRow++;
		return super.readLine();
	}
	
	/**
	 * Reads and parses the title line.  Returns false if read failed.
	 */
	private boolean readTitleLine() throws IOException, ParseException {
		if (titleLineRead) {
			return true;
		}
		
		String line = readLine();
		if (line == null) {
			return false;
		}
		
		titles = parseLine(line);
		titleLineRead = true;
		return true;
	}
	
	/**
	 * Reads a single row returning a map of titles to their values.  
	 * @return
	 */
	public Map<String, String> readMap() throws IOException, ParseException {
		if (!readTitleLine()) {
			return null;
		}
		
		String line = readLine();
		if (line == null) {
			return null;
		}
		
		Map<String, String> row  = constructMap(line);
		return row;
	}
	
	private Map<String, String> constructMap(String line) throws ParseException {
		// use a linked hash map so that the iteration order is the same as in the file
		Map<String, String> row = new LinkedHashMap<String, String>();
		
		String value;
		String[] parsedLine = parseLine(line);
		for (int i = 0; i < titles.length; i++) {
			// If this line had less columns then the title line,
			// the rest of the values will go in as null
			value = (i < parsedLine.length) ? parsedLine[i] : null;
			
			row.put(titles[i], value);
		}
		
		return row;
	}
	
	private String[] parseLine(String line) throws ParseException {
		List<String> fields = new ArrayList<String>();
		parseIndex = 0;
		
		while (parseIndex <= line.length()) {
			fields.add(parseNextField(line));
		}
		
		return fields.toArray(new String[fields.size()]);
	}
	
	private String parseNextField(String line) throws ParseException {
		/*
		 * Each field may or may not be quoted.  Excel for example only quotes a field
		 * when the delimiter is found in the cell somewhere.  OpenOffic Calc always quotes
		 * unless you tell it to never quote.
		 *
		 * we consider a field quoted if the string starts with our quoteChar string.  This means
		 * there can be no leading characters after the separator.  Eg , "bleh" will not be 
		 * considered quoted as there is a space in front of our quote char.  This follows similar 
		 * rules to how OpenOffice/Excel handle csv parsing
		 */
		
		int startAt = parseIndex;
		String endChar = "";
		if (line.startsWith(quoteChar, parseIndex)) {
			endChar = quoteChar;
			startAt += quoteChar.length();
		}
		
		// search for the next appearance of our ending char (could be the quote char or empty) and our field sep
		int endAt = line.indexOf(endChar + fieldSep, startAt);
		
		if (endAt == -1) {
			// we may be looking at the last field on the line
			if (line.endsWith(endChar)) {
				endAt = line.length() - endChar.length();
			}
		}
		
		/*
		 *  The first can happen when the field is quoted but there's no ending quote string before the end of the line.
		 *  The second can happen if the quote string is the only string in this field /eg ,",
		 */
		if (endAt == -1 || startAt > endAt) {
			throw new ParseException("No ending delimeter [" + line.substring(parseIndex) + "]", parseIndex);
		} 
		
		parseIndex = endAt + endChar.length() + fieldSep.length();
		return line.substring(startAt, endAt);
	}

	@Override
	public int getCurrentRow() {
		return currentRow;
	}
}



















