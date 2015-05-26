package com.n4systems.util.excel;

import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.NonConvertingDateTime;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.views.TableView;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.BitField;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class uses Apache POI to generate .xlsx (new style) Excel Spreadsheets using the XSSFWorkbook.  This ensures
 * that the Excel Workbook is generated entirely in memory with no use of a temporary file on disk.
 *
 * It should be noted that - for extremely large spreadsheets - this can cause the memory footprint of the application
 * to skyrocket.  In testing of the library, it became apparent that a single spreadsheet of 10,000 rows of 20 columns
 * each can exceed a memory footprint of 800MB.  Garbage Collection should be sufficient to address this issue, but
 * under extreme stress of generating multiple massive spreadsheets for multiple clients, we may possibly experience
 * memory availability issues on the Application Server.
 *
 * This class is more or less a copy of {@link com.n4systems.util.ExcelBuilder} but has been modified to use the XSSF
 * library to allow for the new Excel formatting as well as to allow for efficient in-memory building.
 *
 * Created by Jordan Heath on 15-05-07.
 */
public class ExcelXSSFBuilder {
    private static final String EXCEL_DATE_FORMAT  = "m/d/yy";
    private static final String EXCEL_DATE_TIME_FORMAT = "m/d/yy h:mm";
    private static final short CELL_OUTER_BORDER = CellStyle.BORDER_MEDIUM;
    private static final short CELL_INNER_BORDER = CellStyle.BORDER_THIN;

    public static final int DEFAULT =	(1 << 0);
    public static final int DATE =		(1 << 1);
    public static final int TITLE =		(1 << 2);
    public static final int LEFT =		(1 << 3);
    public static final int RIGHT =		(1 << 4);
    public static final int TOP =		(1 << 5);
    public static final int BOTTOM =	(1 << 6);
    public static final int DATE_TIME =	(1 << 7);

    //Just a word of warning... if you start having memory problems, this is the first place to look.  XSSF spreadsheets
    //can get STUPID big... as much as an 800MB footprint for a sheet with 10,000 rows of 20 columns each.  You can
    //lower the size of this footprint by using SXSSFWorkbooks, but those require the use of multiple temp files and
    //result in a hell of a lot of disk I/O.  At the time of writing this, they also don't output text all that well.
    private XSSFWorkbook workbook;
    private Font titleFont;
    private DataFormat dataFormat;

    private Map<Integer, CellStyle> cellStyleMap = new HashMap<>();

    private final DateTimeDefiner dateTimeDefinition;

    /**
     * This is the primary constructor for the ExcelXSSFBuilder.  A DateTimeDefiner must be supplied for formatting
     * of dates and shifting to the appropriate timezone where applicable.
     *
     * @param dateTimeDefinition - A DateTimeDefiner which is used to format dates and apply expected timezones.
     */
    public ExcelXSSFBuilder(DateTimeDefiner dateTimeDefinition) {
        this.workbook = new XSSFWorkbook();
        this.dateTimeDefinition = dateTimeDefinition;
        titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        dataFormat = workbook.createDataFormat();
    }

