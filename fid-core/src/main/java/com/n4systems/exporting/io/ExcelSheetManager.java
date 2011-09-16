package com.n4systems.exporting.io;

import static com.google.common.base.Preconditions.*;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

public class ExcelSheetManager {
	private static final Logger logger = Logger.getLogger(ExcelSheetManager.class);
	
	private WritableWorkbook workbook;
	
	private String[] sheetTitles = {"Sheet1"};

	public ExcelSheetManager() {
	}
	
	public ExcelSheetManager(String...sheetTitles) { 
		this.sheetTitles = sheetTitles;
	}
	
	void createSheets(WritableWorkbook workbook) {
		this.workbook = workbook;		
		int i = 0;
		for (String sheet:getSheetTitles()) { 
			workbook.createSheet(sheet, i++);
		} 
	}
	
	protected WritableSheet getSheetForColumn(String columnTitle) {
		checkArgument(workbook.getSheets().length>0, "you must have at least one sheet in your workbook.");
		if (workbook.getSheets().length>1) { 		
			logger.warn("since you have multiple sheets in your excel spreadsheet, you probably want to override getSheet() to control which cells go on which sheet otherwise everything will end up on the first sheet.");
		}
		return workbook.getSheets()[0];		// just return the first one by default.
	}
		

	public WritableSheet getSheetByName(String sheetName) {		
		return workbook.getSheet(sheetName);
	}
	
	protected String[] getSheetTitles() { 
		return sheetTitles;
	}
	
	void close() {
		workbook = null;
	}

}

