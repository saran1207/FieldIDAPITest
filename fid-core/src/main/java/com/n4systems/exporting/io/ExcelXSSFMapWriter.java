package com.n4systems.exporting.io;

import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.NonConvertingDateTime;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.BitField;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This MapWriter is built like the ExcelMapWriter to allow for it to be used in the same places.
 *
 * It differs in that it uses the Apache POI XSSF library.  This allows us to write the newer .xlsx Excel spreadsheets
 * and to write them with vastly more rows and columns.
 *
 * Because of the fact that this is one of two branches in the way that we handle Excel spreadsheets, this will end
 * up needing to be rewritten so that we only have one.  For more details see {@link com.n4systems.util.excel.ExcelXSSFBuilder}.
 *
 * Created by Jordan Heath on 2015-05-27.
 */
public class ExcelXSSFMapWriter implements MapWriter {

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

    private static final Logger logger = Logger.getLogger(ExcelXSSFMapWriter.class);

    private final DateTimeDefiner dateTimeDefiner;

    private XSSFWorkbook workbook;
    private Sheet sheet;

    private Font titleFont;
    private DataFormat dataFormat;

    private List<String> titles;


    private Map<Integer, CellStyle> cellStyleMap = new HashMap<>();

    public ExcelXSSFMapWriter(DateTimeDefiner dateTimeDefiner) {
        this.dateTimeDefiner = dateTimeDefiner; //This is used to format any dates that we happen across...
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Sheet 1");

        //Copying some other logic from ExcelXSSFBuilder...
        //FIXME We should only have one path of doing this.  Rewrite this and ExcelXSSFBuilder so that we only have one... they pretty much do the same thing.

        titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        dataFormat = workbook.createDataFormat();
    }

    /**
     * This method just dumps the Workbook into a Stream so that you can put that in a file or just chuck it right up to
     * S3.
     *
     * @param oStream - An OutputStream to write the Workbook to!
     * @throws IOException - If zombies have risen or the Maple Leafs are about to win the Stanly Cup, this method will throw IOExceptions.
     */
    public void writeToStream(OutputStream oStream) throws IOException {
        try {
            //We autosize like this at the end, because we want to make sure all of the work is finished first.
            for(int i = 0; i < titles.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(oStream);
        } finally {
            IOUtils.closeQuietly(oStream);
        }
    }




    /*
            Interface Methods...
     */
    @Override
    public void write(Map<String, Object> row) throws IOException {
        try {
            if(titles == null) {
                writeTitleLine(row);
            }
            writeRow(row);
        } catch (Exception e) {
            logger.error("Something really bad happened...", e);
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if(workbook != null) {
            workbook.close();
            workbook = null;
        }
    }



    /*
            Private Methods...
     */
    private void writeTitleLine(Map<String, Object> rowMap) {
        //Square peg won't fit in round hole... use hammer.  Square peg is now round... should fit in the hole.
        titles = rowMap.keySet().stream().collect(Collectors.toList());

        writeTitles();
    }

    private void writeTitles() {
        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());

        titles.forEach(title -> {
            Cell cell = row.createCell(row.getPhysicalNumberOfCells());
            cell.setCellValue(title);

            if(cell.getColumnIndex() == 0) {
                cell.setCellStyle(getCellStyle(TITLE|LEFT));
            } else if(cell.getColumnIndex() == (titles.size() - 1)) {
                cell.setCellStyle(getCellStyle(TITLE|RIGHT));
            } else {
                cell.setCellStyle(getCellStyle(TITLE));
            }
        });
    }

    private void writeRow(Map<String, Object> rowMap) {
        //Create a row... this handy-dandy method (getPhysicalNumberOfRows()) allows us to figure out how many rows
        //there are without externally tracking them...... which makes generating new rows super easy.
        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows()); //  +\- 1??

        //Now use .forEach and a Lambda to spit out the values into the sheet.  Null values get written as blanks, so
        //it's okay if we don't find a title value in the map.  It would, however, be problematic to find that we don't
        //get all of the titles in the first "row".
        titles.forEach(title -> fillCell(row.createCell(row.getPhysicalNumberOfCells()), rowMap.get(title)));
    }

    private void fillCell(Cell cell, Object value) {
        // this sets the value of the cell based on the Objects type
//        if(value == null) {
//            //For nulls, just do nothing... this is really odd...
//
//        } else
        if(value instanceof Number) {
            // POI only accepts double value numbers so we convert any number we have to a double
            cell.setCellValue(((Number)value).doubleValue());

        } else if(value instanceof PlainDate) {
            cell.setCellValue((Date)value);

            // override the default style for dates
            cell.setCellStyle(getCellStyle(DEFAULT|DATE));
        } else if(value instanceof Date) {

            if (value instanceof NonConvertingDateTime) {
                cell.setCellValue((Date)value);
            } else {
                cell.setCellValue(DateHelper.convertToUserTimeZone((Date) value, dateTimeDefiner.getTimeZone()));
            }

            // override the default style for dates
            cell.setCellStyle(getCellStyle(DEFAULT|DATE_TIME));

        } else if(value instanceof Boolean) {

            cell.setCellValue((Boolean)value);

        } else if(value instanceof String) {

            cell.setCellValue(new XSSFRichTextString((String)value));

        } else if (value != null) {
            // default to a toString call
            cell.setCellValue(new XSSFRichTextString(value.toString()));
        }
    }

    //TODO Put this in some kind of base class shared by anything that works with XSSF!!

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
}
