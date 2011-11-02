package com.n4systems.model.dashboard;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.parents.AbstractEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_definitions")
public class WidgetDefinition<W extends WidgetConfiguration> extends AbstractEntity {

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

    @SuppressWarnings("unchecked")
    public W getConfig() {
        return (W) config;
    }

    public void setConfig(W config) {
        this.config = config;
    }
}
