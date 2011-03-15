package com.n4systems.model.columns;

import com.n4systems.model.Tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="custom_column_mappings")
@PrimaryKeyJoinColumn(name="column_id")
public class CustomColumnMapping extends ColumnMapping {

    @Column(name="report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private CustomColumnCategory category;

    @ManyToOne
    @JoinColumn(name="tenant_id")
    private Tenant tenant;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public CustomColumnCategory getCategory() {
        return category;
    }

    public void setCategory(CustomColumnCategory category) {
        this.category = category;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}
