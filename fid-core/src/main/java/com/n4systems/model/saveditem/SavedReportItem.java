package com.n4systems.model.saveditem;

import com.n4systems.model.search.EventReportCriteriaModel;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("R")
public class SavedReportItem extends SavedItem {

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="report_id")
    private EventReportCriteriaModel report;

    public EventReportCriteriaModel getReport() {
        return report;
    }

    public void setReport(EventReportCriteriaModel report) {
        this.report = report;
    }

}
