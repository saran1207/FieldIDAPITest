package com.n4systems.exporting.io;

import static com.google.common.base.Preconditions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.n4systems.util.MapUtils;

public class ExcelMapWriter implements MapWriter {
	private final String dateFormat;
	private WritableWorkbook workbook;
	private ExcelSheetManager excelSheetManager;
	private String[] titles;
	private int currentRow = 0;
	
	public ExcelMapWriter(OutputStream out, String dateFormat) throws IOException {
		this.dateFormat = dateFormat;
		workbook = Workbook.createWorkbook(out);		
		excelSheetManager = new ExcelSheetManager();  // default..override if need be.
	}
	
	@Override
	public void write(Map<String, ?> row) throws IOException {
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

	private void writeTitleLine(Map<String, ?> rowMap) throws RowsExceededException, WriteException   {
		if (titles != null) {
			return;
		}
		titles = rowMap.keySet().toArray(new String[0]);
		
		// We'll push the titles into a map so we can reuse the writeRow logic
		writeRow(MapUtils.fillMapKeysAndValues(titles));
	}
	
	private void writeRow(Map<String, ?> rowMap) throws RowsExceededException, WriteException {
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
