package com.n4systems.exporting.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class CsvMapWriterTest {
	
	@Test
	public void test_write() throws IOException {
		String lineSep = System.getProperty("line.separator");
		
		Writer strWrite = new StringWriter();
		
		MapWriter writer = new CsvMapWriter(strWrite);
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("title1", "value1");
		map.put("title2", 42);
		map.put("title3", "hello \" world");
		map.put("title3", "asd,bleh");
		
		writer.write(map);
		
		String expected = "\"title1\",\"title2\",\"title3\"" + lineSep + "\"value1\",\"42\",\"asd,bleh\"" + lineSep;
		
		assertEquals(expected, strWrite.toString());
	}
}
