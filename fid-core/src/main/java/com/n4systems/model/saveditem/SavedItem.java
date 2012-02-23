package com.n4systems.model.saveditem;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.search.SearchCriteriaModel;
import com.n4systems.model.security.SecurityDefiner;

@SuppressWarnings("serial")
@Entity
@Table(name="saved_items")
@DiscriminatorColumn(name = "type")
public abstract class SavedItem<T extends SearchCriteriaModel> extends EntityWithTenant implements NamedEntity {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SavedItem.class);
	}

    public abstract T getSearchCriteria();
    public abstract void setSearchCriteria(T searchCriteria);

	@Column(nullable=false, length=255)
	private String name;
	private String sharedByName;
	@Column(length=1000)
	private String description;

    @Override
	public String getName() {
        return name;
    }

    @Override
	public void setName(String name) {
        this.name = name;
    }

    public String getSharedByName() {
        return sharedByName;
    }

    public void setSharedByName(String sharedByName) {
        this.sharedByName = sharedByName;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Transient
    public abstract String getTitleLabelKey();

}
