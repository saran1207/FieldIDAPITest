package com.n4systems.model.saveditem;

import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name="saved_items")
@DiscriminatorColumn(name = "type")
public abstract class SavedItem<T extends SearchCriteria> extends EntityWithTenant implements NamedEntity {

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
    
    @OneToMany(mappedBy = "savedItem", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SendSavedItemSchedule> sendSchedules = new ArrayList<SendSavedItemSchedule>();

    public SavedItem() {
        super();
    }

    public SavedItem(SavedItem<T> savedItem) {
        super();
        setDescription(savedItem.getDescription());
        setName(savedItem.getName());
        setSharedByName(savedItem.getSharedByName());
        setTenant(savedItem.getTenant());
    }
    
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
    
    public List<SendSavedItemSchedule> getSendSchedules() {
        return sendSchedules;
    }

    public void setSendSchedules(List<SendSavedItemSchedule> sendSchedules) {
        this.sendSchedules = sendSchedules;
    }

    @Transient
    public abstract String getTitleLabelKey();
    
    @Override
    public void reset() {
        super.reset();
    }

    public <X extends SavedItem<?>> X copy(X savedItem) {
        savedItem.setName(getName());
        savedItem.setDescription(getDescription());
        savedItem.setSharedByName(getSharedByName());
        savedItem.setTenant(getTenant());
        return savedItem;
    }
}
