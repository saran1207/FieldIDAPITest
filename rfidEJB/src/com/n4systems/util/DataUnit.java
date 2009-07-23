package com.n4systems.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * Copyright (c) 2007, Mark Frederiksen
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 	* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the 
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution
 * 	* Neither the name of the Sixgreen Group nor the names of its contributors may be used to 
 * 	  endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY 
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Used to convert from one data unit to another.  Supports both SI and IEC unit definitions.
 * BigInteger/Decimal are used internally all calculations as there is no guarantee a conversion
 * would not result in an overflow.
 */
public enum DataUnit {
	BITS(Unit.BIT, Prefix.NONE),												BYTES(Unit.BYTE, Prefix.NONE),
	KILOBITS(Unit.BIT, Prefix.KILO),		KIBIBITS(Unit.BIT, Prefix.KIBI),	KILOBYTES(Unit.BYTE, Prefix.KILO),		KIBIBYTES(Unit.BYTE, Prefix.KIBI), 
	MEGABITS(Unit.BIT, Prefix.MEGA),		MEBIBITS(Unit.BIT, Prefix.MEBI),	MEGABYTES(Unit.BYTE, Prefix.MEGA),		MEBIBYTES(Unit.BYTE, Prefix.MEBI), 
	GIGABITS(Unit.BIT, Prefix.GIGA),		GIBIBITS(Unit.BIT, Prefix.GIBI),	GIGABYTES(Unit.BYTE, Prefix.GIGA),		GIBIBYTES(Unit.BYTE, Prefix.GIBI), 
	TERABITS(Unit.BIT, Prefix.TERA),		TEBIBITS(Unit.BIT, Prefix.TEBI),	TERABYTES(Unit.BYTE, Prefix.TERA),		TEBIBYTES(Unit.BYTE, Prefix.TEBI), 
	PETABITS(Unit.BIT, Prefix.PETA),		PEBIBITS(Unit.BIT, Prefix.PEBI),	PETABYTES(Unit.BYTE, Prefix.PETA),		PEBIBYTES(Unit.BYTE, Prefix.PEBI), 
	EXABITS(Unit.BIT, Prefix.EXA),			EXBIBITS(Unit.BIT, Prefix.EXBI),	EXABYTES(Unit.BYTE, Prefix.EXA),		EXBIBYTES(Unit.BYTE, Prefix.EXBI), 
	ZETTABITS(Unit.BIT, Prefix.ZETTA),		ZEBIBITS(Unit.BIT, Prefix.ZEBI),	ZETTABYTES(Unit.BYTE, Prefix.ZETTA),	ZEBIBYTES(Unit.BYTE, Prefix.ZEBI), 
	YOTTABITS(Unit.BIT, Prefix.YOTTA),		YOBIBITS(Unit.BIT, Prefix.YOBI),	YOTTABYTES(Unit.BYTE, Prefix.YOTTA),	YOBIBYTES(Unit.BYTE, Prefix.YOBI);

	private final Unit unit;
	private final Prefix prefix;
	
	DataUnit(Unit unit, Prefix prefix) {
		this.unit = unit;
		this.prefix = prefix;	
	}
	
	/**
	 * Returns the SI or IEC prefix for this unit as well as the Bit or Byte suffix. <br/>
	 * <pre>
	 * 	Example:
	 * 		DataUnit.MEBIBYTES.getSymbol()	returns "MiB"
	 * 		DataUnit.MEGABITS.getSymbol() 	returns "Mb"
	 * </pre>
	 * @return	String symbol
	 */
	public String getSymbol() {
		return (prefix.getPrefix() + unit.getSuffix());
	}
	
	/**
	 * Converts fixed-point <code>value</code> from DataUnit <code>from</code> to <code>this</code> DataUnit.
	 * @see #convertFrom(BigDecimal, DataUnit)
	 * @param value	Value to convert
	 * @param from 	<code>value<code>'s DataUnit
	 * @return		BigInteger representing the converted value
	 */
	public BigInteger convertFrom(BigInteger value, DataUnit from) {
		return convert(value, from, this);
	}

	/**
	 * Converts floating-point <code>value</code> from DataUnit <code>from</code> to <code>this</code> DataUnit.
	 * @see #convertFrom(BigInteger, DataUnit)
	 * @param value	Value to convert
	 * @param from 	<code>value<code>'s DataUnit
	 * @return		BigDecimal representing the converted value
	 */
	public BigDecimal convertFrom(BigDecimal value, DataUnit from) {
		return convert(value, from, this);
	}
	
