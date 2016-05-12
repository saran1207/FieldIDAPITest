package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "criteriasections")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CriteriaSection extends EntityWithTenant implements Listable<Long>, NamedEntity, Retirable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private @Localized String title;
	
	@Column(nullable=false)
	private boolean retired = false;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@OrderColumn(name="orderIdx")
    @JoinTable(name="criteriasections_criteria", joinColumns = @JoinColumn(name="criteriasections_id"), inverseJoinColumns = @JoinColumn(name="criteria_id"))
	@org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Criteria> criteria = new ArrayList<Criteria>();

    private boolean optional = false;

	private boolean required = false;

    @Transient
    private Long oldId;
	
	public CriteriaSection() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		title = (title != null) ? title.trim() : null;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Criteria> getCriteria() {
		return criteria;
	}
	
	public void setCriteria(List<Criteria> criteria) {
		this.criteria.clear();
		this.criteria.addAll(criteria);
	}

	public String getShortName() { 
		return getTitle().length() > 20 ? getTitle().substring(0, 20) + "..." : getTitle(); 
	}
	
	@Override
	public String getDisplayName() {
		return getTitle();
	}

	@Override
	public boolean isRetired() {
		return retired;
	}

	@Override
	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	public String getName() {
		return getTitle();
	}
	
	public void setName( String name ) {
		setTitle( name );
	}

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
		if (required) {
			criteria.stream()
					.filter(c -> !(c instanceof OneClickCriteria) && !(c instanceof ObservationCountCriteria))
					.forEach(c -> c.setRequired(required));
		}
	}

	@Override
	public String toString() {
		return "CriteriaSection [" + title + "]";
	}

    @Transient
    public List<Criteria> getAvailableCriteria() {
        List<Criteria> availableCriteria = new ArrayList<Criteria>();
        for (Criteria criteria : getCriteria()) {
            if (!criteria.isRetired()) {
                availableCriteria.add(criteria);
            }
        }
        return availableCriteria;
    }

	public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }
}
