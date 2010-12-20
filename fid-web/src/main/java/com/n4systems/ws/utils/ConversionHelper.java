package com.n4systems.ws.utils;

import java.util.List;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.model.WsModelConverter;

public class ConversionHelper {
	
	public <M, W> W convertSingle(Loader<M> loader, WsModelConverter<M, W> converter){
		M model = WsLoaderHelper.load(loader);
		W wsModel = converter.fromModel(model);
		return wsModel;
	}
	
	public <M, W> List<W> convertList(Loader<List<M>> loader, WsModelConverter<M, W> converter){
		List<M> models = WsLoaderHelper.load(loader);
		List<W> wsModels = converter.fromModels(models);
		return wsModels;
	}
	
}
