package com.n4systems.exporting.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

import com.n4systems.util.MapUtils;

/**
 * A Writer for creating CSV and other delimited files.<p />
 * Example: <p />
 * <pre>	
 * // Create our map writer with the default field separator and delimiter
 * Writer out = new StringWriter();
 * MapWriter mapWriter = new MapWriter(out, "First", "Second", "Third", "Fourth");
 *
 * Map&lt;Integer, String&gt; row = new HashMap&lt;Integer, String&gt;();
 *
 * row.put("First", "The first field");
 * row.put("Second", null);				// this field will appear blank in our file
 * row.put("Third", "last field");
 * row.put("Last", "bad field");		// this field will not be printed, as 
 * 										// there is no corresponding title
 * 
 * // Note that there was a title field defined for "Fourth" but we have not
 * // specified a value for it in this row.  That field will be printed the
 * // same as if we had called 'row.put("Fourth", null);'
 *
 * // write the single row and close the stream.  Note the first call to 
 * // write will also flush the header line
 * mapWriter.write(row);
 * mapWriter.close();
 *
 * System.out.println(out.toString());
 * </pre>
 * Output: <p/>
 * <pre>
 * "First","Second","Third","Fourth"
 * "The first field",,"last field",
 * </pre>
 */
public class CsvMapWriter extends BufferedWriter implements MapWriter {
	private static final String DEFAULT_FIELD_SEP = ",";
	private static final String DEFAULT_QUOTE_CHAR = "\"";

	private final String fieldSep;
	private final String quoteChar;
	private final String[] titles;
	private boolean titleLineWritten = false;
	
	/**
	 * Creates an MapWriter using a the default separator and quotation character.  
	 * Opens a FileWriter as the underlying output stream.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #MapWriter(Writer, String...)
	 */
	public CsvMapWriter(File file, String...titles) throws IOException {
		this(new FileWriter(file), titles);
	}

	/**
	 * Creates an MapWriter using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #MapWriter(Writer, Map, String, String)
	 */
	public CsvMapWriter(Writer out, String...headers) {
		this(out, DEFAULT_FIELD_SEP, DEFAULT_QUOTE_CHAR, headers);
	}
	
	/**
	 * Creates an MapWriter with a custom field separator and quotation character.
	 * @param out	The underlying stream
	 * @param fieldSep		Separator to use between fields.  May be null for no separation	
	 * @param quoteChar		Character to use to quote fields.  May be null for no quoting
	 * @param headers		An array of column titles
	 */
	public <T> CsvMapWriter(Writer out, String fieldSep, String quoteChar, String...headers) {
		super(out);
		
		// copy the headers to avoid external modification
		this.titles = Arrays.copyOf(headers, headers.length);
		this.fieldSep = (fieldSep != null) ? fieldSep : "";
		this.quoteChar = (quoteChar != null) ? quoteChar : "";
	}
	
	private void writeTitleLine() throws IOException {
		// We'll push the titles into a map so we can reuse the writeRow logic
		writeRow(MapUtils.fillMapKeysAndValues(titles));
		titleLineWritten = true;
	}
	
	/**
	 * Writes a single row to the stream.  Fields will be printed in the same order as the titles
	 * @param row			A map of field titles to values.  Titles must match those given 
	 * 						at construction time.  Titles which do not have a corresponding 
	 * 						value in this map will be assumed null.  Values in this map which 
	 * 						do not have a corresponding title will be silently ignored.
	 * @throws IOException
	 */
	public void write(Map<String, ?> row) throws IOException {
		if (!titleLineWritten) {
			writeTitleLine();
		}
		writeRow(row);
	}
	
	private void writeRow(Map<String, ?> row) throws IOException {			
		write(formatLine(row));
		newLine();
		flush();
	}

	private String formatLine(Map<String, ?> row) {
		StringBuffer line = new StringBuffer();

		boolean firstField = true;
		for (String title: titles) {
			// make sure the first field in the line does 
			// not get a field separator
			if (firstField) {
				firstField = false;
			} else {
				line.append(fieldSep);
			}
			
			apppendFieldToLine(line, row.get(title));
		}
		
		return line.toString();
	}

	private void apppendFieldToLine(StringBuffer line, Object field) {
		// if the field is null, we append nothing so we don't get
		// empty quote chars
		if (field != null) {
			appendQuoteChar(line);
			line.append(prepareField(field));
			appendQuoteChar(line);
		}
	}

	private String prepareField(Object field) {
		String value = field.toString();
		
		// we have to make sure new lines get escaped properly
		return value.replace("\n", "\\n");
	}
	
	private void appendQuoteChar(StringBuffer line) {
		if (quoteChar != null) {
			line.append(quoteChar);
		}
	}
}
