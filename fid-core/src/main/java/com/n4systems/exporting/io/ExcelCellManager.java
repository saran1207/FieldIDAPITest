package com.n4systems.exporting.io;

import jxl.write.*;

import java.lang.Boolean;
import java.lang.Number;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class ExcelCellManager {

	private Map<String,AtomicInteger> sheetColumns = new HashMap<String,AtomicInteger>();
    private Map<String, WritableCellFormat> dateCellFormats = new HashMap<String, WritableCellFormat>();
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
			cell = new DateTime(col, row, (Date)value, getDateCellFormat(dateFormat), DateTime.GMT);
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

    private WritableCellFormat getDateCellFormat(String dateFormat) {
        if (dateCellFormats.get(dateFormat) == null) {
            dateCellFormats.put(dateFormat, new WritableCellFormat(new DateFormat(dateFormat)));
        }
        return dateCellFormats.get(dateFormat);
    }
	
}

