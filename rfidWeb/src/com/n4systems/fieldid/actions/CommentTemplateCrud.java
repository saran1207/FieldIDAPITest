package com.n4systems.fieldid.actions;

import java.util.Collection;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.CommentTempBean;
import rfid.ejb.session.CommentTemp;

import com.google.gson.Gson;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
public class CommentTemplateCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;
	
	private CommentTempBean commentTemplate;
	private CommentTemp commentTemplateManager;
	
	private Gson json = new Gson();
	
	public CommentTemplateCrud(CommentTemp commentTemplateManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.commentTemplateManager = commentTemplateManager;
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		commentTemplate = commentTemplateManager.findCommentTemplate(getUniqueID());
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
			commentTemplateManager.persistCommentTemplate(commentTemplate);
		} else {
			commentTemplateManager.updateCommentTemplate(commentTemplate);
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
		
		commentTemplateManager.removeCommentTemplate(commentTemplate);
		
		return SUCCESS;
	}
	
	public Collection<ListingPair> getCommentTemplates() {
		return commentTemplateManager.findCommentTemplatesLP(getTenantId());
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
		for (ListingPair listingPair : getCommentTemplates()) {
			if(listingPair.getName() != null) {
				if ((listingPair.getName().equals(formValue) && uniqueID == null) ||
					(listingPair.getName().equals(formValue) && !listingPair.getId().equals(uniqueID))) {
					return true;
				}
			}
		}
		return false;
	}
}
