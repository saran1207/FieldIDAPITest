package com.n4systems.model.saveditem;

import com.n4systems.model.search.AssetSearchCriteria;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("S")
public class SavedSearchItem extends SavedItem<AssetSearchCriteria> {

    private static final String DEFAULT_NAME = "New Search";

    public SavedSearchItem() {}

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="search_id")
    private AssetSearchCriteria searchCriteria;

    public SavedSearchItem(AssetSearchCriteria criteria) {
    	this.searchCriteria = criteria;
        setName(DEFAULT_NAME);
    }

    public SavedSearchItem(AssetSearchCriteria searchCriteria, SavedSearchItem savedSearchItem) {
        this(searchCriteria);
        if (savedSearchItem!=null) { 
            setId(savedSearchItem.getId());
            setName(savedSearchItem.getName());
        }
    }

    @Override
	public AssetSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    @Override
	public void setSearchCriteria(AssetSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String getTitleLabelKey() {
        return "label.search";
    }

}
