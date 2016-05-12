package com.n4systems.model;

import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="select_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SelectCriteria extends Criteria {

	@Column(name="selectoption", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="select_criteria_options", joinColumns = @JoinColumn(name="select_criteria_id"))
	@OrderColumn(name="orderidx")
    @Localized
	@org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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