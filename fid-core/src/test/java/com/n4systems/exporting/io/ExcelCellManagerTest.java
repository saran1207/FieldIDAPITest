package com.n4systems.exporting.io;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.junit.Test;

public class ExcelCellManagerTest {	
	
	@Test
	public void test_get_cell() throws IOException, RowsExceededException, WriteException {
		String[] titles = {"aNumber", "aDate", "aString", "aBoolean","anObject"};
		Date today = new Date();
		
		ExcelCellManager fixture = new ExcelCellManager();
		Map<String,Object> rowMap = new HashMap<String,Object>();
		rowMap.put(titles[0], new Double(100));
		rowMap.put(titles[1], today);
		rowMap.put(titles[2], "String");
		rowMap.put(titles[3], new Boolean(true));
		rowMap.put(titles[4], new TestCellObject("hello"));
		
		WritableSheet sheet1 = createMock(WritableSheet.class);
		WritableSheet sheet2 = createMock(WritableSheet.class);
		WritableSheet[] sheets = {sheet1,sheet2};
		expect(sheet1.getName()).andReturn(titles[0]).times(6);
		expect(sheet2.getName()).andReturn(titles[1]).times(1);
		replay(sheet1);
		replay(sheet2);
		
		WritableWorkbook workbook = createMock(WritableWorkbook.class);
		expect(workbook.getSheets()).andReturn(sheets);
		replay(workbook);
		
		fixture.initialize(workbook);
		WritableCell cell = fixture.getCell(0, sheet1, titles, 0, rowMap);
		assertEquals(0, cell.getColumn());
		assertEquals(jxl.write.Number.class, cell.getClass());
		assertEquals(100.0, ((jxl.write.Number)cell).getValue());
		
		cell = fixture.getCell(0, sheet1, titles, 1, rowMap);
		assertEquals(1, cell.getColumn());
		assertEquals(DateTime.class, cell.getClass());
		assertEquals(today, ((DateTime)cell).getDate());
		
		cell = fixture.getCell(0, sheet1, titles, 2, rowMap);
		assertEquals(2, cell.getColumn());
		assertEquals(Label.class, cell.getClass());
		assertEquals("String", ((Label)cell).getString());
		
		cell = fixture.getCell(0, sheet1, titles, 3, rowMap);
		assertEquals(3, cell.getColumn());
		assertEquals(jxl.write.Boolean.class, cell.getClass());
		assertEquals(true, ((jxl.write.Boolean)cell).getValue());
		
		cell = fixture.getCell(0, sheet1, titles, 4, rowMap);
		assertEquals(4, cell.getColumn());
		assertEquals(Label.class, cell.getClass());
		assertEquals("TESThello", ((Label)cell).getString());
		
		verify(sheet1, sheet2, workbook);
	}
	
	class TestCellObject {	
		private String label;

		public TestCellObject(String label) { 
			this.label = label;
		}
		@Override
		public String toString() { 
			return "TEST"+label;
		}
	}
	
}
