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


    /*
            Interface Methods...
     */
    @Override
    public String[] getTitles() throws IOException, ParseException {
        if(titles == null) {
            Row row = sheet.getRow(0);

            titles = new String[row.getPhysicalNumberOfCells()];

            for(int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                titles[i] = String.valueOf(row.getCell(i).getStringCellValue());
            }
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

    private Object[] readNextRow() {
        if(atLastRow()) {
            return null;
        }

        //Did you see what I did there??
        Row row = sheet.getRow(++currentRow);
        List<Object> values = new ArrayList<>();

        row.iterator().forEachRemaining(cell -> {
            //POI is more advanced than JXL... so there are many possible cell data types...
            //Good news is, for everything else there's MasterCard.  Uh... no, that's not right.  For everything
            //else, there's a predictable series of CellType values.  We'll use those now.
            switch(cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    values.add(null);
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    values.add(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    //However, Numeric cells are weird.  They could be a number... or they could be a date.
                    if(DateUtil.isCellDateFormatted(cell)) {
                        //Looks like it's a date, so we'll delocalize the date and drop it into the list.
                        values.add(DateHelper.delocalizeDate(cell.getDateCellValue(), timeZone));
                    } else {
                        //It's not a Date, so it MUST be another Numeric value.
                        values.add(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    values.add(cell.getStringCellValue());
                    break;
                default:
                    break;
            }
        });

        //I think we're done now.  Onwards and upwards!!!
        return values.toArray();
    }

    private boolean atLastRow() {
        return (currentRow == (sheet.getPhysicalNumberOfRows() - 1));
    }
}
