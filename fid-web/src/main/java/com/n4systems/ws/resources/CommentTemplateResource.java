package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;

@Path("/CommentTemplate")
public class CommentTemplateResource extends BaseResource<CommentTemplate, WsCommentTemplate> {

	public CommentTemplateResource(@Context UriInfo uriInfo) {
		super(uriInfo, new CommentTemplateResourceDefiner());
	}

}
