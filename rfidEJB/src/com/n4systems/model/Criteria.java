package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "criteria")
public class Criteria extends EntityWithTenant implements Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String displayText;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH}, optional=false)
	private StateSet states;
	
	private boolean principal;

	@Column(nullable=false)
	private boolean retired = false;
	
	@Column(name="text", nullable=false, length=511)
	@CollectionOfElements(fetch= FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<String> recommendations = new ArrayList<String>();
	
	@Column(name="text", nullable=false, length=511)
	@CollectionOfElements(fetch= FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<String> deficiencies = new ArrayList<String>();
	
	public Criteria() {}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public StateSet getStates() {
		return states;
	}

	public void setStates(StateSet states) {
		this.states = states;
	}
	
	public String getDisplayName() {
		return getDisplayText();
	}
	
	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	public List<String> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<String> recomendations) {
		this.recommendations = recomendations;
	}

	public List<String> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<String> deficiencies) {
		this.deficiencies = deficiencies;
	}

	@Override
	public int hashCode() {
		/*
		 * Since Critera is used as a Map Key in some places, we're using the id of the object 
		 * If the id is null (such as if the object had not been persisted yet) we default to the super's implementation
		 */
		return (id == null) ? super.hashCode() : id.hashCode();
	}

	@Override
    public String toString() {
	    return getDisplayText() + " (" + getId() + ")";
    }
		
}
