package com.n4systems.fieldid.ws.v2.resources.setupdata.commenttemplate;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("commentTemplate")
public class ApiCommentTemplateResource extends SetupDataResourceReadOnly<ApiCommentTemplate, CommentTemplate> {

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
