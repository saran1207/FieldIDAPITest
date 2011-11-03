package com.n4systems.model.dashboard.widget;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.orgs.BaseOrg;

@Entity
@Table(name = "widget_configurations_event_kpi")
@PrimaryKeyJoinColumn(name="id")
public class EventKPIWidgetConfiguration extends WidgetConfiguration {

    @OneToMany
    @JoinTable(name = "widget_configurations_event_kpi_orgs",
                    joinColumns = @JoinColumn(name = "config_id"),
                    inverseJoinColumns = @JoinColumn(name = "org_id"))
    @IndexColumn(name="orderIdx")
    private List<BaseOrg> orgs = new ArrayList<BaseOrg>();

    public List<BaseOrg> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<BaseOrg> orgs) {
        this.orgs = orgs;
    }

    @Override
    public EventKPIWidgetConfiguration copy() {
        EventKPIWidgetConfiguration copy = (EventKPIWidgetConfiguration) super.copy();
        copy.setOrgs(new ArrayList<BaseOrg>(orgs));
        return copy;
    }

}
