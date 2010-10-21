package com.n4systems.ejb;

import java.util.Collection;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Tenant;
import com.n4systems.tools.Pager;

public interface AutoAttributeManager {
	
	public Pager<AutoAttributeDefinition> findAllPage( AutoAttributeCriteria criteria, Tenant tenant, Integer pageNumber, Integer pageSize);
		
	public AutoAttributeDefinition findTemplateToApply( AssetType assetType, Collection<InfoOptionBean> selectedInfoOptions );
	public AutoAttributeDefinition findTemplateToApply(	AutoAttributeCriteria criteria,Collection<InfoOptionBean> selectedInfoOptions) ;
	public AutoAttributeDefinition saveDefinition( AutoAttributeDefinition definition );
	public AutoAttributeCriteria update( AutoAttributeCriteria criteria );
	public void removeDefinition( AutoAttributeDefinition definition ) ;
	public void delete(AutoAttributeCriteria criteria);
	public void clearRetiredInfoFields( AssetType assetType);
		
}
