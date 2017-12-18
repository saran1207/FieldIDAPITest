package com.n4systems.exporting;


import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.conversion.autoattribute.AutoAttributeToModelConverter;
import com.n4systems.api.conversion.event.EventToModelConverter;
import com.n4systems.api.conversion.orgs.CustomerOrgToModelConverter;
import com.n4systems.api.conversion.orgs.DivisionOrgToModelConverter;
import com.n4systems.api.conversion.users.UserToModelConverter;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.util.ServiceLocator;
import com.n4systems.utils.email.WelcomeNotifier;

import java.util.TimeZone;

public class ImporterFactory {
	private final SecurityFilter filter;
	private final SaverFactory saverFactory;
	private final LoaderFactory loaderFactory;

	public ImporterFactory(SecurityFilter filter) {
		this(filter, new SaverFactory(), new LoaderFactory(filter));
	}

	public ImporterFactory(SecurityFilter filter, SaverFactory saverFactory, LoaderFactory loaderFactory) {
		this.filter = filter;
		this.saverFactory = saverFactory;
		this.loaderFactory = loaderFactory;
	}

	protected ViewValidator createViewValidator() {
		return new ViewValidator(filter);
	}

	protected CustomerOrgToModelConverter createCustomerOrgToModelConverter() {
		return new CustomerOrgToModelConverter(loaderFactory.createGlobalIdLoader(CustomerOrg.class), loaderFactory.createInternalOrgByNameLoader());
	}

	protected DivisionOrgToModelConverter createDivisionOrgToModelConverter() {
		return new DivisionOrgToModelConverter(loaderFactory.createGlobalIdLoader(DivisionOrg.class));
	}
	
	private UserToModelConverter createUserToModelConverter() {
		return new UserToModelConverter(loaderFactory.createGlobalIdLoader(User.class), loaderFactory.createOrgByNameLoader(), loaderFactory.createUserGroupForNameLoader());
	}

	protected AutoAttributeToModelConverter createAutoAttributeToModelConverter(AutoAttributeCriteria criteria) {
		return new AutoAttributeToModelConverter(criteria);
	}

	protected AssetToModelConverter createAssetToModelConverter(User identifiedBy, AssetType type) {
		AssetToModelConverter converter = new AssetToModelConverter(loaderFactory.createAssetByMobileGuidLoader(),
																	loaderFactory.createOrgByNameLoader(),
                                                                    createNonIntegrationOrderManager(),
                                                                    loaderFactory.createAssetStatusByNameLoader(),
                                                                    new InfoOptionMapConverter(),
                                                                    loaderFactory.createPredefinedLocationTreeLoader(),
                                                                    loaderFactory.createUserByFullNameLoader());
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		return converter;
	}

	protected NonIntegrationOrderManager createNonIntegrationOrderManager() {
		return new NonIntegrationOrderManager(saverFactory.createNonIntegrationLineItemSaver());
	}

	protected AssetSaveService createAssetSaveService(User user) {
		return new AssetSaveService(createLegacyAsset(), user);
	}

	protected LegacyAsset createLegacyAsset() {
		return ServiceLocator.getLegacyAssetManager();
	}

	protected EventPersistenceFactory createEventPersistenceFactory() {
		return new ProductionEventPersistenceFactory();
	}

	protected EventToModelConverter createEventToModelConverter(ThingEventType type, TimeZone timeZone) {
		EventToModelConverter converter = new EventToModelConverter(
				loaderFactory.createOrgByNameLoader(), 
				loaderFactory.createAssetByIdOwnerTypeLoader(), 
				loaderFactory.createAssetStatusByNameLoader(),
                loaderFactory.createEventStatusByNameLoader(),
                loaderFactory.createEventBookFindOrCreateLoader(),
				loaderFactory.createUserByFullNameLoader(),
                loaderFactory.createPredefinedLocationTreeLoader(),
				type, timeZone);
		
		return converter;
	}

	public CustomerImporter createCustomerImporter(MapReader reader) {
		return new CustomerImporter(reader, createViewValidator(), saverFactory.createOrgSaver(), createCustomerOrgToModelConverter(), createDivisionOrgToModelConverter());
	}

	public AutoAttributeImporter createAutoAttributeImporter(MapReader reader, AutoAttributeCriteria criteria) {
		return new AutoAttributeImporter(reader, createViewValidator(), saverFactory.createAutoAttributeDefinitionSaver(), createAutoAttributeToModelConverter(criteria));
	}


	public UserImporter createUserImporter(MapReader reader, WelcomeNotifier emailNotifier, UserLimits userLimits, String timeZoneId, PasswordPolicy passwordPolicy) {
		// UGH DD : too many arguments for this constructor.  refactor.
		return new UserImporter(reader, createViewValidator(), userLimits, saverFactory.createUserSaver(), createUserToModelConverter(), emailNotifier, timeZoneId, passwordPolicy);	
	}
	
	public AssetImporter createAssetImporter(MapReader reader, User identifiedBy, AssetType type, SecurityContext securityContext, SecurityFilter securityFilter) {
		return new AssetImporter(reader, createViewValidator(), createAssetToModelConverter(identifiedBy, type), securityContext, securityFilter);
	}

	public EventImporter createEventImporter(MapReader reader, User modifiedBy, ThingEventType type) {
		EventImporter importer = new EventImporter(reader, createViewValidator(), createEventPersistenceFactory(), createEventToModelConverter(type, modifiedBy.getTimeZone()));
		importer.setModifiedBy(modifiedBy.getId());
		return importer;
	}
}
