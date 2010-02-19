package com.n4systems.exporting.io;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.n4systems.model.downloadlink.ContentType;

public class MapWriterFactoryTest {

	@Test
	public void create_creates_corect_type() throws IOException { 
		MapWriterFactory factory = new MapWriterFactory();
		
		MapWriter writer = factory.create(new ByteArrayOutputStream(), ContentType.CSV);
		
		assertTrue(writer instanceof CsvMapWriter);
		
		writer = factory.create(new ByteArrayOutputStream(), ContentType.EXCEL);
		
		assertTrue(writer instanceof ExcelMapWriter);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void create_throws_exception_on_bad_content_type() throws IOException { 
		MapWriterFactory factory = new MapWriterFactory();
		
		factory.create(new ByteArrayOutputStream(), ContentType.ZIP);
	}
}
