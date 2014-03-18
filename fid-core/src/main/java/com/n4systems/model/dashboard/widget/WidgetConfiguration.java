package com.n4systems.model.dashboard.widget;

import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations")
@Inheritance(strategy = InheritanceType.JOINED)
public class WidgetConfiguration extends AbstractEntity {

    @Column(name="name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WidgetConfiguration copy() {
        try {
            WidgetConfiguration widgetConfiguration = getClass().newInstance();
            widgetConfiguration.setId(getId());
            widgetConfiguration.setCreated(getCreated());
            widgetConfiguration.setCreatedBy(getCreatedBy());
            widgetConfiguration.setModified(getModified());
            widgetConfiguration.setModifiedBy(getModifiedBy());
            widgetConfiguration.setName(getName());
            return widgetConfiguration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
