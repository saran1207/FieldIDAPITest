package com.n4systems.model.asset;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.AssetNetworkFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.MaxSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class LastEventDateLoader extends Loader<Date> {

	private Long networkId;
	
	public LastEventDateLoader() {}

	@Override
	public Date load(EntityManager em) {
		if (networkId == null) {
			throw new SecurityException("networkId must be set");
		}
		
		QueryBuilder<Date> loader = new QueryBuilder<Date>(Asset.class, new AssetNetworkFilter(networkId));
		loader.setSelectArgument(new MaxSelect("lastEventDate"));

		Date lastDate = loader.getSingleResult(em);
		return lastDate;
	}

	public LastEventDateLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}
	
}
