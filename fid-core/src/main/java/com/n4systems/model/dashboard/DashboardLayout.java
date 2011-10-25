package com.n4systems.model.dashboard;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import org.hibernate.annotations.IndexColumn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "dashboard_layouts")
public class DashboardLayout extends EntityWithTenant {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dashboard_layouts_dashboard_columns",
                    joinColumns = @JoinColumn(name = "dashboard_layout_id"),
                    inverseJoinColumns = @JoinColumn(name = "dashboard_column_id"))
    @IndexColumn(name="orderIdx")
    private List<DashboardColumn> columns = new ArrayList<DashboardColumn>();

    @ManyToOne(fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public List<DashboardColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DashboardColumn> columns) {
        this.columns = columns;
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

}