	/**
	 * Converts fixed-point <code>value</code> from <code>this</code> DataUnit to <code>to</code> DataUnit.
	 * @see #convertTo(BigDecimal, DataUnit)
	 * @param value	Value to convert
	 * @param to 	DataUnit to convert <code>value<code> to
	 * @return		BigInteger representing the converted value
	 */
	public BigInteger convertTo(BigInteger value, DataUnit to) {
		return convert(value, this, to);
	}
	
	/**
	 * Converts floating-point <code>value</code> from <code>this</code> DataUnit to <code>to</code> DataUnit.
	 * @see #convertTo(BigInteger, DataUnit)
	 * @param value	Value to convert
	 * @param to 	DataUnit to convert <code>value<code> to
	 * @return		BigDecimal representing the converted value
	 */
	public BigDecimal convertTo(BigDecimal value, DataUnit to) {
		return convert(value, this, to);
	}
	
	/**
	 * Converts a fixed-point value from one unit to another.
	 * @param value	Value to convert
	 * @param from	<code>value</code>'s current unit
	 * @param to	DataUnit to convert <code>value<code> to
	 * @return		BigInteger representing the converted value
	 */
	static public BigInteger convert(BigInteger value, DataUnit from, DataUnit to) {
		BigInteger fromMultiplier = from.getMultiplier();
		BigInteger toMultiplier = to.getMultiplier();
		
		return value.multiply(fromMultiplier.divide(toMultiplier));
	}
	
	/**
	 * Converts a floating-point value from one unit to another.
	 * @param value	Value to convert
	 * @param from	<code>value</code>'s current unit
	 * @param to	DataUnit to convert <code>value<code> to
	 * @return		BigDecimal representing the converted value
	 */
	static public BigDecimal convert(BigDecimal value, DataUnit from, DataUnit to) {
		BigDecimal fromMultiplier = new BigDecimal(from.getMultiplier());
		BigDecimal toMultiplier = new BigDecimal(to.getMultiplier());
		
		return value.multiply(fromMultiplier.divide(toMultiplier));
	}
	
	private BigInteger getMultiplier() {
		return unit.multiple().multiply(prefix.getMultiple());
	}
	
	private enum Unit {
		BIT("b", BigInteger.valueOf(1)), BYTE("B", BigInteger.valueOf(8));
		
		private final String symbol;
		private final BigInteger multiple;
		
		Unit(String symbol, BigInteger multiple) {
			this.symbol = symbol;
			this.multiple = multiple;
		}
		
		private BigInteger multiple() {
			return multiple;
		}
		
		private String getSuffix() {
			return symbol;
		}
	}
	
	private enum Prefix {
		NONE(	"",	 BigInteger.valueOf(1)),
		KILO(	"k", BigInteger.valueOf(1000)),			KIBI("Ki", BigInteger.valueOf(1024)), 
		MEGA(	"M", BigInteger.valueOf(1000).pow(2)),	MEBI("Mi", BigInteger.valueOf(1024).pow(2)), 
		GIGA(	"G", BigInteger.valueOf(1000).pow(3)),	GIBI("Gi", BigInteger.valueOf(1024).pow(3)), 
		TERA(	"T", BigInteger.valueOf(1000).pow(4)),	TEBI("Ti", BigInteger.valueOf(1024).pow(4)), 
		PETA(	"P", BigInteger.valueOf(1000).pow(5)),	PEBI("Pi", BigInteger.valueOf(1024).pow(5)), 
		EXA(	"E", BigInteger.valueOf(1000).pow(6)),	EXBI("Ei", BigInteger.valueOf(1024).pow(6)), 
		ZETTA(	"Z", BigInteger.valueOf(1000).pow(7)),	ZEBI("Zi", BigInteger.valueOf(1024).pow(7)), 
		YOTTA(	"Y", BigInteger.valueOf(1000).pow(8)),	YOBI("Yi", BigInteger.valueOf(1024).pow(8));
		
		private final String prefix;
		private final BigInteger multiple;
		
		Prefix(String prefix, BigInteger multiple) {
			this.prefix = prefix;
			this.multiple = multiple;
		}
		
		private String getPrefix() {
			return prefix;
		}
		
		private BigInteger getMultiple() {
			return multiple;
		}
	}
}

