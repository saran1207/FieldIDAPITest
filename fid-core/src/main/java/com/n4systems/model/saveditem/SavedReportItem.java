package com.n4systems.model.saveditem;

import com.n4systems.model.search.EventReportCriteria;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("R")
public class SavedReportItem extends SavedItem<EventReportCriteria> {

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="report_id")
    private EventReportCriteria searchCriteria;

    public SavedReportItem() {}
    public SavedReportItem(EventReportCriteria criteria) {
        this.searchCriteria = criteria;
    }

    public SavedReportItem(EventReportCriteria eventReportCriteria, SavedItem<EventReportCriteria> savedItem) {
        this(eventReportCriteria);
        if (savedItem!=null) {
            setId(savedItem.getId());
            setName(savedItem.getName());
        }
    }

    @Override
	public EventReportCriteria getSearchCriteria() {
        return searchCriteria;
    }

    @Override
	public void setSearchCriteria(EventReportCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.report";
    }

}
