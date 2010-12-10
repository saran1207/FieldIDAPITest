package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "criteriaresults")
@Inheritance(strategy = InheritanceType.JOINED)
public class CriteriaResult extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
	private OneClickCriteria criteria;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="event_id")
	private AbstractEvent event;
	
	@OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="criteriaresults_recommendations")
	@IndexColumn(name="orderidx")
	private List<Recommendation> recommendations = new ArrayList<Recommendation>();
	
	@OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="criteriaresults_deficiencies")
	@IndexColumn(name="orderidx")
	private List<Deficiency> deficiencies = new ArrayList<Deficiency>();
	
	public CriteriaResult() {}
	
	public CriteriaResult(Tenant tenant, OneClickCriteria criteria) {
		super(tenant);
		this.criteria = criteria;
	}

	public OneClickCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(OneClickCriteria criteria) {
		this.criteria = criteria;
	}

	public AbstractEvent getEvent() {
		return event;
	}

	public void setEvent(AbstractEvent event) {
		this.event = event;
	}
	
	public List<Recommendation> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<Recommendation> recomendations) {
		this.recommendations = recomendations;
	}

	public List<Deficiency> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<Deficiency> deficiencies) {
		this.deficiencies = deficiencies;
	}

	public Status getResult() {
		return null;
	}

	@Override
    public String toString() {
		String recString = new String();
		for (Observation observation: getRecommendations()) {
			recString += "\n" + observation;
		}
		
		String defString = new String();
		for (Observation observation: getDeficiencies()) {
			defString += "\n" + observation;
		}
		
	    return "Result: " + getId() + ", " + getCriteria() + " = " + StringUtils.indent(recString, 1) + StringUtils.indent(defString, 1);
    }
	
}
