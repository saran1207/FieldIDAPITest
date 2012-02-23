package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteriaModel;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.views.TableView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ResultTransformerFactory {

    public ResultTransformer<TableView> createResultTransformer(SearchCriteriaModel searchCriteria) {
		List<String> columns = new ArrayList<String>();
		for(ColumnMappingView mapping: searchCriteria.getSortedStaticAndDynamicColumns()) {
			if (mapping != null) {
				columns.add(mapping.getPathExpression());
			}
		}

		ResultTransformer<TableView> transformer = null;
		try {
			transformer = new TableViewTransformer("id", columns);
		} catch (ParseException e) {
            throw new RuntimeException(e);
		}

		return transformer;
    }

}
