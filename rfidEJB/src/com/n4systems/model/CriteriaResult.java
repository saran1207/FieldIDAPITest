package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "criteriaresults")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CriteriaResult extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
	private Criteria criteria;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
	private State state;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
	private AbstractInspection inspection;
	
	@OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="criteriaresults_recommendations")
	@IndexColumn(name="orderidx")
	private List<Recommendation> recommendations = new ArrayList<Recommendation>();
	
	@OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="criteriaresults_deficiencies")
	@IndexColumn(name="orderidx")
	private List<Deficiency> deficiencies = new ArrayList<Deficiency>();
	
	public CriteriaResult() {}
	
	public CriteriaResult(Tenant tenant, Criteria criteria, State state) {
		super(tenant);
		this.criteria = criteria;
		this.state = state;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public AbstractInspection getInspection() {
		return inspection;
	}

	public void setInspection(AbstractInspection inspection) {
		this.inspection = inspection;
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
		return (criteria.isPrincipal()) ? state.getStatus() : null;
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
		
	    return "Result: " + getId() + ", " + getCriteria() + " = " + getState() + StringUtils.indent(recString, 1) + StringUtils.indent(defString, 1);
    }
	
}
