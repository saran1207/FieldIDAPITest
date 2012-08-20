package com.n4systems.util;

import com.n4systems.model.utils.NonConvertingDateTime;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.views.TableView;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelBuilder {
	private static final String EXCEL_DATE_FORMAT  = "m/d/yy";
	private static final String EXCEL_DATE_TIME_FORMAT = "m/d/yy h:mm";
	private static final short CELL_OUTER_BORDER = HSSFCellStyle.BORDER_MEDIUM;
	private static final short CELL_INNER_BORDER = HSSFCellStyle.BORDER_THIN;
	
	public static final int DEFAULT =	(1 << 0);
	public static final int DATE =		(1 << 1);
	public static final int TITLE =		(1 << 2);
	public static final int LEFT =		(1 << 3);
	public static final int RIGHT =		(1 << 4);
	public static final int TOP =		(1 << 5);
	public static final int BOTTOM =	(1 << 6);
	public static final int DATE_TIME =	(1 << 7);
	
	private HSSFWorkbook book = new HSSFWorkbook();
	private HSSFFont titleFont;
	
	// this acts as a cache of cell styles so we don't go over our 32k limit
	private Map<Integer, HSSFCellStyle> cellStyleMap = new HashMap<Integer, HSSFCellStyle>();
	
	private final DateTimeDefinition dateTimeDefinition;
	
	public ExcelBuilder(DateTimeDefinition dateTimeDefinition) {
		// setup title font
		titleFont = book.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		this.dateTimeDefinition = dateTimeDefinition;
	}
	
	private HSSFCellStyle getCellStyle(int options) {
		// if we already have a style for these options
		if(cellStyleMap.containsKey(options)){
			return cellStyleMap.get(options);
		}
		
		//we don't have a style for this property set, we need to create one
		BitField bits = new BitField(options);
		
		HSSFCellStyle style = book.createCellStyle();

		// sets the default, everything else is an override
		style.setBorderTop(CELL_INNER_BORDER);
		style.setBorderBottom(CELL_INNER_BORDER);
		style.setBorderLeft(CELL_INNER_BORDER);
		style.setBorderRight(CELL_INNER_BORDER);
		
		if(bits.isSet(DATE_TIME)) {
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat(EXCEL_DATE_TIME_FORMAT));
		} 
		if(bits.isSet(DATE)) {
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat(EXCEL_DATE_FORMAT));
		}
		
		if(bits.isSet(TITLE)) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setBorderTop(CELL_OUTER_BORDER);
			style.setBorderBottom(CELL_OUTER_BORDER);
			
			// bold the titles
	        style.setFont(titleFont);
		}
		
		if(bits.isSet(LEFT)) {
			style.setBorderLeft(CELL_OUTER_BORDER);
		}
		
		if(bits.isSet(RIGHT)) {
			style.setBorderRight(CELL_OUTER_BORDER);
		}
		
		if(bits.isSet(TOP)) {
			style.setBorderTop(CELL_OUTER_BORDER);
		}
		
		if(bits.isSet(BOTTOM)) {
			style.setBorderBottom(CELL_OUTER_BORDER);
		}
		
		cellStyleMap.put(options, style);
		
		return style;
	}
	
	public HSSFSheet getSheet(String name) {
		return book.getSheet(name);
	}
	
	public void createSheet(String name) {
		book.createSheet(name);
	}
	
	public void createSheet(String name, List<String> titles, TableView table) {
		createSheet(name);
		setSheetTitles(name, titles);
		addSheetData(name, table);
	}
	
	public void setSheetTitles(String name, List<String> titles) {
		HSSFSheet sheet = getSheet(name);
		
		// we need to check if rows have already been defined
		if(sheet.getPhysicalNumberOfRows() > 0) {
			// if we have rows already defined, then we need to shift them down one
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
		}
		
        // add in the titles
		HSSFCell cell;
		HSSFRow row = sheet.createRow(0);
		for(short col = 0; col < titles.size(); col++ ) {
			cell = row.createCell(col);
			cell.setCellValue(new HSSFRichTextString(titles.get(col)));
			
			if(col == 0) {
				// left side
				cell.setCellStyle(getCellStyle(TITLE|LEFT));
			} else if(col == (titles.size() - 1)) {
				//right side
				cell.setCellStyle(getCellStyle(TITLE|RIGHT));
			} else {
				cell.setCellStyle(getCellStyle(TITLE));
			}
		}
		
		// freeze the title row
		sheet.createFreezePane( 0, 1, 0, 1 );
	}
	
	public void addSheetData(String name, TableView table) {
		HSSFSheet sheet = getSheet(name);
		HSSFRow row;
		
		// start the rows after the last row
		short firstRow = (short)(sheet.getLastRowNum() + 1);
		short maxColumns = 0;
		int styleOptions;
		
		for(short rowId = 0; rowId < table.getRowSize(); rowId++) {
			styleOptions = DEFAULT;
			
			// create a new row in the sheet
			row = sheet.createRow((short)(rowId + firstRow));
			for(short colId = 0; colId < table.getColumnSize(); colId++) {
				
				// set top bottom borders
				if(rowId == 0) {
					// top
					styleOptions = styleOptions|TOP;
				} else if(rowId == (table.getRowSize() - 1)) {
					// bottom
					styleOptions = styleOptions|BOTTOM;
				}
				
				//set left right borders
				if(colId == 0) {
					// left
					styleOptions = styleOptions|LEFT;
				} else if(colId == (table.getColumnSize() -1)) {
					// right
					styleOptions = styleOptions|RIGHT;
				}
				
				
				// create a cell and set the value
				setupCell(row.createCell(colId), table.getCell(rowId, colId), styleOptions);
			}
			
			// we need to track the max number of columns so we know how many to autosize later
			if((table.getColumnSize() - 1) > maxColumns) {
				maxColumns = (short)(table.getColumnSize() - 1);
			}
		}
		
		// now auto size all the columns
		for(short i = 0; i <= maxColumns; i ++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	private void setupCell(HSSFCell cell, Object value, int styleOptions) {

		cell.setCellStyle(getCellStyle(styleOptions));
		
		// this sets the value of the cell based on the Objects type
		if(value == null) {
			// for nulls just create an empty text cell
			cell.setCellValue(new HSSFRichTextString());
			
		} else if(value instanceof Number) {
			// POI only accepts double value numbers so we convert any number we have to a double
			cell.setCellValue(((Number)value).doubleValue());
			
		} else if(value instanceof PlainDate) {
			cell.setCellValue((Date)value);
			
			// override the default style for dates
			cell.setCellStyle(getCellStyle(styleOptions|DATE));
		} else if(value instanceof Date) {

            if (value instanceof NonConvertingDateTime) {
                cell.setCellValue((Date)value);
            } else {
                cell.setCellValue(DateHelper.convertToUserTimeZone((Date)value, dateTimeDefinition.getTimeZone()));
            }

			// override the default style for dates
			cell.setCellStyle(getCellStyle(styleOptions|DATE_TIME));
		    
		} else if(value instanceof Boolean) {
			
			cell.setCellValue((Boolean)value);
			
		} else if(value instanceof String) {
			
			cell.setCellValue(new HSSFRichTextString((String)value));
			
		} else {
			// default to a toString call
			cell.setCellValue(new HSSFRichTextString(value.toString()));
		}

	}
	
	public void writeToFile(File file) throws IOException {
		FileOutputStream bookOut = null;
		try {
			// write the book
			bookOut = new FileOutputStream(file);
			book.write(bookOut);
		} finally {
			IOUtils.closeQuietly(bookOut);
		}
	}
}
