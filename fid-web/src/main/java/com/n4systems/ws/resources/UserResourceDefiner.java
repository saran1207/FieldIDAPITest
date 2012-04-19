package com.n4systems.ws.resources;


import com.n4systems.model.lastmodified.LastModifiedListLoader;
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
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createUserLastModifiedLoader();
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
