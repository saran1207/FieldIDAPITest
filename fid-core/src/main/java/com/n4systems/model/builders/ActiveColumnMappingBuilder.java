package com.n4systems.model.builders;

import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ColumnMapping;

public class ActiveColumnMappingBuilder extends EntityWithTenantBuilder<ActiveColumnMapping> {

    private ColumnLayout columnLayout;
    private ColumnMapping columnMapping;
    private Integer order;

    public static ActiveColumnMappingBuilder anActiveColumnMapping() {
        return new ActiveColumnMappingBuilder(null, null, null);
    }

    public ActiveColumnMappingBuilder(ColumnMapping columnMapping, Integer order, ColumnLayout columnLayout) {
        this.columnMapping = columnMapping;
        this.order = order;
        this.columnLayout = columnLayout;
    }

    public ActiveColumnMappingBuilder order(Integer order) {
        return makeBuilder(new ActiveColumnMappingBuilder(columnMapping, order, columnLayout));
    }

    public ActiveColumnMappingBuilder columnMapping(ColumnMapping columnMapping) {
        return makeBuilder(new ActiveColumnMappingBuilder(columnMapping, order, columnLayout));
    }

    public ActiveColumnMappingBuilder columnLayout(ColumnLayout columnLayout) {
        return makeBuilder(new ActiveColumnMappingBuilder(columnMapping, order, columnLayout));
    }

    @Override
    public ActiveColumnMapping createObject() {
        ActiveColumnMapping activeColumnMapping = super.assignAbstractFields(new ActiveColumnMapping());
        activeColumnMapping.setOrder(order);
        activeColumnMapping.setMapping(columnMapping);
        activeColumnMapping.setColumnLayout(columnLayout);
        return activeColumnMapping;
    }

}
