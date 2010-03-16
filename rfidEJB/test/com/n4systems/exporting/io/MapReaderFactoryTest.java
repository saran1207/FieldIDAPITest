package com.n4systems.exporting.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

public class MapReaderFactoryTest {
//
//	private static final String[] CSV_CONTENT_TYPES = {
//		"text/csv"
//	};	
//	
//	private static final String[] EXCEL_CONTENT_TYPES = {
//		"application/vnd.ms-excel", 
//		"application/msexcel", "application/x-msexcel", 
//		"application/x-ms-excel", "application/x-excel", 
//		"application/x-dos_ms_excel", "application/xls"
//	};
	
	private byte[] createExcelFile() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ExcelMapWriter writer = new ExcelMapWriter(out, "yyyy-MM-dd");
		
		Map<String, Object> inputMap = new LinkedHashMap<String, Object>();
		inputMap.put("title1", "value1");
		inputMap.put("title2", 42);
		inputMap.put("title3", "hello \" world");
		inputMap.put("title4", "asd,bleh");
		
		writer.write(inputMap);
		writer.close();
		
		return out.toByteArray();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void create_reader_throws_exception_on_bad_content_type() throws IOException {
		MapReaderFactory readerFactory = new MapReaderFactory();
		
		readerFactory.createMapReader(new ByteArrayInputStream(new byte[0]), "hello/world");
	}
	
	@Test
	public void factory_creates_proper_reader_for_content_type() throws IOException {
		MapReaderFactory readerFactory = new MapReaderFactory();
		
		byte[] excelFile = createExcelFile();
		
		MapReader reader = readerFactory.createMapReader(new ByteArrayInputStream(new byte[0]), "text/csv");
		assertTrue(reader instanceof CsvMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/vnd.ms-excel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/msexcel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/x-msexcel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/x-ms-excel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/x-excel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/x-dos_ms_excel");
		assertTrue(reader instanceof ExcelMapReader);
		
		reader = readerFactory.createMapReader(new ByteArrayInputStream(excelFile), "application/xls");
		assertTrue(reader instanceof ExcelMapReader);
	}
	
	@Test
	public void responds_correctly_to_is_csv() {
		MapReaderFactory readerFactory = new MapReaderFactory();
		
		assertTrue(readerFactory.isCsv("text/csv"));
		assertFalse(readerFactory.isCsv("application/vnd.ms-excel"));
		assertFalse(readerFactory.isCsv("application/msexcel"));
		assertFalse(readerFactory.isCsv("application/x-msexcel"));
		assertFalse(readerFactory.isCsv("application/x-ms-excel"));
		assertFalse(readerFactory.isCsv("application/x-excel"));
		assertFalse(readerFactory.isCsv("application/x-dos_ms_excel"));
		assertFalse(readerFactory.isCsv("application/xls"));
		assertFalse(readerFactory.isCsv("hello/world"));
	}
	
	@Test
	public void responds_correctly_to_is_excel() {
		MapReaderFactory readerFactory = new MapReaderFactory();
		
		assertFalse(readerFactory.isExcel("text/csv"));
		assertTrue(readerFactory.isExcel("application/vnd.ms-excel"));
		assertTrue(readerFactory.isExcel("application/msexcel"));
		assertTrue(readerFactory.isExcel("application/x-msexcel"));
		assertTrue(readerFactory.isExcel("application/x-ms-excel"));
		assertTrue(readerFactory.isExcel("application/x-excel"));
		assertTrue(readerFactory.isExcel("application/x-dos_ms_excel"));
		assertTrue(readerFactory.isExcel("application/xls"));
		assertFalse(readerFactory.isCsv("hello/world"));
	}
	
	@Test
	public void responds_correctly_to_is_supported_content_type() {
		MapReaderFactory readerFactory = new MapReaderFactory();
		
		assertTrue(readerFactory.isSupportedContentType("text/csv"));
		assertTrue(readerFactory.isSupportedContentType("application/vnd.ms-excel"));
		assertTrue(readerFactory.isSupportedContentType("application/msexcel"));
		assertTrue(readerFactory.isSupportedContentType("application/x-msexcel"));
		assertTrue(readerFactory.isSupportedContentType("application/x-ms-excel"));
		assertTrue(readerFactory.isSupportedContentType("application/x-excel"));
		assertTrue(readerFactory.isSupportedContentType("application/x-dos_ms_excel"));
		assertTrue(readerFactory.isSupportedContentType("application/xls"));
		assertFalse(readerFactory.isSupportedContentType("hello/world"));
	}
}
