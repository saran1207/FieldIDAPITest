package com.n4systems.util.views;

public class TableViewExcelHandler {
	
	private final ExcelOutputHandler[] cellHandlers;
	
	public TableViewExcelHandler(ExcelOutputHandler[] cellHandlers) {
		this.cellHandlers = cellHandlers;
	}

	public void handle(TableView table) {
		for (int row = 0; row < table.getRowSize(); row++) {
			for (int col = 0; col < table.getColumnSize(); col++) {
				handleCell(table, row, col);
			}
		}
	}

	private void handleCell(TableView table, int row, int col) {
		Object convertedValue = cellHandlers[col].handleExcel(table.getId(row), table.getCell(row, col));

		table.setCell(row, col, convertedValue);
	}
	
}
