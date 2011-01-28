package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;

public interface ResourceDefiner<M, W> {
	public WsModelConverter<M, W> getResourceConverter();
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory);
	public IdLoader<? extends Loader<M>> getResourceIdLoader(LoaderFactory loaderFactory);
	public Class<W> getWsModelClass();
}
