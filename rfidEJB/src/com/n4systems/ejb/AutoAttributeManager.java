package com.n4systems.ejb;

import java.util.Collection;

import javax.ejb.Local;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Organization;
import com.n4systems.model.ProductType;
import com.n4systems.tools.Pager;

@Local
public interface AutoAttributeManager {
	
	public Pager<AutoAttributeDefinition> findAllPage( AutoAttributeCriteria criteria, Organization tenant, Integer pageNumber, Integer pageSize);
		
	public AutoAttributeDefinition findTemplateToApply( ProductType productType, Collection<InfoOptionBean> selectedInfoOptions );
	public AutoAttributeDefinition findTemplateToApply(	AutoAttributeCriteria criteria,Collection<InfoOptionBean> selectedInfoOptions) ;
	public AutoAttributeDefinition saveDefinition( AutoAttributeDefinition definition );
	public AutoAttributeCriteria update( AutoAttributeCriteria criteria );
	public void removeDefinition( AutoAttributeDefinition definition ) ;
	public void delete(AutoAttributeCriteria criteria);
	public void clearRetiredInfoFields( ProductType productType );
		
}
