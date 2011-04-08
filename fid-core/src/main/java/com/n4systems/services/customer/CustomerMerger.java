package com.n4systems.services.customer;

import java.util.List;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.DuplicateCustomerException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.eventbook.EventBookSaver;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.project.ProjectSaver;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SavedReportSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.services.EventScheduleService;
import com.n4systems.util.persistence.QueryBuilder;

public class CustomerMerger {
	
	private final PersistenceManager persistenceManager;
	
	private final LegacyAsset legacyAsset;
	private final EventManager eventManager;
	private final User user;

	private final EventScheduleService scheduleService;
	
	private OrgSaver orgSaver = new OrgSaver();
	private EventBookSaver eventBookSaver = new EventBookSaver();
	private ProjectSaver projectSaver = new ProjectSaver();
	private UserSaver userSaver = new UserSaver();
	private SavedReportSaver reportSaver = new SavedReportSaver();
	private NotificationSettingSaver notificationSaver = new NotificationSettingSaver();


	public CustomerMerger(PersistenceManager persistenceManager, LegacyAsset legacyAsset, EventManager eventManger,
			EventScheduleService eventScheduleService, User user) {
		this.persistenceManager = persistenceManager;
		this.legacyAsset = legacyAsset;
		this.eventManager = eventManger;
		this.user = user;
		this.scheduleService = eventScheduleService;

	}
	
	public CustomerOrg merge(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		
		validateMerge(winningCustomer, losingCustomer);
		
		moveDivisions(winningCustomer, losingCustomer);
		moveAssets(winningCustomer, losingCustomer);
		moveEventBooks(winningCustomer, losingCustomer);
		moveJobs(winningCustomer, losingCustomer);
		moveUsers(winningCustomer, losingCustomer);

		moveSavedReports(winningCustomer, losingCustomer);
		moveEmailNotifications(winningCustomer, losingCustomer);
		
		losingCustomer.archiveEntity();
		orgSaver.update(losingCustomer);
		
		return winningCustomer;
	}

	private void moveEmailNotifications(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<NotificationSetting> notificationQuery = new QueryBuilder<NotificationSetting>(NotificationSetting.class, new OpenSecurityFilter()).addSimpleWhere("owner", losingOrg);
		List<NotificationSetting> notifications = persistenceManager.findAll(notificationQuery);
		
		for (NotificationSetting notification: notifications) {
			notification.setOwner(winningOrg);
			notificationSaver.update(notification);
		}
	}

	private void moveSavedReports(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<SavedReport> reportQuery = new QueryBuilder<SavedReport>(SavedReport.class, new OpenSecurityFilter()).addSimpleWhere("tenant", losingOrg.getTenant());
		List<SavedReport> reports = persistenceManager.findAll(reportQuery);
		
		for (SavedReport report : reports) {
			if(report.getCriteria().containsKey("ownerId")) {
				report.getCriteria().put("ownerId", winningOrg.getId().toString());
				reportSaver.update(report);
			}
		}
	}

	private void moveUsers(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<User> userQuery = new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("owner", losingOrg);
		List<User> users = persistenceManager.findAll(userQuery);

		for (User user: users) {
			user.setOwner(winningOrg);
			userSaver.update(user);
		}
	}

	private void moveJobs(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<Project> jobQuery = new QueryBuilder<Project>(Project.class, new OpenSecurityFilter()).addSimpleWhere("owner", losingOrg);
		List<Project> jobs = persistenceManager.findAll(jobQuery);
		
		for (Project job: jobs) {
			job.setOwner(winningOrg);
			projectSaver.update(job);
		}
	}

	private void moveEventBooks(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		QueryBuilder<EventBook> eventBookQuery = new QueryBuilder<EventBook>(EventBook.class, new OpenSecurityFilter()).addSimpleWhere("owner", losingCustomer);
		List<EventBook> eventBooks = persistenceManager.findAll(eventBookQuery);
		
		for (EventBook eventBook: eventBooks) {
			eventBook.setOwner(winningCustomer);
			eventBookSaver.update(eventBook);
		}
	}

	private void validateMerge(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		
		if(winningCustomer.equals(losingCustomer)) {
			throw new DuplicateCustomerException("you can't merge a customer into itself.");
		}
		
		if(!winningCustomer.getTenant().equals(losingCustomer.getTenant())) {
			throw new TenantNotValidForActionException("tenants must match");
		}
	}

	private void moveDivisions(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		
		List<DivisionOrg> losingDivisions = getCustomerDivisions(losingCustomer);
		List<DivisionOrg> winningDivisions = getCustomerDivisions(winningCustomer);

		for (DivisionOrg divisonToMove : losingDivisions) {
			DivisionOrg matchedDivision = divisionNameExists(divisonToMove.getName(), winningDivisions);
			if (matchedDivision != null) {
				moveAssets(matchedDivision, divisonToMove);
				moveJobs(matchedDivision, divisonToMove);
				moveUsers(matchedDivision, divisonToMove);
				
				moveSavedReports(matchedDivision, divisonToMove);
				moveEmailNotifications(matchedDivision, divisonToMove);
				divisonToMove.archiveEntity();

			}else {
				divisonToMove.setParent(winningCustomer);
				//TODO Set division customer?
			}
			orgSaver.update(divisonToMove);
		}
	}

	private DivisionOrg divisionNameExists(String name,	List<DivisionOrg> winningDivisions) {
		for (DivisionOrg division : winningDivisions) {
			if(division.getName().equalsIgnoreCase(name)) {
				return division;
			}
		}
		return null;
	}

	private List<DivisionOrg> getCustomerDivisions(CustomerOrg customer) {
		QueryBuilder<DivisionOrg> divisionQuery = new QueryBuilder<DivisionOrg>(DivisionOrg.class, new OpenSecurityFilter()).addSimpleWhere("parent", customer);
		return persistenceManager.findAll(divisionQuery);
	}

	private void moveAssets(BaseOrg winningOrg, BaseOrg losingOrg) {

		QueryBuilder<Asset> assetQuery = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter()).addSimpleWhere("owner", losingOrg);
		List<Asset> assetsToMove = persistenceManager.findAll(assetQuery);
		
		for (Asset assetToMove : assetsToMove) {
			moveEvents(assetToMove, winningOrg);
			assetToMove.setOwner(winningOrg);
			updateAsset(assetToMove);
		}
	}

	private void updateAsset(Asset asset) {
		try {
			legacyAsset.update(asset, user);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update asset to new owner", e);
		}
	}

	private void moveEvents(Asset asset, BaseOrg winningOrg) {
		QueryBuilder<Event> eventsQuery = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter()).addSimpleWhere("asset", asset);
		List<Event> eventsToMove = persistenceManager.findAll(eventsQuery);

		for (Event eventToMove : eventsToMove) {
			eventToMove.setOwner(winningOrg);
			updateEvent(eventToMove);
			updateSchedule(winningOrg, eventToMove.getSchedule());
		}
	}

	private void updateSchedule(BaseOrg winningOrg, EventSchedule schedule) {
		if (schedule != null) {
			schedule.setOwner(winningOrg);
			scheduleService.updateSchedule(schedule);
		}		
	}

	private void updateEvent(Event event) {
		try {
			eventManager.updateEvent(event, user.getId(), null, null);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update events to new owner", e);
		}
	}
}
