package com.n4systems.fieldid.actions.location;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.location.LevelName;
import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationLevelsSaver;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class PredefinedLocaitonLevelsCrud extends AbstractCrud {

	private PredefinedLocationLevels predefinedLocationLevels;
	
	private LevelNameWebModel levelName;

	public PredefinedLocaitonLevelsCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	
	
	public String doCreate() {
		PredefinedLocationLevels locationLevels = getPredefinedLocationLevels();
		
		
		locationLevels.getLevels().add(levelName.getIndex(), new LevelName(levelName.getName().getTrimmedString()));
		
		updateLevels(locationLevels);
		addFlashMessageText("label.level_added");
		return SUCCESS;
	}
	
	public String doUpdate() {
		PredefinedLocationLevels locationLevels = getPredefinedLocationLevels();
		
		if (isIndexNotInList(locationLevels)) {
			addActionErrorText("error.level_could_not_be_found");
			return INPUT;
		}
		
		locationLevels.getLevels().set(levelName.getIndex(), new LevelName(levelName.getName().getTrimmedString()));
		
		updateLevels(locationLevels);
		
		addFlashMessageText("label.level_updated");
		return SUCCESS;
	}
	
	public String doDelete() {
		PredefinedLocationLevels locationLevels = getPredefinedLocationLevels();
		if (isIndexNotInList(locationLevels)) {
			addActionErrorText("error.level_could_not_be_found");
			return INPUT;
		}
		
		locationLevels.getLevels().remove(levelName.getIndex().intValue());
		
		updateLevels(locationLevels);
		
		addFlashMessageText("label.level_removed");
		return SUCCESS;
	}

	private boolean isIndexNotInList(PredefinedLocationLevels locationLevels) {
		return levelName.getIndex() >= locationLevels.getLevels().size();
	}

	private void updateLevels(PredefinedLocationLevels locationLevels) {
		new PredefinedLocationLevelsSaver().update(locationLevels);
	}
	
	public List<LevelName> getLevels() {
		return getPredefinedLocationLevels().getLevels();
	}

	public LevelNameWebModel getLevelName() {
		return levelName;
	}

	public void setLevelName(LevelNameWebModel levelName) {
		this.levelName = levelName;
	}


	private PredefinedLocationLevels getPredefinedLocationLevels() {
		if (predefinedLocationLevels == null) {
			predefinedLocationLevels = getLoaderFactory().createPredefinedLocationLevelsLoader().load();
			predefinedLocationLevels.setTenant(getTenant());
			
		}
		return predefinedLocationLevels;
	}

}
