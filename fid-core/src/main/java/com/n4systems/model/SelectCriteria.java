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
@Table(name="select_criteria")
@PrimaryKeyJoinColumn(name="id")
public class SelectCriteria extends Criteria {
	
	@Column(name="selectoption", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="select_criteria_options", joinColumns = @JoinColumn(name="select_criteria_id"))
	@IndexColumn(name="orderidx")	
	private List<String> options = new ArrayList<String>();

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public List<String> getOptions() {
		return options;
	}
	
    @Override
    public String getTypeDescription() {
        return "Select";
    }
}