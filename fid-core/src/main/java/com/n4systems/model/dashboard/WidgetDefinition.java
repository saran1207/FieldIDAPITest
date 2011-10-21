package com.n4systems.model.dashboard;

import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_definitions")
public class WidgetDefinition extends AbstractEntity {

    @Column(name="widget_type")
    @Enumerated(EnumType.STRING)
    private WidgetType widgetType;

    @Column(name="name")
    private String name;

    public WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
