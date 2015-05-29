package com.n4systems.exporting.io;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkArgument;

public class ExcelMapWriter implements MapWriter {
	private final String dateFormat;
    private final TimeZone timeZone;
	private WritableWorkbook workbook;
	private ExcelSheetManager excelSheetManager;
	private String[] titles;
	private int currentRow = 0;
	
	public ExcelMapWriter(OutputStream out, String dateFormat, TimeZone timeZone) throws IOException {
		this.dateFormat = dateFormat;
        this.timeZone = timeZone;
		workbook = Workbook.createWorkbook(out);		
		excelSheetManager = new ExcelSheetManager();  // default..override if need be.
	}
	
	@Override
	public void write(Map<String, Object> row) throws IOException {
		try {
			writeTitleLine(row);
			writeRow(row);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
	
	private final void resetManagers() {
		excelSheetManager.initialize(workbook);
		excelSheetManager.setDateFormat(dateFormat);
        excelSheetManager.setTimeZone(timeZone);
	}

	@Override
	public void close() throws IOException {
		if (workbook != null) {
			try {
				autoSizeColumns();				
				workbook.write();
				workbook.close();
				excelSheetManager.close();
			} catch (WriteException e) {
				throw new IOException(e);
			}
			workbook = null;
		}
	}

	private void writeTitleLine(Map<String, Object> rowMap) throws RowsExceededException, WriteException   {
		if (titles != null) {
			return;
		}
		titles = rowMap.keySet().toArray(new String[0]);
		
		// We'll push the titles into a map so we can reuse the writeRow logic
		Map<String,Object> titleMap = new LinkedHashMap<String,Object>();
		for (String title : titles) {
			titleMap.put(title, title);
		}
		writeRow(titleMap);
	}
	
	private void writeRow(Map<String, Object> rowMap) throws RowsExceededException, WriteException {
		resetManagers();		
		for (int col = 0; col < titles.length; col++) {
			excelSheetManager.addCell(currentRow, titles, col, rowMap);			
		}
		currentRow++;
	}
	
	private final void autoSizeColumns() {
		CellView view = new CellView();
		view.setAutosize(true);
		
		for (WritableSheet sheet:workbook.getSheets()) { 
			for (int col = 0; col < sheet.getColumns(); col++) {
				sheet.setColumnView(col, view);
			}
		}
	}
	
	public ExcelMapWriter withExcelSheetManager(ExcelSheetManager manager) { 
		checkArgument(manager!=null);
		this.excelSheetManager = manager;
		return this;
	}
}
