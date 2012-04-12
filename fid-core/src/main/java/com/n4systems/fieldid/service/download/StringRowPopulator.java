package com.n4systems.fieldid.service.download;

import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.views.RowView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringRowPopulator {

    public static void populateRowsWithConvertedStrings(Collection<RowView> rows, SearchCriteria searchCriteria, TableGenerationContext exportContextProvider) {
        for (RowView row : rows) {
            populateRowWithConvertedStrings(row, searchCriteria, exportContextProvider);
        }
    }

    public static void populateRowWithConvertedStrings(RowView row, SearchCriteria searchCriteria, TableGenerationContext exportContextProvider) {
        CellHandlerFactory cellHandlerFactory = new CellHandlerFactory(exportContextProvider);
        List<String> rowValues = new ArrayList<String>();
        int index = 0;
        for (ColumnMappingView column : searchCriteria.getSortedStaticAndDynamicColumns()) {
            WebOutputHandler handler = cellHandlerFactory.getHandler(column.getOutputHandler());
            String cellValue = handler.handleWeb(row.getId(), row.getValues().get(index));
            rowValues.add(cellValue);
            index++;
        }
        row.setStringValues(rowValues);
    }

}
