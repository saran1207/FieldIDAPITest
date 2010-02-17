package com.n4systems.model.downloadlink;

import rfid.ejb.entity.UserBean;

public class DownloadLinkFactory {
	
	public DownloadLinkFactory() {}
	
	public DownloadLink createDownloadLink(UserBean user, String name, ContentType type) {
		DownloadLink link = new DownloadLink();
		link.setState(DownloadState.REQUESTED);
		link.setContentType(type);
		link.setTenant(user.getTenant());
		link.setUser(user);
		link.setName(name);
		return link;
	}
}
