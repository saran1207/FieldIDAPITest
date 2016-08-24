package com.n4systems.fieldid.actions;

import com.google.gson.Gson;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.api.Listable;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.*;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

@Validation
@UserPermissionFilter(userRequiresOneOf = { Permissions.MANAGE_SYSTEM_CONFIG})
public class CommentTemplateCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;

	private CommentTemplate commentTemplate;

	private Gson json = new Gson();

	private List<CommentTemplate> commentTemplates;

	public CommentTemplateCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		commentTemplate = persistenceManager.find(CommentTemplate.class, uniqueId, getSecurityFilter());
	}

	@Override
	protected void initMemberFields() {
		commentTemplate = new CommentTemplate();
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.MANAGE_SYSTEM_CONFIG, Permissions.TAG, Permissions.CREATE_EVENT, Permissions.EDIT_EVENT})
	public String doShow() {
		if (commentTemplate == null) {
			commentTemplate = new CommentTemplate();
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doLoadEdit() {
		if (commentTemplate == null) {
			addActionError("Comment Template not found");
			return ERROR;
		}

		return INPUT;
	}

	public String doSave() {
		if (commentTemplate.getId() == null) {
			commentTemplate.setTenant(getTenant());
			persistenceManager.saveAny(commentTemplate);
		} else {
			persistenceManager.updateAny(commentTemplate);
		}

		addActionMessage("Data has been updated.");

		return SUCCESS;
	}

	@SkipValidation
	public String doRemove() {
		if (commentTemplate == null) {
			addActionError("Comment Template not found");
			return ERROR;
		}

		persistenceManager.deleteAny(commentTemplate);

		return SUCCESS;
	}

	public List<CommentTemplate> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public String getName() {
		return commentTemplate.getName();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.required")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.duplicate")
	public void setName(String name) {
		commentTemplate.setName(name);
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.comment_template_length", maxLength = "250")
	public String getComment() {
		return commentTemplate.getComment();
	}

	public void setComment(String comment) {
		commentTemplate.setComment(comment);
	}

	public Gson getJson() {
		return json;
	}

	public CommentTemplate getCommentTemplate() {
		return commentTemplate;
	}

	public boolean duplicateValueExists(String formValue) {
		for (Listable<Long> template : getCommentTemplates()) {
			if (template.getDisplayName() != null) {
				if ((template.getDisplayName().equals(formValue) && uniqueID == null)
						|| (template.getDisplayName().equals(formValue) && !template.getId().equals(uniqueID))) {
					return true;
				}
			}
		}
		return false;
	}
}
