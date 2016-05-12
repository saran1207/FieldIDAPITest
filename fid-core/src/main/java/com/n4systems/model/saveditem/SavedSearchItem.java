package com.n4systems.model.saveditem;

import com.n4systems.model.search.AssetSearchCriteria;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("S")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SavedSearchItem extends SavedItem<AssetSearchCriteria> {

    public SavedSearchItem() {}

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="search_id")
    private AssetSearchCriteria searchCriteria;

    public SavedSearchItem(AssetSearchCriteria criteria) {
    	this.searchCriteria = criteria;
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
