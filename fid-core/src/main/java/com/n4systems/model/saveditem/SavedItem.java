package com.n4systems.model.saveditem;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="saved_items")
@DiscriminatorColumn(name = "type")
public abstract class SavedItem extends EntityWithTenant {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SavedItem.class);
	}

	@Column(nullable=false, length=255)
	private String name;
	private String sharedByName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSharedByName() {
        return sharedByName;
    }

    public void setSharedByName(String sharedByName) {
        this.sharedByName = sharedByName;
    }
}
