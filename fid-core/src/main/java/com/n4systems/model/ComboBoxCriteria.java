package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="combobox_criteria")
@PrimaryKeyJoinColumn(name="id")
public class ComboBoxCriteria extends Criteria {
	
	@Column(name="selectoption", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="combobox_criteria_options", joinColumns = @JoinColumn(name="combobox_criteria_id"))
	@IndexColumn(name="orderidx")	
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