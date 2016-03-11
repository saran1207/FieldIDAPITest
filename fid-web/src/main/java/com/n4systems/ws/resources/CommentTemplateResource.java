package com.n4systems.ws.resources;

import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/CommentTemplate")
public class CommentTemplateResource extends BaseResource<CommentTemplate, WsCommentTemplate> {

	public CommentTemplateResource(@Context UriInfo uriInfo) {
		super(uriInfo, new CommentTemplateResourceDefiner());
	}

}
