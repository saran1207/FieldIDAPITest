package com.n4systems.model.product;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.ProductNetworkFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.MaxSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class LastInspectionDateLoader extends Loader<Date> {

	private Long networkId;
	
	public LastInspectionDateLoader() {}

	@Override
	protected Date load(EntityManager em) {
		if (networkId == null) {
			throw new SecurityException("networkId must be set");
		}
		
		QueryBuilder<Date> loader = new QueryBuilder<Date>(Asset.class, new ProductNetworkFilter(networkId));
		loader.setSelectArgument(new MaxSelect("lastInspectionDate"));

		Date lastDate = loader.getSingleResult(em);
		return lastDate;
	}

	public LastInspectionDateLoader setNetworkId(Long networkId) { 
		this.networkId = networkId;
		return this;
	}
	
}
