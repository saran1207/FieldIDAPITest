package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

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
	private WritableSheet sheet;
	private String[] titles;
	private int currentRow = 0;
	
	public ExcelMapWriter(OutputStream out, String dateFormat) throws IOException {
		this.dateFormat = dateFormat;
		workbook = Workbook.createWorkbook(out);
		sheet = workbook.createSheet("Sheet1", 0);
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
	
	@Override
	public void close() throws IOException {
		if (workbook != null) {
			try {
				autoSizeColumns();
				sheet = null;
				workbook.write();
				workbook.close();
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
		for (int col = 0; col < titles.length; col++) {
			sheet.addCell(getCell(currentRow, col, rowMap.get(titles[col])));
		}
		currentRow++;
	}
	
	private WritableCell getCell(int row, int col, Object value) {
		WritableCell cell;
		
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
	
	private void autoSizeColumns() {
		CellView view = new CellView();
		view.setAutosize(true);
		
		for (int col = 0; col < sheet.getColumns(); col++) {
			sheet.setColumnView(col, view);
		}
	}
}
