package com.n4systems.ws.utils;

import org.apache.log4j.Logger;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.exceptions.WsInternalErrorException;
import com.n4systems.ws.exceptions.WsResourceNotFoundException;

public class WsLoaderHelper<T> {
	private static Logger logger = Logger.getLogger(WsLoaderHelper.class);
	
	public static <T> T load(Loader<T> loader) throws WsResourceNotFoundException {
		T entity = null;
		try {
			entity = loader.load();
		} catch (Exception e) {
			logger.error("Failed loading resource", e);
			throw new WsInternalErrorException();
		}
		
		if (entity == null) {
			throw new WsResourceNotFoundException();
		}
		
		return entity;
	}
}
