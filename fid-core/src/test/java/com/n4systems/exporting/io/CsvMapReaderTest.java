package com.n4systems.exporting.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CsvMapReaderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_read_simple() throws IOException, ParseException {
		String csv = "\"Field 1\",\"Field 2\",\"Field 3\"\n" + "\"val1\",\"val2\",\"val3\"";
	
		MapReader reader = new CsvMapReader(new StringReader(csv));
		
		Map<String, Object> row = reader.readMap();
	
		assertTrue(row instanceof LinkedHashMap);
		assertEquals("val1", row.get("Field 1"));
		assertEquals("val2", row.get("Field 2"));
		assertEquals("val3", row.get("Field 3"));
	}
	
	@Test(expected=ParseException.class)
	public void test_read_badformat_throws_parse_exception() throws IOException, ParseException {
		String csv = "\"Field 1\",\"Field 2\",\"Field 3\"\n" + "\"val1\",\"val2\",\"val3"; // note val3 missing final quote
	
		MapReader reader = new CsvMapReader(new StringReader(csv));
		
		reader.readMap();
	}
	
	@Test
	public void read_handles_mixed_quotes() throws IOException, ParseException {
		String csv = "\"Field 1\",\"Field 2\",\"Field 3\"\n" + "\"val1\"bleh\",val2,\"val3\"";
	
		MapReader reader = new CsvMapReader(new StringReader(csv));
		
		Map<String, Object> row = reader.readMap();
		
		assertEquals("val1\"bleh", row.get("Field 1"));
		assertEquals("val2", row.get("Field 2"));
		assertEquals("val3", row.get("Field 3"));
	}
}
