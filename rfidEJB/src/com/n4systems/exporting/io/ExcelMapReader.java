package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.n4systems.util.MapUtils;

public class ExcelMapReader implements MapReader {
	private Workbook workbook;
	private Sheet sheet;
	private String[] titles;
	private int currentRow = -1;
	
	public ExcelMapReader(InputStream in) throws IOException {
		try {
			workbook = Workbook.getWorkbook(in);
		} catch (BiffException e) {
			throw new IOException(e);
		}
		sheet = workbook.getSheet(0);
		
		// we need at least 2 rows.  One for titles, and one data row
		if (sheet.getRows() < 2) {
			throw new EmptyDocumentException();
		}
	}
	
	@Override
	public void close() throws IOException {
		if (workbook != null) {
			sheet = null;
			workbook.close();
			workbook = null;
		}
	}

	@Override
	public String[] getTitles() throws IOException, ParseException {
		if (titles == null) {
			Object[] titleRow = readNextRow();
			titles = new String[titleRow.length];
			
			for (int i = 0; i < titleRow.length; i++) {
				// these should always be strings, but we'll parse it this way for safety
				titles[i] = String.valueOf(titleRow[i]);
			}
		}
		return titles;
	}

	@Override
	public Map<String, Object> readMap() throws IOException, ParseException {
		// ensure titles have been read
		if (getTitles() == null) {
			return null;
		}
		
		Map<String, Object> rowMap = mapNextRow();
		return rowMap;
	}
	
	private Map<String, Object> mapNextRow() {
		Object[] row = readNextRow();
		if (row == null) {
			return null;
		}
		
		// map the titles to the row values
		Map<String, Object> rowMap = MapUtils.combineArrays(titles, row);
		return rowMap;
	}
	
	private Object[] readNextRow() {
		if (atLastRow()) {
			return null;
		}
		
		currentRow++;
		
		Cell[] cells = sheet.getRow(currentRow);
		Object[] values = new Object[cells.length];
		
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DateCell) {
				values[i] = ((DateCell)cells[i]).getDate();
			} else {
				/*
				 * For now, all other field types will come back as Strings.  At some point 
				 * we will probably need to add handling for Number/Boolean types.
				 */
				values[i] = cells[i].getContents();
			}
		}
		return values;
	}
	
	private boolean atLastRow() {
		return (currentRow == (sheet.getRows() - 1));
	}

	@Override
	public int getCurrentRow() {
		return currentRow;
	}


}
