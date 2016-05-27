package com.n4systems.model;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import rfid.ejb.entity.InfoFieldBean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * This defines the set of input info fields and output info fields for an asset type.
 * These fields are used to help fill in values when tagging an asset in all user interfaces.
 * @author aaitken
 *
 */
@Entity
@Table ( name="autoattributecriteria" )
@Localized(ignore = true)   // for now, we are currently not supporting this localization.
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutoAttributeCriteria extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@ManyToMany (targetEntity = InfoFieldBean.class, fetch = FetchType.LAZY)
	@JoinTable (name = "autoattributecriteria_inputinfofield",
					joinColumns = @JoinColumn(name = "r_autoattributecriteria", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infofield", referencedColumnName = "uniqueid"))
	@OrderBy( "uniqueID" )
	@org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<InfoFieldBean> inputs = new ArrayList<InfoFieldBean>();;
	
	@ManyToMany (targetEntity = InfoFieldBean.class,  fetch = FetchType.LAZY)
	@JoinTable (name = "autoattributecriteria_outputinfofield",
					joinColumns = @JoinColumn(name = "r_autoattributecriteria", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_infofield", referencedColumnName = "uniqueid"))
	@OrderBy( "uniqueID" )
	@org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<InfoFieldBean> outputs = new ArrayList<InfoFieldBean>();
	
	@OneToMany( targetEntity =AutoAttributeDefinition.class, fetch = FetchType.LAZY, mappedBy="criteria", cascade = CascadeType.ALL )
	@org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<AutoAttributeDefinition> definitions;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY )
	@JoinColumn(name = "r_producttype")
	private AssetType assetType;
	
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
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}
	
}
