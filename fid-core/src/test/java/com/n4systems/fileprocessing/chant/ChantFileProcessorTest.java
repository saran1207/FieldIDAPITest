package com.n4systems.fileprocessing.chant;

import static org.junit.Assert.*;

import com.n4systems.tools.FileDataContainer;
import org.junit.Test;

import com.n4systems.fileprocessing.ChantFileProcessor;
import com.n4systems.util.ConfigContextRequiredTestCase;

public class ChantFileProcessorTest extends ConfigContextRequiredTestCase {

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
		
		assertEquals("2010-01-25T09:13:09-0500", sut.correctTimeZoneAndMillisecondFormatting("2010-01-25T09:13:09.75-05:00"));
		
		assertEquals("2010-01-25T09:07:36-0500", sut.correctTimeZoneAndMillisecondFormatting("2010-01-25T09:07:36.671875-05:00"));
	}

	@Test
	public void should_retrieve_company_name_from_file() throws Exception {
		ChantFileProcessor sut = new ChantFileProcessor();
        FileDataContainer dataContainer = sut.processFile(getClass().getResourceAsStream("test_2200.xml"), "test_2200.xml");

        assertEquals("Ontario Power Generation Inc.", dataContainer.getCustomerName());
    }

	private class ChantFilePorcessorTesting extends ChantFileProcessor {

		@Override
		protected String correctTimeZoneAndMillisecondFormatting(String dateString) {
			return super.correctTimeZoneAndMillisecondFormatting(dateString);
		}
		
	}

}
