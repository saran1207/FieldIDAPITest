package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.setupdata.WsUser;
import com.n4systems.ws.model.setupdata.WsUserConverter;

public class UserResourceDefiner implements ResourceDefiner<User, WsUser> {

	@Override
	public WsModelConverter<User, WsUser> getResourceConverter() {
		return new WsUserConverter();
	}

	@Override
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(User.class);
	}

	@Override
	public IdLoader<? extends Loader<User>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(User.class);
	}

	@Override
	public Class<WsUser> getWsModelClass() {
		return WsUser.class;
	}

}
