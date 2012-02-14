package com.n4systems.model.saveditem;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.n4systems.model.search.EventReportCriteriaModel;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("R")
public class SavedReportItem extends SavedItem<EventReportCriteriaModel> {

    public SavedReportItem() {}
    public SavedReportItem(EventReportCriteriaModel criteria) {
        this.searchCriteria = criteria;
    }

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="report_id")
    private EventReportCriteriaModel searchCriteria;

    @Override
	public EventReportCriteriaModel getSearchCriteria() {
        return searchCriteria;
    }

    @Override
	public void setSearchCriteria(EventReportCriteriaModel searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.report";
    }
}
