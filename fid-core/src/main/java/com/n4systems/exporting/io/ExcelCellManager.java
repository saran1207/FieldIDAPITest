package com.n4systems.exporting.io;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

class ExcelCellManager {

	private Map<String,AtomicInteger> sheetColumns = new HashMap<String,AtomicInteger>();
	private String dateFormat = "mm/dd/yy";

	public ExcelCellManager() { 
	}

	public void initialize(WritableWorkbook workbook) {
		for (WritableSheet sheet: workbook.getSheets()) {
			sheetColumns.put(sheet.getName(), new AtomicInteger(0));
		}
	}
			
	public WritableCell getCell(int row, WritableSheet sheet, String[] titles, int titleIndex, Map<String, Object> rowMap) {
		WritableCell cell;
		Object value = rowMap.get(titles[titleIndex]);
		
		Integer col = getCurrentColumnForSheet(sheet.getName());
						
		if (value == null) {
			cell = new Label(col, row, "");
		} else if (value instanceof Number) {
			cell = new jxl.write.Number(col, row, ((Number)value).doubleValue());
		} else if (value instanceof Boolean) {
			cell = new jxl.write.Boolean(col, row, (Boolean)value);
		} else if (value instanceof Date) {
			cell = new DateTime(col, row, (Date)value, new WritableCellFormat(new DateFormat(dateFormat)), DateTime.GMT);			
		} else if (value instanceof Number) {
			cell = new jxl.write.Number(col, row, ((Number)value).doubleValue());
		} else {
			cell = new Label(col, row, value.toString());
		}		
		
		return cell;
	}

	private Integer getCurrentColumnForSheet(String sheetName) {
		return 	sheetColumns.get(sheetName).getAndIncrement();
	}

	public void setDateFormat(String dateFormat) { 
		this.dateFormat = dateFormat;
	}
	
}

