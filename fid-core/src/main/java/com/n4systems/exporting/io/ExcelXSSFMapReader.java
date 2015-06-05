package com.n4systems.exporting.io;

import com.n4systems.util.DateHelper;
import com.n4systems.util.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * This MapReader reads the "new" (as of... what... 2006?) Excel .xlsx formatted spreadsheets for
 *
 * Created by Jordan Heath on 2015-05-28.
 */
public class ExcelXSSFMapReader implements MapReader {

    private final TimeZone timeZone;
    private XSSFWorkbook workbook;
    private Sheet sheet;
    private String[] titles;
    private int currentRow = 0;

    public ExcelXSSFMapReader(InputStream in, TimeZone timeZone) throws IOException {
        this.timeZone = timeZone;

        this.workbook = new XSSFWorkbook(in);
        this.sheet = workbook.getSheetAt(0);

        if(sheet.getPhysicalNumberOfRows() < 2) {
            throw new EmptyDocumentException();
        }
    }


    @Override
    public String[] getTitles() throws IOException, ParseException {
        if(titles == null) {
            Row row = sheet.getRow(0);

            List<String> titleList = new ArrayList<>();

            row.cellIterator().forEachRemaining(cell -> titleList.add(cell.getStringCellValue()));

            titles = titleList.toArray(new String[titleList.size()]);
        }

        return titles;
    }

    @Override
    public Map<String, Object> readMap() throws IOException, ParseException {
        //If the titles haven't been read yet, just send back a null or something...
        if(getTitles() == null) {
            //Seriously, if you're trying to read a row before you grabbed the titles, you're bananas!!
            return null;
        }

        return mapNextRow();
    }

    private Map<String, Object> mapNextRow() {
        //Is there a particular reason not to use iterators here???
        Object[] row = readNextRow();
        if(row == null) {
            //If it's null, you hit the end.
            return null;
        }

        //Mash the two arrays (titles and row) into one map.
        return MapUtils.combineArrays(titles, row);
    }

    @Override
    public int getCurrentRow() {
        return currentRow;
    }

    @Override
    public void close() throws IOException {
        if(workbook != null) {
            sheet = null;
            workbook.close();
            workbook = null;
        }
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    private Object[] readNextRow() {
        if(atLastRow()) {
            return null;
        }

        //Did you see what I did there??
        Row row = sheet.getRow(++currentRow);

        //With .xlsx files, things get a little bit weird when a user edits them.  Deleting a cell removes it from
        //the spreadsheet in such a way that referencing a cell at that position isn't valid.  Similarly, if you try
        //to use an Iterator to iterate through the cells, those "deleted" cells are completely missing.
        //To get around this, you can use the Iterator like normal, but you need to use the Column Index baked into
        //the cell to determine which position in the Array to write the value to.

        //We're going to send back an array, so let's create one of the same size as the Titles array... since we
        //have a title for every single column, this is a RELIABLE method of determining actual width.
        Object[] values = new Object[titles.length];

        //Now crack into that iterator, so we can map up all the cells we KNOW about...
        row.cellIterator().forEachRemaining(cell -> {
            //...but just to be safe, we're going to make sure those cells are from the range we expect.  Anything
            //outside of that range, we will consider garbage.  Cthulhu only knows how it ended up there.
            if(cell != null && cell.getColumnIndex() < titles.length) {
                values[cell.getColumnIndex()] = readCellValue(cell);
            }
        });

        return values;
    }


    public Object readCellValue(Cell cell) {
        //I'm not sure whether it's caused by poor construction of the spreadsheet (possible) or whether it's because
        //of the cell being completely nulled out if the user hits "DELETE"... but sometimes it looks like a completely
        //null cell will pop up... one that doesn't even get considered as part of the "physical cells" of the
        //spreadsheet.  To compensate for this, we treat these null cells as a null value and jump out of the method
        //early.
        if(cell == null) return null;

        //POI is more advanced than JXL... so there are many possible cell data types...
        //Good news is, for everything else there's MasterCard.  Uh... no, that's not right.  For everything
        //else, there's a predictable series of CellType values.  We'll use those now.
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                //However, Numeric cells are weird.  They could be a number... or they could be a date.
                if(DateUtil.isCellDateFormatted(cell)) {
                    //Looks like it's a date, so we'll delocalize the date and drop it into the list.
                    return DateHelper.delocalizeDate(cell.getDateCellValue(), timeZone);
                } else {
                    //It's not a Date, so it MUST be another Numeric value.
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                //If it's anything else, we should also treat this value as null.
                return null;
        }
    }

    private boolean atLastRow() {
        return (currentRow == (sheet.getPhysicalNumberOfRows() - 1));
    }
}
