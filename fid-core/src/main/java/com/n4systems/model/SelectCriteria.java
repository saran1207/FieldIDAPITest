package com.n4systems.model;

import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="select_criteria")
@PrimaryKeyJoinColumn(name="id")
public class SelectCriteria extends Criteria {

	@Column(name="selectoption", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="select_criteria_options", joinColumns = @JoinColumn(name="select_criteria_id"))
	@IndexColumn(name="orderidx")
    @Localized
	private List<String> options = new ArrayList<String>();

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public List<String> getOptions() {
		return options;
	}
	
    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.SELECT;
    }
}