package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.parents.EntityWithTenant;

/**
 * This class
 * @author aaitken
 *
 */
@Entity
@Table( name="autoattributedefinition")
public class AutoAttributeDefinition extends EntityWithTenant {

	private static final long serialVersionUID = 1L;
	
	@ManyToMany (targetEntity = InfoOptionBean.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable (name = "autoattributedefinition_inputinfooption",
					joinColumns = @JoinColumn(name = "r_autoattributedefinition", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	@OrderBy( "infoField" )
	List<InfoOptionBean> inputs = new ArrayList<InfoOptionBean>();
	
	@ManyToMany (targetEntity = InfoOptionBean.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable (name = "autoattributedefinition_outputinfooption",
					joinColumns = @JoinColumn(name = "r_autoattributedefinition", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	@OrderBy( "uniqueID" )
	List<InfoOptionBean> outputs = new ArrayList<InfoOptionBean>();
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "r_autoattributecriteria")
	AutoAttributeCriteria criteria;
	
	public List<InfoOptionBean> getInputs() {
		return inputs;
	}
	public void setInputs(List<InfoOptionBean> inputs) {
		this.inputs = inputs;
	}
	public List<InfoOptionBean> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<InfoOptionBean> outputs) {
		this.outputs = outputs;
	}
	public AutoAttributeCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(AutoAttributeCriteria criteria) {
		this.criteria = criteria;
	}
	
	
}
