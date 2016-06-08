package com.n4systems.model.dashboard;

import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.parents.AbstractEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_definitions")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
