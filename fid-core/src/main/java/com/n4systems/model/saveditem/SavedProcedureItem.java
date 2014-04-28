package com.n4systems.model.saveditem;

import com.n4systems.model.search.ProcedureCriteria;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("P")
public class SavedProcedureItem extends SavedItem<ProcedureCriteria> {

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="procedures_id")
    private ProcedureCriteria searchCriteria;

    public SavedProcedureItem() {}

    public SavedProcedureItem(ProcedureCriteria criteria) {
        this.searchCriteria = criteria;
    }

    public SavedProcedureItem(ProcedureCriteria criteria, SavedItem<ProcedureCriteria> savedItem) {
        this(criteria);
        if (savedItem != null) {
            setId(savedItem.getId());
            setName(savedItem.getName());
        }
    }

    @Override
    public ProcedureCriteria getSearchCriteria() {
        return searchCriteria;
    }

    @Override
    public void setSearchCriteria(ProcedureCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.procedures";
    }
}
