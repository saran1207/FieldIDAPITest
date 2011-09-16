package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
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
			ensureSheetsExist();
			writeTitleLine(row);
			writeRow(row);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
	
	private final void ensureSheetsExist() {
		if (workbook.getSheets().length==0) { 
			excelSheetManager.createSheets(workbook);
		}
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
		ExcelCellManager cellManager = new ExcelCellManager(workbook);
		for (int col = 0; col < titles.length; col++) {
			// refactor this into sheetManager.addCell which contains a cellManager.
			WritableSheet sheet = excelSheetManager.getSheetForColumn(titles[col]);
			if (sheet!=null) {
				WritableCell cell = cellManager.getCell(currentRow,sheet, titles, col, rowMap.get(titles[col]));
				sheet.addCell(cell);
			}
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
		this.excelSheetManager = manager;
		return this;
	}
	
	
	
	
	class ExcelCellManager {

		private Map<String,AtomicInteger> sheetColumns = new HashMap<String,AtomicInteger>();

		public ExcelCellManager(WritableWorkbook workbook) { 
			for (WritableSheet sheet: workbook.getSheets()) {
				sheetColumns.put(sheet.getName(), new AtomicInteger(0));
			}				
		}
		
		public WritableCell getCell(int row, WritableSheet sheet, String[] titles, int titleIndex, Object value) {
			WritableCell cell;
			
			Integer col = sheetColumns.get(sheet.getName()).getAndIncrement();
			
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
			
//			WritableCellFeatures writableCellFeatures = new WritableCellFeatures();
//			writableCellFeatures.setComment("("+row+","+col+")");
//			cell.setCellFeatures(writableCellFeatures);
			return cell;
		}
		
	}

	
}
