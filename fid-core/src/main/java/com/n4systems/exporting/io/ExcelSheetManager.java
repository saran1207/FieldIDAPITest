package com.n4systems.exporting.io;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkArgument;

public class ExcelSheetManager {
	private static final Logger logger = Logger.getLogger(ExcelSheetManager.class);
	
	private String[] sheetTitles = {"Sheet1"};

	private ExcelCellManager excelCellManager = new ExcelCellManager();
	Map<String, WritableSheet> sheets = new HashMap<String, WritableSheet>();
	
	public ExcelSheetManager() {
		setExcelCellManager(new ExcelCellManager());
	}
	
	public ExcelSheetManager(String... sheetTitles) { 
		this.sheetTitles = sheetTitles;
	}
	
	final void initialize(WritableWorkbook workbook) {		
		if (CollectionUtils.isEmpty(sheets)) { 
			int i = 0;
			for (String sheet:getSheetTitles()) { 
				sheets.put(sheet, workbook.createSheet(sheet, i++));
			}
		}
		// do this AFTER you have added sheets!
		getExcelCellManager().initialize(workbook);
	}
	
	protected WritableSheet getSheetForColumn(String columnTitle) {
		checkArgument(!CollectionUtils.isEmpty(sheets), "you must have at least one sheet in your workbook.");
		if (sheets.size()>1) { 		
			logger.warn("since you have multiple sheets in your excel spreadsheet, you probably want to override getSheet() to control which cells go on which sheet otherwise everything will end up on the first sheet.");
		}

        return sheets.get(columnTitle);
	}
		

	final WritableSheet getSheetByName(String sheetName) {		
		return sheets.get(sheetName);
	}
	
	protected String[] getSheetTitles() { 
		return sheetTitles;
	}
	
	final void close() {
		sheets.clear();
	}

	public void addCell(int row, String[] titles, int col, Map<String, Object> rowMap) throws RowsExceededException, WriteException {
		WritableSheet sheet = getSheetForColumn(sheetTitles[0]);
		if (sheet!=null) { 
			sheet.addCell(getExcelCellManager().getCell(row, sheet, titles, col, rowMap));
		}		
	}

	public void setDateFormat(String dateFormat) {
		getExcelCellManager().setDateFormat(dateFormat);
	}

    public void setTimeZone(TimeZone timeZone) {
        getExcelCellManager().setTimeZone(timeZone);
    }

	protected void setExcelCellManager(ExcelCellManager excelCellManager) {
		this.excelCellManager = excelCellManager;
	}

	protected ExcelCellManager getExcelCellManager() {
		return excelCellManager;
	}

}

