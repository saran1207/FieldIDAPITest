package com.n4systems.model;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.parents.EntityWithTenant;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class
 * @author aaitken
 *
 */
@Entity
@Table( name="autoattributedefinition")
public class AutoAttributeDefinition extends EntityWithTenant implements Exportable {

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
	
	
	public List<InfoOptionBean> getSanitizedOutputs() {
		List<InfoOptionBean> sanatizedOutputs = new ArrayList<InfoOptionBean>();
		for (InfoFieldBean field : criteria.getOutputs()) {
			sanatizedOutputs.add(getDefinintionOutputFor(field));
		}
		return sanatizedOutputs;
	}
	
	private InfoOptionBean getDefinintionOutputFor(InfoFieldBean infoField) {
		InfoOptionBean result = findOutputOptionFor(infoField);
		
		return (result != null) ? result : InfoOptionBean.createBlankInfoOption(infoField);
	}
	
	private InfoOptionBean findOutputOptionFor(InfoFieldBean infoField) {
		for (InfoOptionBean infoOption : outputs) {
			if (infoOption.getInfoField().equals(infoField)) {
				return infoOption;
			}
		}
		return null;
	}
	
	@Override
	public String getGlobalId() {
		// Auto Attributes are exportable but are never edited
		return null;
	}
	
	@Override
	public void setGlobalId(String globalId) {}
	
}