    /**
     * This method uses the "options" byte from a cell to determine desired formatting.
     *
     * @param options - An Integer which will be parsed as a Byte for processing related styles.
     * @return A CellStyle indicative of the value of the "options" int.
     */
    private CellStyle getCellStyle(int options) {
        //Take a shortcut and send back the style if we already have it.
        if(cellStyleMap.containsKey(options)) {
            return cellStyleMap.get(options);
        }
        
        //Oh, this style is new. We should process and create it.
        BitField bits = new BitField(options);

        CellStyle style = workbook.createCellStyle();

        style.setBorderTop(CELL_INNER_BORDER);
        style.setBorderBottom(CELL_INNER_BORDER);
        style.setBorderLeft(CELL_INNER_BORDER);
        style.setBorderRight(CELL_INNER_BORDER);

        if(bits.isSet(DATE_TIME)) {
            style.setDataFormat(dataFormat.getFormat(EXCEL_DATE_TIME_FORMAT));
        }
        if(bits.isSet(DATE)) {
            style.setDataFormat(dataFormat.getFormat(EXCEL_DATE_FORMAT));
        }
        if(bits.isSet(TITLE)) {
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setBorderTop(CELL_OUTER_BORDER);
            style.setBorderBottom(CELL_OUTER_BORDER);

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

    /**
     * This method returns the Sheet object with a name matching that provided in the "name" parameter.
     *
     * @param name - A String representation of the name of the desired Workbook Sheet.
     * @return A Sheet representing the desired page from the Workbook.
     */
    public Sheet getSheet(String name) {
        return workbook.getSheet(name);
    }

    /**
     * This method creates a Sheet in the workbook with the name specified by the String parameter.
     *
     * @param name - A String value representing the name of the Sheet you wish to create in the Workbook.
     */
    public void createSheet(String name) {
        workbook.createSheet(name);
    }

    /**
     * This method creates a sheet with a table and titles specified, using the specified name.
     *
     * @param name - A String representing the desired name of the Workbook Sheet.
     * @param titles - A List populated with String representations of the desired Titles to be desplayed on the Sheet.
     * @param table - A TableView object representing the data to be written to the Sheet.
     */
    public void createSheet(String name, List<String> titles, TableView table) {
        createSheet(name);
        setSheetTitles(name, titles);
        addSheetData(name, table);
    }

    /**
     * This method adds a List of String titles to the Sheet from the Workbook with the specified name.
     *
     * @param name - A String representation of the name of the desired Workbook Sheet.
     * @param titles - A List populated with String representations of the desired titles on the Sheet.
     */
    public void setSheetTitles(String name, List<String> titles) {
        Sheet sheet = getSheet(name);

        // we need to check if rows have already been defined
        if(sheet.getPhysicalNumberOfRows() > 0) {
            // if we have rows already defined, then we need to shift them down one
            sheet.shiftRows(0, sheet.getLastRowNum(), 1);
        }

        // add in the titles
        Cell cell;
        Row row = sheet.createRow(0);
        for(short col = 0; col < titles.size(); col++ ) {
            cell = row.createCell(col);
            cell.setCellValue(new XSSFRichTextString(titles.get(col)));

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
        sheet.createFreezePane(0, 1, 0, 1);
    }

    /**
     * This method adds data from the specified TableView to the Workbook Sheet with the specified name.
     *
     * @param name - The name of the Workbook Sheet to add the TableView contents to.
     * @param table - A TableView object representing the data to be written to the Workbook Sheet.
     */
    public void addSheetData(String name, TableView table) {
        Sheet sheet = getSheet(name);
        Row row;

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

    /**
     * This method applies the necessary formatting to the specified Workbook Sheet Cell and adds the specified value
     * to that Cell.
     *
     * @param cell - A Cell object representing the cell to be configured and written to.
     * @param value - An Object representing the value to be written to the specified Cell.
     * @param styleOptions - An int value representing the style to be applied to the specified Cell.
     */
    private void setupCell(Cell cell, Object value, int styleOptions) {

        cell.setCellStyle(getCellStyle(styleOptions));

        // this sets the value of the cell based on the Objects type
        if(value == null) {
            // for nulls just create an empty text cell
            cell.setCellValue(new XSSFRichTextString());

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
                cell.setCellValue(DateHelper.convertToUserTimeZone((Date) value, dateTimeDefinition.getTimeZone()));
            }

            // override the default style for dates
            cell.setCellStyle(getCellStyle(styleOptions|DATE_TIME));

        } else if(value instanceof Boolean) {

            cell.setCellValue((Boolean)value);

        } else if(value instanceof String) {

            cell.setCellValue(new XSSFRichTextString((String)value));

        } else {
            // default to a toString call
            cell.setCellValue(new XSSFRichTextString(value.toString()));
        }

    }

    /**
     * This method writes the Workbook out to a file.  This shouldn't be used and is only here to support legacy stuff
     * that still uses a disk file.  That's why this is marked as deprecated.  Please don't use it.
     *
     * @param stream - A Stream to which you want to write the Excel Workbook.
     * @throws IOException This exception probably happened because the planets are improperly aligned.
     */
    public void writeToStream(OutputStream stream) throws IOException {
        try {
            workbook.write(stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
