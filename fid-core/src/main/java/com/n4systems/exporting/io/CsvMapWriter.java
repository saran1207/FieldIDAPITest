package com.n4systems.exporting.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.n4systems.util.MapUtils;


public class CsvMapWriter implements MapWriter {
	private static final String DEFAULT_FIELD_SEP = ",";
	private static final String DEFAULT_QUOTE_CHAR = "\"";

	private BufferedWriter writer;
	private final String fieldSep;
	private final String quoteChar;
	
	private String[] titles;

	/**
	 * Creates an MapWriter using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #MapWriter(Writer, Map, String, String)
	 */
	public CsvMapWriter(OutputStream out) {
		this(new OutputStreamWriter(out));
	}

	/**
	 * Creates an MapWriter using a the default separator and quotation character.
	 * @see #DEFAULT_FIELD_SEP
	 * @see #DEFAULT_QUOTE_CHAR
	 * @see #MapWriter(Writer, Map, String, String)
	 */
	public CsvMapWriter(Writer out) {
		this(out, DEFAULT_FIELD_SEP, DEFAULT_QUOTE_CHAR);
	}

	/**
	 * Creates an MapWriter with a custom field separator and quotation character.
	 * @param out			The underlying stream
	 * @param fieldSep		Separator to use between fields.  May be null for no separation	
	 * @param quoteChar		Character to use to quote fields.  May be null for no quoting
	 */
	public <T> CsvMapWriter(Writer out, String fieldSep, String quoteChar) {
		this.writer = new BufferedWriter(out);
		this.fieldSep = (fieldSep != null) ? fieldSep : "";
		this.quoteChar = (quoteChar != null) ? quoteChar : "";
	}
	
	@Override
	public void write(Map<String, Object> row) throws IOException {
		writeTitleLine(row);
		writeRow(row);
	}
	
	@Override
	public void close() throws IOException {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}
	
	private void writeTitleLine(Map<String, ?> row) throws IOException {
		if (titles != null) {
			return;
		}
		
		titles = row.keySet().toArray(new String[0]);
		
		// We'll push the titles into a map so we can reuse the writeRow logic
		writeRow(MapUtils.fillMapKeysAndValues(titles));
	}
	
	private void writeRow(Map<String, ?> row) throws IOException {			
		writer.write(formatLine(row));
		writer.newLine();
		writer.flush();
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
