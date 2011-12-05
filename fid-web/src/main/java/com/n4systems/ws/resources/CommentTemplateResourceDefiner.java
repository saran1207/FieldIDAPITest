package com.n4systems.ws.resources;



import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplateConverter;

public class CommentTemplateResourceDefiner implements ResourceDefiner<CommentTemplate, WsCommentTemplate>{

	@Override
	public WsModelConverter<CommentTemplate, WsCommentTemplate> getResourceConverter() {
		return new WsCommentTemplateConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(CommentTemplate.class);
	}

	@Override
	public IdLoader<? extends Loader<CommentTemplate>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createEntityByIdLoader(CommentTemplate.class);
	}

	@Override
	public Class<WsCommentTemplate> getWsModelClass() {
		return WsCommentTemplate.class;
	}

}
