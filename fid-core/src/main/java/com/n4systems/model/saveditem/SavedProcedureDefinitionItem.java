package com.n4systems.model.saveditem;

import com.n4systems.model.search.ProcedureDefinitionCriteria;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("D")
public class SavedProcedureDefinitionItem extends SavedItem<ProcedureDefinitionCriteria> {

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="procedure_defs_id")
    private ProcedureDefinitionCriteria searchCriteria;

    public SavedProcedureDefinitionItem() {}

    public SavedProcedureDefinitionItem(ProcedureDefinitionCriteria criteria) {
        this.searchCriteria = criteria;
    }

    public SavedProcedureDefinitionItem(ProcedureDefinitionCriteria criteria, SavedItem<ProcedureDefinitionCriteria> savedItem) {
        this(criteria);
        if (savedItem != null) {
            setId(savedItem.getId());
            setName(savedItem.getName());
        }
    }

    @Override
    public ProcedureDefinitionCriteria getSearchCriteria() {
        return searchCriteria;
    }

    @Override
    public void setSearchCriteria(ProcedureDefinitionCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.procedures";
    }
}
