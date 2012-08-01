package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigEntry;

import java.util.Date;


@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class AssetSelectionAction extends AbstractAction {

	public AssetSelectionAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doSelection() {
		return SUCCESS;
	}
	
	public Long getListLimitSize() {
		return getConfigContext().getLong(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId());
	}

    public Date getNextScheduledEventDate(Long id) {
        Event openEvent = new NextEventScheduleLoader().setAssetId(id).load();
        return openEvent==null ? null : openEvent.getNextDate();
    }

}
