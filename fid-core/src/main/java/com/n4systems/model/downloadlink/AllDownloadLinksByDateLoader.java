package com.n4systems.model.downloadlink;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class AllDownloadLinksByDateLoader extends NonSecuredListLoader<DownloadLink> {

	private Date olderThanDate;
	
	public AllDownloadLinksByDateLoader() {}
	
	@Override
	public List<DownloadLink> load(EntityManager em) {
		QueryBuilder<DownloadLink> builder = new QueryBuilder<DownloadLink>(DownloadLink.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create(Comparator.LT, "created", olderThanDate));
		
		List<DownloadLink> downloads = builder.getResultList(em);
		return downloads;
	}

	public AllDownloadLinksByDateLoader setOlderThanDate(Date olderThanDate) {
		this.olderThanDate = olderThanDate;
		return this;
	}

}
