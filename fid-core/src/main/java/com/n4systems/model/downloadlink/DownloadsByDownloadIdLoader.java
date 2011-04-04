package com.n4systems.model.downloadlink;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class DownloadsByDownloadIdLoader extends SecurityFilteredLoader<DownloadLink>{

	private String downloadId;
	
	public DownloadsByDownloadIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected DownloadLink load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<DownloadLink> builder = new QueryBuilder<DownloadLink>(DownloadLink.class, new OpenSecurityFilter());
		builder.addSimpleWhere("downloadId", downloadId);
		return builder.getSingleResult(em);
	}

	public void setDownloadId(String downloadId) {
		this.downloadId = downloadId;
	}


}
