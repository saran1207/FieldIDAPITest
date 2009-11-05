package com.n4systems.fieldid.actions;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.CommentTempBean;

import com.google.gson.Gson;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.api.Listable;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class CommentTemplateCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;
	
	private CommentTempBean commentTemplate;
		
	private Gson json = new Gson();

	private List<Listable<Long>> commentTemplates;
	
	public CommentTemplateCrud( PersistenceManager persistenceManager) {
		super(persistenceManager);
		
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		commentTemplate = persistenceManager.findLegacy(CommentTempBean.class, uniqueId, getSecurityFilter());
	}
	
	@Override
	protected void initMemberFields() {
		commentTemplate = new CommentTempBean();
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation 
	@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig, Permissions.Tag, Permissions.CreateInspection, Permissions.EditInspection})
	public String doShow() {
		if( commentTemplate == null ) {
			commentTemplate = new CommentTempBean();
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
		if (commentTemplate.getUniqueID() == null) {
			commentTemplate.setTenant(getTenant());
			persistenceManager.saveAny(commentTemplate);
		} else {
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
	
	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public String getName() {
		return commentTemplate.getTemplateID();
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message = "", key="error.required")
	@CustomValidator(type="uniqueValue", message = "", key="error.duplicate")
	public void setName(String name) {
		commentTemplate.setTemplateID(name);
	}
	
	public String getComment() {
		return commentTemplate.getContents();
	}

	public void setComment(String comment) {
		commentTemplate.setContents(comment);
	}
	
	public Gson getJson() {
		return json;
	}

	public CommentTempBean getCommentTemplate() {
		return commentTemplate;
	}

	public boolean duplicateValueExists(String formValue) {
		for (Listable<Long> template : getCommentTemplates()) {
			if(template.getDisplayName() != null) {
				if ((template.getDisplayName().equals(formValue) && uniqueID == null) ||
					(template.getDisplayName().equals(formValue) && !template.getId().equals(uniqueID))) {
					return true;
				}
			}
		}
		return false;
	}
}
