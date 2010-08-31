package com.n4systems.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class DataUnitTest {
	private static final int FLOAT_SCALE = 10;
	
	private static final BigInteger ONE_INT = BigInteger.valueOf(1);
	private static final BigDecimal ONE_FLOAT = BigDecimal.valueOf(1.0);
	
	private static final BigInteger ONE_BYTE = BigInteger.valueOf(8);
	private static final BigInteger ONE_KIBIBYTE_INT = BigInteger.valueOf(1024);
	private static final BigInteger ONE_KILOBYTE_INT = BigInteger.valueOf(1000);
	private static final BigDecimal ONE_KIBIBYTE_FLOAT = BigDecimal.valueOf(1024.0);
	private static final BigDecimal ONE_KILOBYTE_FLOAT = BigDecimal.valueOf(1000.0);
	
	@Test
	public void test_iec_convert_to_fixedpoint() {
		assertEquals(ONE_KIBIBYTE_INT.pow(8), DataUnit.YOBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(7), DataUnit.ZEBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(6), DataUnit.EXBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(5), DataUnit.PEBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(4), DataUnit.TEBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(3), DataUnit.GIBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(2), DataUnit.MEBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(1), DataUnit.KIBIBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KIBIBYTE_INT.pow(0), DataUnit.BYTES.convertTo(ONE_INT, DataUnit.BYTES));
	}
	
	@Test
	public void test_iec_convert_to_floatpoint() {
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(8).setScale(FLOAT_SCALE), DataUnit.YOBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(7).setScale(FLOAT_SCALE), DataUnit.ZEBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(6).setScale(FLOAT_SCALE), DataUnit.EXBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(5).setScale(FLOAT_SCALE), DataUnit.PEBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(4).setScale(FLOAT_SCALE), DataUnit.TEBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(3).setScale(FLOAT_SCALE), DataUnit.GIBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(2).setScale(FLOAT_SCALE), DataUnit.MEBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(1).setScale(FLOAT_SCALE), DataUnit.KIBIBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KIBIBYTE_FLOAT.pow(0).setScale(FLOAT_SCALE), DataUnit.BYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE) );
	}
	
	@Test
	public void test_si_convert_to_fixedpoint() {
		assertEquals(ONE_KILOBYTE_INT.pow(8), DataUnit.YOTTABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(7), DataUnit.ZETTABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(6), DataUnit.EXABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(5), DataUnit.PETABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(4), DataUnit.TERABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(3), DataUnit.GIGABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(2), DataUnit.MEGABYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(1), DataUnit.KILOBYTES.convertTo(ONE_INT, DataUnit.BYTES));
		assertEquals(ONE_KILOBYTE_INT.pow(0), DataUnit.BYTES.convertTo(ONE_INT, DataUnit.BYTES));
	}
	
	@Test
	public void test_si_convert_to_floatpoint() {
		assertEquals(ONE_KILOBYTE_FLOAT.pow(8).setScale(FLOAT_SCALE), DataUnit.YOTTABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(7).setScale(FLOAT_SCALE), DataUnit.ZETTABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(6).setScale(FLOAT_SCALE), DataUnit.EXABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(5).setScale(FLOAT_SCALE), DataUnit.PETABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(4).setScale(FLOAT_SCALE), DataUnit.TERABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(3).setScale(FLOAT_SCALE), DataUnit.GIGABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(2).setScale(FLOAT_SCALE), DataUnit.MEGABYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(1).setScale(FLOAT_SCALE), DataUnit.KILOBYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
		assertEquals(ONE_KILOBYTE_FLOAT.pow(0).setScale(FLOAT_SCALE), DataUnit.BYTES.convertTo(ONE_FLOAT, DataUnit.BYTES).setScale(FLOAT_SCALE));
	}
	
	@Test
	public void test_bit_to_byte_conversion() {
		assertEquals(BigInteger.valueOf(1024).pow(8).multiply(ONE_BYTE), DataUnit.YOBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(7).multiply(ONE_BYTE), DataUnit.ZEBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(6).multiply(ONE_BYTE), DataUnit.EXBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(5).multiply(ONE_BYTE), DataUnit.PEBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(4).multiply(ONE_BYTE), DataUnit.TEBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(3).multiply(ONE_BYTE), DataUnit.GIBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(2).multiply(ONE_BYTE), DataUnit.MEBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(1).multiply(ONE_BYTE), DataUnit.KIBIBYTES.convertTo(ONE_INT, DataUnit.BITS));
		assertEquals(BigInteger.valueOf(1024).pow(0).multiply(ONE_BYTE), DataUnit.BYTES.convertTo(ONE_INT, DataUnit.BITS));
	}
	
	@Test
	public void test_convert_to_smaller() {
		assertEquals(15728640L, DataUnit.MEBIBYTES.convertTo(15L, DataUnit.BYTES));
	}
	
	@Test
	public void test_symbols() {
		assertEquals("GiB", DataUnit.GIBIBYTES.getSymbol());
		assertEquals("GB", DataUnit.GIGABYTES.getSymbol());
		
		assertEquals("MiB", DataUnit.MEBIBYTES.getSymbol());
		assertEquals("MB", DataUnit.MEGABYTES.getSymbol());

		assertEquals("KiB", DataUnit.KIBIBYTES.getSymbol());
		assertEquals("kB", DataUnit.KILOBYTES.getSymbol());

		assertEquals("B", DataUnit.BYTES.getSymbol());
		assertEquals("b", DataUnit.BITS.getSymbol());
	}
	
	@Test
	public void test_format() {
		assertEquals("123 MB", DataUnit.MEGABYTES.format(BigInteger.valueOf(123)));
		
		BigInteger converted = DataUnit.BITS.convertFrom(BigInteger.valueOf(123), DataUnit.YOBIBYTES);
		assertEquals("1189583006500795107910877184 b", DataUnit.BITS.format(converted));
		
		assertEquals("123.457 GiB", DataUnit.GIBIBYTES.format(BigDecimal.valueOf(123.456789), 3));
		
		BigDecimal convertedDec = DataUnit.BYTES.convertTo(BigDecimal.valueOf(1024 * 5), DataUnit.MEBIBYTES);
		assertEquals("0.005 MiB", DataUnit.MEBIBYTES.format(convertedDec, 3));
	}
}
