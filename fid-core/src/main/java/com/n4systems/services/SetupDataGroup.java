package com.n4systems.services;

import java.util.Date;

import com.n4systems.model.EventBook;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.InspectionType;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.State;
import com.n4systems.model.UnitOfMeasure;
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
	PRODUCT_TYPE(AssetType.class, AssetStatus.class, AssetTypeGroup.class, AssociatedInspectionType.class, AssetTypeSchedule.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getAssetTypes();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setAssetTypes(newDate);
		}
	},
	INSPECTION_TYPE(InspectionType.class, State.class, EventBook.class, CommentTempBean.class, UnitOfMeasure.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getInspectionTypes();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setInspectionTypes(newDate);
		}
	},
	AUTO_ATTRIBUTES(AutoAttributeCriteria.class, AutoAttributeDefinition.class

	) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getAutoAttributes();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setAutoAttributes(newDate);
		}
	},
	OWNERS(PrimaryOrg.class, SecondaryOrg.class, CustomerOrg.class, DivisionOrg.class, OrgConnection.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getOwners();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setOwners(newDate);
		}
	},
	JOBS(Project.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getJobs();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setJobs(newDate);
		}
	
	},
	EMPLOYEES (User.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModeDates) {
			return lastModeDates.getEmployees();
		}
		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setEmployees(newDate);
		}
	},
	LOCATIONS(PredefinedLocation.class) {
		@Override
		public Date getLastModDate(SetupDataLastModDates lastModDates) {
			return lastModDates.getLocations();
		}

		@Override
		public void setLastModDate(Date newDate, SetupDataLastModDates lastModeDates) {
			lastModeDates.setLocations(newDate);
		}
	};

	private final Class<?>[] groupClasses;

	SetupDataGroup(Class<?>... groupClasses) {
		this.groupClasses = groupClasses;
	}

	public Class<?>[] getGroupClasses() {
		return groupClasses;
	}

	public abstract Date getLastModDate(SetupDataLastModDates lastModDates);

	public abstract void setLastModDate(Date newDate, SetupDataLastModDates lastModDates);

}