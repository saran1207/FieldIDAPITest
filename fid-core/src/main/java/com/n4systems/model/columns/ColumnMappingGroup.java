package com.n4systems.model.columns;

import com.n4systems.model.parents.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "column_mapping_groups")
public class ColumnMappingGroup extends AbstractEntity {

    @Column(name="label")
    private String label;

    @OneToMany(mappedBy = "group")
    private Collection<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();

    @Column(name="report_type")
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(name="group_key")
    private String groupKey;

    @Column(name="ordervalue")
    private Integer order;

    public Collection<ColumnMapping> getColumnMappings() {
        return columnMappings;
    }

    public void setColumnMappings(Collection<ColumnMapping> columnMappings) {
        this.columnMappings = columnMappings;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
