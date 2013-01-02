package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.IndexColumn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "criteria")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Criteria extends EntityWithTenant implements Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String displayText;
	
	@Column(nullable=false)
	private boolean retired = false;

    @Column(nullable = true)
    private String instructions;
	
	@Column(name="text", nullable=false, length=511)
	@ElementCollection(fetch= FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<String> recommendations = new ArrayList<String>();
	
	@Column(name="text", nullable=false, length=511)
	@ElementCollection(fetch= FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<String> deficiencies = new ArrayList<String>();
	
	public Criteria() {}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	public String getDisplayName() {
		return getDisplayText();
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public abstract CriteriaType getCriteriaType();

    @Transient
    public boolean hasNonEmptyInstructions() {
        if (instructions == null) {
            return false;
        }
        Document doc = Jsoup.parseBodyFragment(instructions);
        return !StringUtils.isBlank(doc.text());
    }

}
