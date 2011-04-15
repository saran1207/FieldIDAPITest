package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;

public class DownloadLinkBuilder extends BaseBuilder<DownloadLink> {
	
	private String name;
	private ContentType contentType;
	private DownloadState state;
	private User user;
	private Tenant tenant;
	
	public static DownloadLinkBuilder aDownloadLink() {
		return new DownloadLinkBuilder(TenantBuilder.aTenant().build(), "downloadLink", ContentType.PDF, DownloadState.COMPLETED, UserBuilder.aUser().build());
	}
	
	
	public DownloadLinkBuilder(Tenant tenant, String name, ContentType contentType, DownloadState state, User user) {
		this.tenant = tenant;
		this.name = name;
		this.contentType = contentType;
		this.state = state;
		this.user = user;
	}

	@Override
	public DownloadLink createObject() {
		DownloadLink link = new DownloadLink();
		link.setTenant(tenant);
		link.setName(name);
		link.setContentType(contentType);
		link.setState(state);
		link.setUser(user);
		link.setCreatedBy(user);
		return link;
	}

	public DownloadLinkBuilder withTenant(Tenant tenant) {
		return makeBuilder(new DownloadLinkBuilder(tenant, name, contentType, state, user));
	}
	
	public DownloadLinkBuilder withName(String name) {
		return makeBuilder(new DownloadLinkBuilder(tenant, name, contentType, state, user));
	}

	public DownloadLinkBuilder withContentType(ContentType contentType) {
		return makeBuilder(new DownloadLinkBuilder(tenant, name, contentType, state, user));
	}

	public DownloadLinkBuilder withState(DownloadState state) {
		return makeBuilder(new DownloadLinkBuilder(tenant, name, contentType, state, user));
	}

	public DownloadLinkBuilder withUser(User user) {
		return makeBuilder(new DownloadLinkBuilder(tenant, name, contentType, state, user));
	}

}
