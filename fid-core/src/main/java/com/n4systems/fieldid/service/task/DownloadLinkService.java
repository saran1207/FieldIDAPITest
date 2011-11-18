package com.n4systems.fieldid.service.task;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;

@Transactional
public class DownloadLinkService extends FieldIdPersistenceService {
	
	public DownloadLink createDownloadLink(String name, ContentType type) {
		DownloadLink link = new DownloadLink();
		link.setState(DownloadState.REQUESTED);
		link.setContentType(type);
		link.setTenant(getCurrentTenant());
		link.setUser(getCurrentUser());
		link.setName(name);
		save(link);
		return link;
	}

	public void save(DownloadLink downloadLink) {
		persistenceService.save(downloadLink);
	}

	public void update(DownloadLink downloadLink) {
		persistenceService.update(downloadLink);
	}

	public void updateState(DownloadLink downloadLink, DownloadState state) {
		downloadLink.setState(state);
		update(downloadLink);
	}
	
}
