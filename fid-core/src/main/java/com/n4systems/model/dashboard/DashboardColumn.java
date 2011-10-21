package com.n4systems.model.dashboard;

import com.n4systems.model.parents.AbstractEntity;
import org.hibernate.annotations.IndexColumn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_columns")
public class DashboardColumn extends AbstractEntity {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dashboard_columns_widget_definitions",
                    joinColumns = @JoinColumn(name = "dashboard_column_id"),
                    inverseJoinColumns = @JoinColumn(name = "widget_definition_id"))
    @IndexColumn(name="orderIdx")
    private List<WidgetDefinition> widgets = new ArrayList<WidgetDefinition>();

    public List<WidgetDefinition> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<WidgetDefinition> widgets) {
        this.widgets = widgets;
    }

}
