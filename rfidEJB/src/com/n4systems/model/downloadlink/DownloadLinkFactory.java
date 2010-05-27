package com.n4systems.model.downloadlink;

import com.n4systems.model.user.User;

public class DownloadLinkFactory {
	
	public DownloadLinkFactory() {}
	
	public DownloadLink createDownloadLink(User user, String name, ContentType type) {
		DownloadLink link = new DownloadLink();
		link.setState(DownloadState.REQUESTED);
		link.setContentType(type);
		link.setTenant(user.getTenant());
		link.setUser(user);
		link.setName(name);
		return link;
	}
}
