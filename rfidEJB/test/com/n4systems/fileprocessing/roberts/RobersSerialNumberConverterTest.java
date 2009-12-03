package com.n4systems.fileprocessing.roberts;
import static org.junit.Assert.*;

import org.junit.Test;

public class RobersSerialNumberConverterTest {
	
	private RobertsSerialNumberConverter serialConverter = new RobertsSerialNumberConverter();
	
	@Test
	public void handles_null() {
		assertEquals(null, serialConverter.toCSV(null));
	}
	
	@Test
	public void handles_empty() {
		assertEquals("", serialConverter.toCSV(""));
	}
	
	@Test
	public void convert_single() {
		assertEquals("SN12345", serialConverter.toCSV("SN12345"));
	}
	
	@Test
	public void handles_bad_formats() {
		assertEquals("sn123xto2", serialConverter.toCSV("sn123xto2"));
		assertEquals("sn123toX", serialConverter.toCSV("sn123toX"));
		assertEquals("sn123to3X", serialConverter.toCSV("sn123to3X"));
	}
	
	@Test
	public void convert_simple_range() {
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV("sn1to5"));
	}
	
	@Test
	public void convert_numbers_only() {
		assertEquals("1,2,3,4,5", serialConverter.toCSV("1to5"));
	}
	
	@Test
	public void handles_capital_to() {
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV("sn1TO5"));
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV("sn1To5"));
	}
	
	@Test
	public void trims_white_space() {
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV(" sn1to5 "));
	}
	
	@Test
	public void handle_range_of_one() {
		assertEquals("sn1", serialConverter.toCSV("sn1to1"));
	}
	
	@Test
	public void handles_zero_padding() {
		assertEquals("sn001,sn002,sn003,sn004,sn005", serialConverter.toCSV("sn001to05"));
	}
	
	@Test
	public void handles_decending() {
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV(" sn5to1 "));
	}
	
	@Test
	public void convert_simple_range_with_spaces() {
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV("sn 1 to 5 "));
		assertEquals("sn1,sn2,sn3,sn4,sn5", serialConverter.toCSV("sn1 to 5 "));
		assertEquals("sn01,sn02,sn03,sn04,sn05", serialConverter.toCSV("sn 01 to 5 "));
	}
	
	@Test
	public void convert_range_with_number_in_middle() {
		assertEquals("sn2fds1,sn2fds2,sn2fds3,sn2fds4,sn2fds5", serialConverter.toCSV("sn2fds1to5"));
	}
	
	@Test
	public void convert_number_only_range() {
		assertEquals("3301,3302,3303,3304,3305,3306,3307,3308", serialConverter.toCSV("3301 to 3308"));
	}
	
	@Test
	public void convert_long_number_with_prefix() {
		assertEquals("MR1168,MR1169,MR1170,MR1171,MR1172,MR1173", serialConverter.toCSV("MR1168 to MR1173"));
	}
}
