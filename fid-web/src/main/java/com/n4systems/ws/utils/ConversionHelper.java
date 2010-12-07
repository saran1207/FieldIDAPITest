package com.n4systems.ws.utils;

import java.util.List;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.model.WsModelConverter;

public class ConversionHelper {
	
	public <M, W> W convert(Loader<M> loader, WsModelConverter<M, W> converter){
		M model = WsLoaderHelper.load(loader);
		W wsModel = converter.fromModel(model);
		return wsModel;
	}
	
	public <M, W> List<W> convert(ListLoader<M> loader, WsModelConverter<M, W> converter){
		List<M> models = WsLoaderHelper.load(loader);
		List<W> wsModels = converter.fromModels(models);
		return wsModels;
	}
	
}
