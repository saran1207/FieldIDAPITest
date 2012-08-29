package com.n4systems.model;

import com.n4systems.model.parents.EntityWithTenant;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "criteriaresults")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CriteriaResult extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, optional=false)
	protected Criteria criteria;
	
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

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="criteriaresults_actions", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "criteriaresult_id"))
    @IndexColumn(name="orderidx")
    private List<Event> actions = new ArrayList<Event>();
	
	public CriteriaResult() {}
	
	public CriteriaResult(Tenant tenant, Criteria criteria) {
		super(tenant);
		this.criteria = criteria;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
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
    
    public abstract String getResultString();

	@Override
	public String toString() {
		return "CriteriaResult [criteria=" + criteria + ", deficiencies="
				+ deficiencies + ", recommendations="
				+ recommendations + "]";
	}

    public List<Event> getActions() {
        return actions;
    }

    public void setActions(List<Event> actions) {
        this.actions = actions;
    }
}
