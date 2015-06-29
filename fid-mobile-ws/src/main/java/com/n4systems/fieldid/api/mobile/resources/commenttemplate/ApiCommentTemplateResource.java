package com.n4systems.fieldid.api.mobile.resources.commenttemplate;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("commentTemplate")
public class ApiCommentTemplateResource extends SetupDataResource<ApiCommentTemplate, CommentTemplate> {

	public ApiCommentTemplateResource() {
		super(CommentTemplate.class, false);
	}

	@Override
	protected ApiCommentTemplate convertEntityToApiModel(CommentTemplate template) {
		ApiCommentTemplate apiTemplate = new ApiCommentTemplate();
		apiTemplate.setSid(template.getId());
		apiTemplate.setActive(true);
		apiTemplate.setModified(template.getModified());
		apiTemplate.setName(template.getName());
		apiTemplate.setComment(template.getComment());
		return apiTemplate;
	}

}
