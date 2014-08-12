package com.n4systems.fieldid.service.comment;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    public CommentTemplate getCommentTemplateById(Long id) {
        return persistenceService.find(CommentTemplate.class, id);
    }

    public CommentTemplate update(CommentTemplate commentTemplate, User modifiedBy) {
        commentTemplate.setModified(new Date(System.currentTimeMillis()));
        commentTemplate.setModifiedBy(modifiedBy);

        return persistenceService.update(commentTemplate);
    }

    public CommentTemplate save(CommentTemplate commentTemplate, User user) {
        commentTemplate.setModified(new Date(System.currentTimeMillis()));
        commentTemplate.setCreated(new Date(System.currentTimeMillis()));
        commentTemplate.setModifiedBy(user);
        commentTemplate.setCreatedBy(user);

        Long id = persistenceService.save(commentTemplate);

        return persistenceService.find(CommentTemplate.class, id);
    }

    public void delete(CommentTemplate commentTemplate) {
        persistenceService.delete(commentTemplate);
    }

    public List<CommentTemplate> getPagedCommentTemplatesList(String order,
                                                              boolean ascending,
                                                              int first,
                                                              int count) {

        QueryBuilder<CommentTemplate> query = createUserSecurityBuilder(CommentTemplate.class);

        if(order != null) {
            query.addOrder(order,
                           ascending);
        }

        return persistenceService.findAllPaginated(query,
                                                   first,
                                                   count);
    }

    public Long getCommentTemplateCount() {

        QueryBuilder<CommentTemplate> query = createUserSecurityBuilder(CommentTemplate.class);

        return persistenceService.count(query);
    }

    /**
     * This method checks if a Comment Template name exists.
     * @param name - A String value representing the name of the Comment Template.
     * @param id - A Long value representing the ID of the comment template if there is one.
     * @return A boolean value indicating whether (true) or not (false) the name is used elsewhere.
     */
    public boolean exists(String name, Long id) {
        QueryBuilder<CommentTemplate> query = createUserSecurityBuilder(CommentTemplate.class);

        query.addSimpleWhere("name", name);
        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE,
                                                     "id",
                                                     id));
        }
        return persistenceService.exists(query);
    }
}
