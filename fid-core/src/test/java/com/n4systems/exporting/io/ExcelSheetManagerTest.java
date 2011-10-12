package com.n4systems.exporting.io;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.junit.Test;

public class ExcelSheetManagerTest {	
	
	@Test
	public void test_write() throws IOException, RowsExceededException, WriteException {
		String[] titles = {"Sheet1", "Sheet2"};
		WritableCell cell = new Label(0,0,"hello");
		ExcelSheetManager fixture = new ExcelSheetManager(titles);
		ExcelCellManager excelCellManager = createMock(ExcelCellManager.class);
		
		Map<String,Object> rowMap = new HashMap<String,Object>();
		
		WritableSheet sheet1 = createMock(WritableSheet.class);
		WritableSheet sheet2 = createMock(WritableSheet.class);
		WritableSheet[] sheets = {sheet1,sheet2};
		sheet1.addCell(cell);
		replay(sheet1);
		replay(sheet2);
		
		WritableWorkbook workbook = createMock(WritableWorkbook.class);
		expect(workbook.createSheet(titles[0], 0)).andReturn(sheet1);
		expect(workbook.createSheet(titles[1], 1)).andReturn(sheet2);
		replay(workbook);
		
		fixture.setExcelCellManager(excelCellManager);
		excelCellManager.initialize(workbook);
		expect(excelCellManager.getCell(0, sheet1, titles, 0, rowMap)).andReturn(cell);
		replay(excelCellManager);
		
		fixture.initialize(workbook);		
		fixture.addCell(0, titles, 0, rowMap);	
		
		verify(workbook);
	}
}
