package com.n4systems.ws.utils;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.model.WsModelConverter;

import java.util.List;

public class ConversionHelper {
	
	public <M, W> List<W> convertList(Loader<List<M>> loader, WsModelConverter<M, W> converter){
		List<M> models = WsLoaderHelper.load(loader);
		List<W> wsModels = converter.fromModels(models);
		return wsModels;
	}
	
}
