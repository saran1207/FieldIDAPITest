package com.n4systems.model.dashboard;

import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_definitions")
public class WidgetDefinition extends AbstractEntity {

    @Column(name="widget_type")
    @Enumerated(EnumType.STRING)
    private WidgetType widgetType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="config_id")
    private WidgetConfiguration config;

    public WidgetDefinition() {}

    public WidgetDefinition(WidgetType widgetType) {
        this.widgetType = widgetType;
        this.config = widgetType.createConfiguration();
    }

    public WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    public WidgetConfiguration getConfig() {
        return config;
    }

    public void setConfig(WidgetConfiguration config) {
        this.config = config;
    }
}
