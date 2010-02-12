package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.n4systems.util.MapUtils;

public class ExcelMapReader implements MapReader {
	private Workbook workbook;
	private Sheet sheet;
	private String[] titles;
	private int currentRow = -1;
	
	public ExcelMapReader(InputStream in) throws BiffException, IOException {
		workbook = Workbook.getWorkbook(in);
		sheet = workbook.getSheet(0);
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
			titles = readNextRow();
		}
		return titles;
	}

	@Override
	public Map<String, String> readMap() throws IOException, ParseException {
		// ensure titles have been read
		if (getTitles() == null) {
			return null;
		}
		
		Map<String, String> rowMap = mapNextRow();
		return rowMap;
	}
	
	private Map<String, String> mapNextRow() {
		String[] row = readNextRow();
		if (row == null) {
			return null;
		}
		
		// map the titles to the row values
		Map<String, String> rowMap = MapUtils.combineArrays(titles, row);
		return rowMap;
	}
	
	private String[] readNextRow() {
		if (atLastRow()) {
			return null;
		}
		
		currentRow++;
		
		Cell[] cells = sheet.getRow(currentRow);
		String[] values = new String[cells.length];
		
		for (int i = 0; i < cells.length; i++) {
			values[i] = cells[i].getContents();
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
