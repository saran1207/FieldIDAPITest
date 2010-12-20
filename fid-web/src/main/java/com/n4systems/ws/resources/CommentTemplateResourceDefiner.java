package com.n4systems.ws.resources;

import java.util.List;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.exceptions.WsNotImplementedException;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplateConverter;

public class CommentTemplateResourceDefiner implements ResourceDefiner<CommentTempBean, WsCommentTemplate>{

	@Override
	public Class<WsCommentTemplate> getWsModelClass() {
		return WsCommentTemplate.class;
	}

	@Override
	public WsModelConverter<CommentTempBean, WsCommentTemplate> getResourceConverter() {
		return new WsCommentTemplateConverter();
	}

	@Override
	public Loader<List<CommentTempBean>> getResourceListLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createCommentTemplateListLoader();
	}

	@Override
	public IdLoader<? extends Loader<CommentTempBean>> getResourceIdLoader(LoaderFactory loaderFactory) {
		throw new WsNotImplementedException();
	}

}
