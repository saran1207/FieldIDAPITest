package com.n4systems.services;


import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.user.User;

public enum SetupDataGroup {
	/*
	 * NOTE - the group classes must implement the HasTenant or
	 * CrossTenantEntity interface. On save, entities implementing HasTenant
	 * will notify the SetupDataLastModUpdateService for their group and
	 * specific tenant. entities implementing CrossTenantEntity will update for
	 * their group and all tenants. If the class is not a subclass of HasTenant
	 * or CrossTenantEntity, SetupDataUpdateEventListener will throw an
	 * exception!
	 */
	PRODUCT_TYPE	(AssetType.class, AssetStatus.class, AssetTypeGroup.class, AssociatedEventType.class, AssetTypeSchedule.class),
	INSPECTION_TYPE	(EventType.class, State.class, StateSet.class, EventBook.class, CommentTemplate.class, UnitOfMeasure.class),
	AUTO_ATTRIBUTES	(AutoAttributeCriteria.class, AutoAttributeDefinition.class),
	OWNERS			(PrimaryOrg.class, SecondaryOrg.class, CustomerOrg.class, DivisionOrg.class, OrgConnection.class),
	JOBS			(Project.class),
	EMPLOYEES		(User.class),
	LOCATIONS		(PredefinedLocation.class);

	private final Class<?>[] groupClasses;

	SetupDataGroup(Class<?>... groupClasses) {
		this.groupClasses = groupClasses;
	}

	public Class<?>[] getGroupClasses() {
		return groupClasses;
	}
	
	public String getFieldName() {
		return SetupDataLastModDates.getFieldNameForSetupDataGroup(this);
	}
}
