package com.n4systems.fileprocessing.chant;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fileprocessing.ChantFileProcessor;

public class ChantFileProcessorTest {

	
	
	@Test
	public void should_handle_problematic_date_format() throws Exception {
		ChantFileProcessor sut = new ChantFileProcessor();
		sut.processFile(getClass().getResourceAsStream("test_2200.xml"), "test_2200.xml");
	}

	
	@Test
	public void should_handle_normal_date_format() throws Exception {
		ChantFileProcessor sut = new ChantFileProcessor();
		sut.processFile(getClass().getResourceAsStream("test_2205.xml"), "test_2205.xml");
	}
	
	
	
	@Test
	public void should_clean_date_format_by_removing_the_colon_in_the_timezone_it() throws Exception {
		ChantFilePorcessorTesting sut = new ChantFilePorcessorTesting();
		//with milliseconds
		assertEquals("2008-06-25T08:45:42-0400", sut.correctTimeZoneAndMillisecondFormatting("2008-06-25T08:45:42.5-04:00"));
		//without milliseconds
		assertEquals("2008-06-25T08:45:42-0400", sut.correctTimeZoneAndMillisecondFormatting("2008-06-25T08:45:42-04:00"));
		
		assertEquals("2008-06-25T08:45:42+0400", sut.correctTimeZoneAndMillisecondFormatting("2008-06-25T08:45:42.5+04:00"));
		
		assertEquals("2008-06-25T08:45:42+0400", sut.correctTimeZoneAndMillisecondFormatting("2008-06-25T08:45:42+04:00"));
	}
	
	
	private class ChantFilePorcessorTesting extends ChantFileProcessor {

		@Override
		protected String correctTimeZoneAndMillisecondFormatting(String dateString) {
			// TODO Auto-generated method stub
			return super.correctTimeZoneAndMillisecondFormatting(dateString);
		}
		
	}
}
