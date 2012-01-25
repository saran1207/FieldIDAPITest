package com.n4systems.model.saveditem;

import com.n4systems.model.search.AssetSearchCriteriaModel;

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
@DiscriminatorValue("S")
public class SavedSearchItem extends SavedItem<AssetSearchCriteriaModel> {

    public SavedSearchItem() {}
    public SavedSearchItem(AssetSearchCriteriaModel criteria) {
        this.searchCriteria = criteria;
    }

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="search_id")
    private AssetSearchCriteriaModel searchCriteria;

    public AssetSearchCriteriaModel getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(AssetSearchCriteriaModel searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.search";
    }

}
