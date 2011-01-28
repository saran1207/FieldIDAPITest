package com.n4systems.ws.resources;

import java.util.List;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplateConverter;

public class CommentTemplateResourceDefiner implements ResourceDefiner<CommentTempBean, WsCommentTemplate>{

	@Override
	public WsModelConverter<CommentTempBean, WsCommentTemplate> getResourceConverter() {
		return new WsCommentTemplateConverter();
	}

	@Override
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLegacyLastModifiedListLoader(CommentTempBean.class);
	}

	@Override
	public IdLoader<? extends Loader<CommentTempBean>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createCommentTemplateIdLoader();
	}

}
