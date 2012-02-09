package com.n4systems.fieldid.service.comment;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.util.persistence.QueryBuilder;

@Transactional(readOnly = true)
public class CommentService extends FieldIdPersistenceService {
	
	public List<CommentTemplate> getCommentTemplates() {
		QueryBuilder<CommentTemplate> builder = new QueryBuilder<CommentTemplate>(CommentTemplate.class, securityContext.getUserSecurityFilter());
		builder.setOrder("name");
		return persistenceService.findAll(builder);
	}
	
	public List<CommentTemplate> getCommentTemplate(String name) {
		QueryBuilder<CommentTemplate> builder = new QueryBuilder<CommentTemplate>(CommentTemplate.class, securityContext.getUserSecurityFilter());
		builder.addSimpleWhere("name", name);
		return persistenceService.findAll(builder);
	}
}
