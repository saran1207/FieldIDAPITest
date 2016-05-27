package com.n4systems.model.dashboard;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dashboard_layouts")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DashboardLayout extends EntityWithTenant {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dashboard_layouts_dashboard_columns",
                    joinColumns = @JoinColumn(name = "dashboard_layout_id"),
                    inverseJoinColumns = @JoinColumn(name = "dashboard_column_id"))
    @OrderColumn(name="orderIdx")
    @org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<DashboardColumn> columns = new ArrayList<DashboardColumn>();

    @ManyToOne(fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private Boolean selected;

    public List<DashboardColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DashboardColumn> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Transient
    public int getWidgetCount() {
        int widgetCount = 0;
        for (DashboardColumn column : columns) {
            widgetCount += column.getWidgets().size();
        }
        return widgetCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
