package com.n4systems.model.downloadlink;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class DownloadLinkListLoader extends ListLoader<DownloadLink> {

	public DownloadLinkListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<DownloadLink> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<DownloadLink> builder = new QueryBuilder<DownloadLink>(DownloadLink.class, filter);
		builder.addOrder("created", false);
		
		List<DownloadLink> links = builder.getResultList(em);
		return links;
	}

}
