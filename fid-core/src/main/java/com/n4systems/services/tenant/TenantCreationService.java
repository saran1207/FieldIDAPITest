package com.n4systems.services.tenant;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.*;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.procedure.LotoSettings;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.uri.ActionURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Locale;

public class TenantCreationService extends FieldIdPersistenceService {

	@Autowired public ConfigService configService;
    @Autowired public S3Service s3Service;
    @Autowired public MailService mailService;

	private OrgSaver orgSaver = new OrgSaver();
	private UserSaver userSaver = new UserSaver();

    public static String DEFAULT_ACTIONS_GROUP_NAME = "Actions";

    private enum DefaultPriorityCodes { Critical, High, Normal, Low; }

    @Transactional(rollbackFor = {MessagingException.class, RuntimeException.class })
	public void createTenant(Tenant tenant, PrimaryOrg primaryOrg, User adminUser) throws MessagingException {
        checkNameUniqueness(tenant);
		createTenant(tenant);
		createPrimaryOrg(tenant, primaryOrg, adminUser.getTimeZoneID());
		createSystemUser(primaryOrg);
		createAdminUser(primaryOrg, adminUser);
		createDefaultSetupData(tenant);
		createFieldIDCatalogConnection(primaryOrg);
        createBasicAction(tenant);
        createDefaultPriorityCode(tenant);
        s3Service.uploadDefaultBrandingLogo(tenant.getId());

		sendWelcomeMessage(adminUser);
	}

    private void createDefaultPriorityCode(Tenant tenant) {
        for (DefaultPriorityCodes code: DefaultPriorityCodes.values()) {
            PriorityCode priorityCode = new PriorityCode();
            priorityCode.setTenant(tenant);
            priorityCode.setName(code.name());
            persistenceService.save(priorityCode);
        }
    }

    private void createBasicAction(Tenant tenant) {
        EventTypeGroup actionGroup = new EventTypeGroup();
        actionGroup.setTenant(tenant);
        actionGroup.setName(DEFAULT_ACTIONS_GROUP_NAME);
        actionGroup.setReportTitle("Action");
        persistenceService.save(actionGroup);

        ActionEventType correctiveAction = new ActionEventType();
        correctiveAction.setGroup(actionGroup);
        correctiveAction.setTenant(tenant);
        correctiveAction.setName("Corrective Action");
        persistenceService.save(correctiveAction);
    }

    private void checkNameUniqueness(Tenant tenant) {
        QueryBuilder<Tenant> tenantBuilder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
        tenantBuilder.addSimpleWhere("name", tenant.getName());
        if (persistenceService.find(tenantBuilder) != null) {
            throw new RuntimeException("Tenant already exists with name: " + tenant.getName());
        }
    }

    private void createTenant(Tenant tenant) {
		persistenceService.save(tenant);

		TenantSettings settings = tenant.getSettings();
		settings.setTenant(tenant);
        settings.setDefaultLanguage(Locale.ENGLISH);
		persistenceService.save(settings);

        LotoSettings lotoSettings = new LotoSettings(tenant);
        persistenceService.save(lotoSettings);
	}

	private void createPrimaryOrg(Tenant tenant, PrimaryOrg primaryOrg, String timeZone) {
		primaryOrg.setTenant(tenant);
		primaryOrg.setUsingSerialNumber(true);
        primaryOrg.setIdentifierLabel("Serial Number");
		primaryOrg.setIdentifierFormat("NSA%y-%g");
		primaryOrg.setDateFormat("MM/dd/yy");
		primaryOrg.setDefaultTimeZone(timeZone);
		persistenceService.save(orgSaver, primaryOrg);
	}

	private void createSystemUser(PrimaryOrg primaryOrg) {
		User user = new User();
		user.setUserType(UserType.SYSTEM);
		user.setRegistered(true);
		user.setTenant(primaryOrg.getTenant());
		user.setOwner(primaryOrg);
		user.setPermissions(Permissions.SYSTEM);
		user.setTimeZoneID("Canada:Ontario - Toronto");
		user.setUserID(configService.getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setEmailAddress(configService.getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
		persistenceService.save(userSaver, user);
	}

	private void createAdminUser(PrimaryOrg primaryOrg, User adminUser) {
		adminUser.setUserType(UserType.ADMIN);
		adminUser.setRegistered(true);
		adminUser.setTenant(primaryOrg.getTenant());
		adminUser.setOwner(primaryOrg);
		adminUser.setPermissions(Permissions.ADMIN);
		adminUser.createResetPasswordKey();
		persistenceService.save(userSaver, adminUser);
	}

	private void createDefaultSetupData(Tenant tenant) {
		BaseSetupDataFactory setupDataFactory = new BaseSetupDataFactory(tenant);

		persistenceService.save(setupDataFactory.createSetupDataLastModDates());
		persistenceService.save(setupDataFactory.createSerialNumberCounterBean());
		persistenceService.save(setupDataFactory.createTagOption());
		persistenceService.save(setupDataFactory.createAssetType());
		persistenceService.save(setupDataFactory.createEventTypeGroup());
		persistenceService.save(setupDataFactory.createPassFailStateSet());
		persistenceService.save(setupDataFactory.createNAPassFailStateSet());

		for (AssetStatus status: setupDataFactory.createAssetStatuses()) {
			persistenceService.save(status);
		}

        for (EventStatus status: setupDataFactory.createEventStatuses()) {
            persistenceService.save(status);
        }
	}

	private void createFieldIDCatalogConnection(PrimaryOrg primaryOrg) {
		TypedOrgConnection catalogConnection = new TypedOrgConnection();
		catalogConnection.setConnectionType(ConnectionType.CATALOG_ONLY);
		catalogConnection.setOwner(primaryOrg);
		catalogConnection.setTenant(primaryOrg.getTenant());

		PrimaryOrg fieldidOrg = persistenceService.findNonSecure(PrimaryOrg.class, configService.getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		catalogConnection.setConnectedOrg(fieldidOrg);

		persistenceService.save(catalogConnection);
	}

	private void sendWelcomeMessage(User adminUser) throws MessagingException {
		TemplateMailMessage invitationMessage = new TemplateMailMessage("Welcome to Field ID", "welcomeMessageTenantCreated");
		invitationMessage.getToAddresses().add(adminUser.getEmailAddress());

		if (!configService.getString(ConfigEntry.SALES_ADDRESS).equals("")){
			invitationMessage.getBccAddresses().add("sales@fieldid.com");
		}

		String portalUrl = ActionURLBuilder
							.newUrl(configService)
							.setAction("firstTimeLogin")
							.setCompany(adminUser.getTenant())
							.addParameter("u", adminUser.getUserID())
							.addParameter("k", adminUser.getResetPasswordKey())
							.toString();

		invitationMessage.getTemplateMap().put("portalUrl", portalUrl);
		invitationMessage.getTemplateMap().put("companyId", adminUser.getTenant().getName());
		invitationMessage.getTemplateMap().put("username", adminUser.getUserID());

		mailService.sendMessage(invitationMessage);
	}
}
