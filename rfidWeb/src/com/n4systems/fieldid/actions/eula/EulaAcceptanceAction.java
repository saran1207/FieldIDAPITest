package com.n4systems.fieldid.actions.eula;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.model.eula.EULA;
import com.n4systems.model.eula.EulaAcceptance;

public class EulaAcceptanceAction extends AbstractAction {
	private static final Logger logger = Logger.getLogger(EulaAcceptanceAction.class);
	private static final long serialVersionUID = 1L;

	private EULA currentEULA;
	
	private EulaAcceptance acceptance;
	private Long eulaId;
	
	
	public EulaAcceptanceAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private void testRequiredEntities(boolean existing) {
		if (!fetchCurrentUser().isAdmin()) {
			addActionErrorText("error.only_admins_on_eula");
			logger.warn(getLogLinePrefix() + "Non admin user [" + getSessionUser().getUserID() + "] on tenant [" + getTenant().getName() + "] has attempted to access the eula accept page.");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(true);
		eulaId = getCurrentEULA().getId();
		EulaAcceptance lastAcceptance = getLastEulaAcceptence();
		if (lastAcceptance != null && lastAcceptance.getEula().equals(getCurrentEULA())) {
			return "alreadyAccepted";
		}
		testRequiredEntities(false);
		return SUCCESS;
	}

	private EulaAcceptance getLastEulaAcceptence() {
		return getLoaderFactory().createLatestEulaAcceptanceLoader().load();
		
	}

	public String doCreate() {
		testRequiredEntities(false);
		if (!getCurrentEULA().getId().equals(eulaId)) {
			addActionErrorText("error.not_accepting_the_current_eula");
			return INPUT;
		}
		
		acceptance = new EulaAcceptance();
		acceptance.setAcceptor(fetchCurrentUser());
		acceptance.setEula(getCurrentEULA());
		acceptance.setTenant(getTenant());
		
		persistenceManager.save(acceptance);
		addFlashMessageText("message.accepted_eula");
		
		
		return SUCCESS;
	}

	public EULA getCurrentEULA() {
		if (currentEULA == null) {
			currentEULA = getLoaderFactory().createCurrentEulaLoader().load();
		}
		return currentEULA;
	}

	public Long getEulaId() {
		return eulaId;
	}

	public void setEulaId(Long eulaId) {
		this.eulaId = eulaId;
	}
	
}
