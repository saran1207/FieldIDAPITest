package com.n4systems.model;

import com.n4systems.persistence.localization.Localized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="combobox_criteria")
@PrimaryKeyJoinColumn(name="id")
public class ComboBoxCriteria extends Criteria {
	
	@Column(name="selectoption", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="combobox_criteria_options", joinColumns = @JoinColumn(name="combobox_criteria_id"))
	@OrderColumn(name="orderidx")
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
        return CriteriaType.COMBO_BOX;
    }

}