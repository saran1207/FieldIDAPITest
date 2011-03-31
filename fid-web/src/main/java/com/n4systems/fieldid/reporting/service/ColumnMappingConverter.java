package com.n4systems.fieldid.reporting.service;

import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.model.columns.ColumnMapping;

public class ColumnMappingConverter {

    public ColumnMappingView convert(ColumnMapping mapping) {
        String id = mapping.getName();
        if (id == null) {
            id = mapping.getGroup().getGroupKey() + mapping.getLabel();
        }
        return new ColumnMappingView(id, mapping.getLabel(), mapping.getPathExpression(), mapping.getSortExpression(), mapping.getOutputHandler(), mapping.isSortable(), true, mapping.getDefaultOrder(), null, mapping.getGroup().getGroupKey(), mapping.getId(), mapping.getJoinExpression());
    }

}
