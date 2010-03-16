package com.n4systems.exporting.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.n4systems.util.MapUtils;

public class CsvMapReader implements MapReader {
	private static final String DEFAULT_FIELD_SEP = ",";
	private static final String DEFAULT_QUOTE_CHAR = "\"";
	
	private final String fieldSep;
	private final String quoteChar;

	private BufferedReader reader;
	private int parseIndex;
	private String[] titles;
	private int currentRow = 0;
	
	/**
	 * Creates an ArrayReader using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #ArrayReader(Writer, String, String)
	 */
	public CsvMapReader(InputStream in) {
		this(new InputStreamReader(in));
	}

	/**
	 * Creates an ArrayReader using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #ArrayReader(Writer, String, String)
	 */
	public CsvMapReader(Reader in) {
		this(new BufferedReader(in), DEFAULT_FIELD_SEP, DEFAULT_QUOTE_CHAR);
	}
	
	/**
	 * Creates an ArrayReader with a custom field separator and quotation character.
	 * @param in			The underlying stream
	 * @param fieldSep		Separator to use between fields.  May be null for no separation	
	 * @param quoteChar		Character to use to quote fields.  May be null for no quoting
	 */
	public CsvMapReader(BufferedReader in, String fieldSep, String quoteChar) {
		this.reader = in;
		this.fieldSep = (fieldSep != null) ? fieldSep : "";
		this.quoteChar = (quoteChar != null) ? quoteChar : "";
	}
	
	@Override
	public String[] getTitles() throws IOException, ParseException {
		if (!readTitleLine()) {
			return null;
		}
		return titles;
	}
	
	@Override
	public Map<String, Object> readMap() throws IOException, ParseException {
		if (!readTitleLine()) {
			return null;
		}
		
		String line = readLine();
		if (line == null) {
			return null;
		}
		
		Map<String, Object> row  = constructMap(line);
		return row;
	}
	
	@Override
	public int getCurrentRow() {
		return currentRow;
	}
	
	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
			reader = null;
		}
	}
	
	public String readLine() throws IOException {
		String nextLine = reader.readLine();
		
		if (nextLine != null) {
			currentRow++;
		}
		
		return nextLine;
	}
	
	/**
	 * Reads and parses the title line.  Returns false if read failed.
	 */
	private boolean readTitleLine() throws IOException, ParseException {
		if (titles != null) {
			return true;
		}
		
		String line = readLine();
		if (line == null) {
			return false;
		}
		
		titles = parseLine(line);
		return true;
	}
	
	private Map<String, Object> constructMap(String line) throws ParseException {
		// map the titles to the line values
		Map<String, Object> row = MapUtils.combineArrays(titles, parseLine(line));		
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
			throw new ParseException("No ending delimeter [" + line.substring(parseIndex) + "]", currentRow);
		} 
		
		parseIndex = endAt + endChar.length() + fieldSep.length();
		return line.substring(startAt, endAt);
	}
}



















