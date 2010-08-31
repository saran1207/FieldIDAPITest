package com.n4systems.exporting.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import com.n4systems.model.utils.PlainDate;

public class CombineExcelMapReaderWriterTest {

	/*
	 * XXX this test should be split out into 2 tests.  The problem is that writing the test for the
	 * ExcelMapWriter is hard as it's difficult to verify that the excel file is correct
	 */
	@Test
	public void test_read_and_write() throws IOException, ParseException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ExcelMapWriter writer = new ExcelMapWriter(out, "yyyy-MM-dd");
		
		// we loose milliseconds when going to excel so we'll use a date without them
		Date testDate = new PlainDate();
		
		Map<String, Object> inputMap = new LinkedHashMap<String, Object>();
		inputMap.put("title1", "value1");
		inputMap.put("title2", 42);
		inputMap.put("title3", "hello \" world");
		inputMap.put("title4", "asd,bleh");
		inputMap.put("title5", testDate);
		
		writer.write(inputMap);
		writer.close();
		
		ExcelMapReader mapReader = new ExcelMapReader(new ByteArrayInputStream(out.toByteArray()), TimeZone.getTimeZone("UTC"));
		
		Map<String, Object> outputMap = mapReader.readMap();
		
		assertEquals(inputMap.size(), outputMap.size());
		assertEquals("value1", outputMap.get("title1"));
		assertEquals("42", outputMap.get("title2"));
		assertEquals("hello \" world", outputMap.get("title3"));
		assertEquals("asd,bleh", outputMap.get("title4"));
		
		assertEquals(testDate, outputMap.get("title5"));
	}
	
}
