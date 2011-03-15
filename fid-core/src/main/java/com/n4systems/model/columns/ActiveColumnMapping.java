package com.n4systems.model.columns;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="active_column_mappings")
public class ActiveColumnMapping extends EntityWithTenant implements Comparable<ActiveColumnMapping> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "column_layout_id")
    private ColumnLayout columnLayout;

    @ManyToOne
    @JoinColumn(name="mapping_id")
    private ColumnMapping mapping;

    @Column(name="ordervalue")
    private Integer order;

    public ColumnMapping getMapping() {
        return mapping;
    }

    public void setMapping(ColumnMapping mapping) {
        this.mapping = mapping;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ColumnLayout getColumnLayout() {
        return columnLayout;
    }

    public void setColumnLayout(ColumnLayout columnLayout) {
        this.columnLayout = columnLayout;
    }

    @Override
    public int compareTo(ActiveColumnMapping other) {
        return order.compareTo(other.order);
    }
}
