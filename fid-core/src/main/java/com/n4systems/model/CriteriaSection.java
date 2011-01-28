package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "criteriasections")
public class CriteriaSection extends EntityWithTenant implements Listable<Long>, NamedEntity, Retirable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String title;
	
	@Column(nullable=false)
	private boolean retired = false;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name="orderIdx")
	private List<Criteria> criteria = new ArrayList<Criteria>();
	
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
		this.criteria = criteria;
	}

	public String getDisplayName() {
		return getTitle();
	}

	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	public String getName() {
		return getTitle();
	}
	
	public void setName( String name ) {
		setTitle( name );
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
	
}
