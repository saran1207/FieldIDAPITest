package com.n4systems.fieldid.service.org;

import com.n4systems.exceptions.DuplicateCustomerException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.eventbook.EventBookService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.notificationsetting.NotificationSettingService;
import com.n4systems.fieldid.service.project.ProjectService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.*;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

public class CustomerMergerService extends FieldIdPersistenceService {
	
	private static final Logger logger = Logger.getLogger(CustomerMergerService.class);
	
	@Autowired
	private NotificationSettingService notificationSettingService;

	@Autowired
	private EventScheduleService eventScheduleService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private EventBookService eventBookService;

	@Autowired
	private AssetService assetService;

	@Autowired
	private ThingEventCreationService thingEventCreationService;

	@Autowired
	private EventService eventService;

	@Autowired
	private MailService mailService;

	@Autowired
	private AsyncService asyncService;


	public void merge(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {

		AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				mergeCustomers(winningCustomer, losingCustomer);
				return null;
			}
		});

		asyncService.run(task);
	}

	private CustomerOrg mergeCustomers(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		
		try{
			validateMerge(winningCustomer, losingCustomer);
			
			moveDivisions(winningCustomer, losingCustomer);
			moveAssets(winningCustomer, losingCustomer);
			moveEventBooks(winningCustomer, losingCustomer);
			moveJobs(winningCustomer, losingCustomer);
			moveUsers(winningCustomer, losingCustomer);
	
			moveSavedReports(winningCustomer, losingCustomer);
			moveEmailNotifications(winningCustomer, losingCustomer);
			
			losingCustomer.archiveEntity();
			orgService.update(losingCustomer);
			mailService.sendMessage(getMessage(winningCustomer, losingCustomer));
		} catch (Exception e) {
			logger.error("Error merging customer", e);
			
		}
		return winningCustomer;
	}

	private void moveEmailNotifications(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<NotificationSetting> notificationQuery = createTenantSecurityBuilder(NotificationSetting.class).addSimpleWhere("owner", losingOrg);
		List<NotificationSetting> notifications = persistenceService.findAll(notificationQuery);
		
		for (NotificationSetting notification: notifications) {
			notification.setOwner(winningOrg);
			notificationSettingService.update(notification);
		}
	}

	private void moveSavedReports(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<SavedReport> reportQuery = createTenantSecurityBuilder(SavedReport.class).addSimpleWhere("tenant", losingOrg.getTenant());
		List<SavedReport> reports = persistenceService.findAll(reportQuery);
		
		for (SavedReport report : reports) {
			if(report.getCriteria().containsKey("ownerId") 
					&& report.getCriteria().get("ownerId").equals(losingOrg.getId().toString())) {
				report.getCriteria().put("ownerId", winningOrg.getId().toString());
				persistenceService.update(report);
			}
		}
	}

	private void moveUsers(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<User> userQuery = createTenantSecurityBuilder(User.class).addSimpleWhere("owner", losingOrg);
		List<User> users = persistenceService.findAll(userQuery);

		for (User user: users) {
			user.setOwner(winningOrg);
			userService.update(user);
		}
	}

	private void moveJobs(BaseOrg winningOrg, BaseOrg losingOrg) {
		QueryBuilder<Project> jobQuery = createTenantSecurityBuilder(Project.class).addSimpleWhere("owner", losingOrg);
		List<Project> jobs = persistenceService.findAll(jobQuery);
		
		for (Project job: jobs) {
			job.setOwner(winningOrg);
			projectService.update(job);
		}
	}

	private void moveEventBooks(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		QueryBuilder<EventBook> eventBookQuery = createTenantSecurityBuilder(EventBook.class).addSimpleWhere("owner", losingCustomer);
		List<EventBook> eventBooks = persistenceService.findAll(eventBookQuery);
		
		for (EventBook eventBook: eventBooks) {
			eventBook.setOwner(winningCustomer);
			eventBookService.update(eventBook);
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
				
				updateWinningDivisionInfo(divisonToMove, matchedDivision);
				
				divisonToMove.archiveEntity();
			}else {
				divisonToMove.setParent(winningCustomer);
			}
			orgService.update(divisonToMove);
		}
	}

	private void updateWinningDivisionInfo(DivisionOrg divisonToMove, DivisionOrg matchedDivision) {			
		matchedDivision.setCode(divisonToMove.getCode());		
		
		matchedDivision.getContact().setName(divisonToMove.getContact().getName());
		matchedDivision.getContact().setEmail(divisonToMove.getContact().getEmail());
		
		divisonToMove.getAddressInfo().copyFieldsTo(matchedDivision.getAddressInfo());
		
		orgService.update(matchedDivision);
	}

	private DivisionOrg divisionNameExists(String name,	List<DivisionOrg> winningDivisions) {
		for (DivisionOrg division : winningDivisions) {
			if(division.getName().replaceAll(" ", "").equalsIgnoreCase(name.replaceAll(" ", ""))) {
				return division;
			}
		}
		return null;
	}

	private List<DivisionOrg> getCustomerDivisions(CustomerOrg customer) {
		QueryBuilder<DivisionOrg> divisionQuery = createTenantSecurityBuilder(DivisionOrg.class).addSimpleWhere("parent", customer).addPostFetchPaths("addressInfo");
		return persistenceService.findAll(divisionQuery);
	}

	private void moveAssets(BaseOrg winningOrg, BaseOrg losingOrg) {

		QueryBuilder<Asset> assetQuery = createTenantSecurityBuilder(Asset.class).addSimpleWhere("owner", losingOrg);
		List<Asset> assetsToMove = persistenceService.findAll(assetQuery);
		
		for (Asset assetToMove : assetsToMove) {
			moveEvents(assetToMove, winningOrg);
			assetToMove.setOwner(winningOrg);
			assetService.update(assetToMove);
		}
	}

	//Move Open and Completed Events
	private void moveEvents(Asset asset, BaseOrg winningOrg) {
		List<ThingEvent> eventsToMove = eventService.getAssetEvents(asset);

		for (ThingEvent eventToMove : eventsToMove) {
			eventToMove.setOwner(winningOrg);
			persistenceService.update(eventToMove);
		}
	}

	private MailMessage getMessage(CustomerOrg winningCustomer, CustomerOrg losingCustomer) {
		String subject = "Customer Merge Completed";
		TemplateMailMessage message = new TemplateMailMessage(subject , "customerMerge");
		message.getToAddresses().add(getCurrentUser().getEmailAddress());
		message.getTemplateMap().put("winner", winningCustomer.getName());
		message.getTemplateMap().put("loser", losingCustomer.getName());
		return message;
	}
}
