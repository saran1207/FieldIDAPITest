package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;
/**
 * This defines the set of input info fields and output info fields for a product type.
 * These fields are used to help fill in values when tagging a product in all user interfaces.
 * @author aaitken
 *
 */
@Entity
@Table ( name="autoattributecriteria" )
public class AutoAttributeCriteria extends EntityWithTenant implements FilteredEntity {
	private static final long serialVersionUID = 1L;
	
	@ManyToMany (targetEntity = InfoFieldBean.class, fetch = FetchType.LAZY)
	@JoinTable (name = "autoattributecriteria_inputinfofield",
					joinColumns = @JoinColumn(name = "r_autoattributecriteria", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infofield", referencedColumnName = "uniqueid"))
	@OrderBy( "uniqueID" )
	private List<InfoFieldBean> inputs = new ArrayList<InfoFieldBean>();;
	
	@ManyToMany (targetEntity = InfoFieldBean.class,  fetch = FetchType.LAZY)
	@JoinTable (name = "autoattributecriteria_outputinfofield",
					joinColumns = @JoinColumn(name = "r_autoattributecriteria", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infofield", referencedColumnName = "uniqueid"))
	@OrderBy( "uniqueID" )
	private List<InfoFieldBean> outputs = new ArrayList<InfoFieldBean>();
	
	@OneToMany( targetEntity =AutoAttributeDefinition.class, fetch = FetchType.LAZY, mappedBy="criteria", cascade = CascadeType.ALL )
	private List<AutoAttributeDefinition> definitions;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY )
	@JoinColumn(name = "r_producttype")
	private ProductType productType;
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD);
	}
	
	public List<InfoFieldBean> getInputs() {
		return inputs;
	}
	
	public void setInputs(List<InfoFieldBean> inputs) {
		this.inputs = inputs;
	}
	
	public List<InfoFieldBean> getOutputs() {
		return outputs;
	}
	
	public void setOutputs(List<InfoFieldBean> outputs) {
		this.outputs = outputs;
	}
	
	public List<AutoAttributeDefinition> getDefinitions() {
		return definitions;
	}
	
	public void setDefinitions(List<AutoAttributeDefinition> definitions) {
		this.definitions = definitions;
	}
	
	public ProductType getProductType() {
		return productType;
	}
	
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
}
