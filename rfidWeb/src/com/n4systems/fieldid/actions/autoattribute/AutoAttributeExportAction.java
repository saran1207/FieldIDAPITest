package com.n4systems.fieldid.actions.autoattribute;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.Permissions;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AutoAttributeExportAction extends AbstractAction {
	private Logger logger = Logger.getLogger(AutoAttributeExportAction.class);
	
	private String exportType; 
	private AutoAttributeCriteria autoAttributeCriteria;

	public AutoAttributeExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doShowImportExport() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doExport() {
		try {
			ContentType contentType = ContentType.valueOf(exportType.toUpperCase());
			
//			AutoAttributeDefinitionListLoader autoAttribLoader = getLoaderFactory().createAutoAttributeDefinitionListLoader().setCriteriaId(criteriaId);
			
			ListLoader<AutoAttributeDefinition> attribLoader = getLoaderFactory().createPassthruListLoader(autoAttributeCriteria.getDefinitions());
			
			getDownloadCoordinator().generateAutoAttributeExport(getText("label.export_file"), getDownloadLinkUrl(), contentType, attribLoader);
		} catch (RuntimeException e) {
			logger.error("Unable to execute auto attribute export", e);
			addFlashMessage(getText("error.export_failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(AutoAttributeCriteria autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}
	
	public Long getCriteriaId() {
		return autoAttributeCriteria.getId();
	}

	public void setCriteriaId(Long criteriaId) {
		if (autoAttributeCriteria == null || !autoAttributeCriteria.getId().equals(criteriaId)) {
			autoAttributeCriteria = getLoaderFactory().createFilteredIdLoader(AutoAttributeCriteria.class).setId(criteriaId).setPostFetchFields("definitions").load();
		}
	}

}
