package com.n4systems.fieldid.ws.v1.resources.commenttemplate;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.commenttemplate.CommentTemplate;

@Component
@Path("commentTemplate")
public class ApiCommentTemplateResource extends SetupDataResource<ApiCommentTemplate, CommentTemplate> {

	public ApiCommentTemplateResource() {
		super(CommentTemplate.class);
	}

	@Override
	public ApiCommentTemplate convertEntityToApiModel(CommentTemplate template) {
		ApiCommentTemplate apiTemplate = new ApiCommentTemplate();
		apiTemplate.setSid(template.getId());
		apiTemplate.setActive(true);
		apiTemplate.setModified(template.getModified());
		apiTemplate.setName(template.getName());
		apiTemplate.setComment(template.getComment());
		return apiTemplate;
	}

}
