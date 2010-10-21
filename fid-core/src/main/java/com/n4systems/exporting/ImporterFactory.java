package com.n4systems.exporting;


import com.n4systems.api.conversion.autoattribute.AutoAttributeToModelConverter;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.conversion.orgs.CustomerOrgToModelConverter;
import com.n4systems.api.conversion.orgs.DivisionOrgToModelConverter;
import com.n4systems.api.conversion.product.ProductToModelConverter;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.handlers.creator.inspections.factory.ProductionInspectionPersistenceFactory;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.InspectionType;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.ServiceLocator;

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

	protected AutoAttributeToModelConverter createAutoAttributeToModelConverter(AutoAttributeCriteria criteria) {
		return new AutoAttributeToModelConverter(criteria);
	}

	protected ProductToModelConverter createProductToModelConverter(User identifiedBy, AssetType type) {
		ProductToModelConverter converter = new ProductToModelConverter(loaderFactory.createOrgByNameLoader(), createNonIntegrationOrderManager(), loaderFactory.createProductStatusByNameLoader(), new InfoOptionMapConverter());
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		return converter;
	}

	protected NonIntegrationOrderManager createNonIntegrationOrderManager() {
		return new NonIntegrationOrderManager(saverFactory.createNonIntegrationLineItemSaver());
	}

	protected ProductSaveService createProductSaveService(User user) {
		return new ProductSaveService(createLegacyProductSerial(), user);
	}

	protected LegacyProductSerial createLegacyProductSerial() {
		return ServiceLocator.getProductSerialManager();
	}

	protected InspectionPersistenceFactory createInspectionPersistenceFactory() {
		return new ProductionInspectionPersistenceFactory();
	}

	protected InspectionToModelConverter createInspectionToModelConverter(InspectionType type) {
		InspectionToModelConverter converter = new InspectionToModelConverter(
				loaderFactory.createOrgByNameLoader(), 
				loaderFactory.createSmartSearchListLoader(), 
				loaderFactory.createProductStatusByNameLoader(), 
				loaderFactory.createInspectionBookFindOrCreateLoader(), 
				loaderFactory.createUserByFullNameLoader());
		
		converter.setType(type);
		return converter;
	}

	public CustomerImporter createCustomerImporter(MapReader reader) {
		return new CustomerImporter(reader, createViewValidator(), saverFactory.createOrgSaver(), createCustomerOrgToModelConverter(), createDivisionOrgToModelConverter());
	}

	public AutoAttributeImporter createAutoAttributeImporter(MapReader reader, AutoAttributeCriteria criteria) {
		return new AutoAttributeImporter(reader, createViewValidator(), saverFactory.createAutoAttributeDefinitionSaver(), createAutoAttributeToModelConverter(criteria));
	}

	public ProductImporter createProductImporter(MapReader reader, User identifiedBy, AssetType type) {
		return new ProductImporter(reader, createViewValidator(), createProductSaveService(identifiedBy), createProductToModelConverter(identifiedBy, type));
	}

	public InspectionImporter createInspectionImporter(MapReader reader, Long modifiedBy, InspectionType type) {
		InspectionImporter importer = new InspectionImporter(reader, createViewValidator(), createInspectionPersistenceFactory(), createInspectionToModelConverter(type));
		importer.setModifiedBy(modifiedBy);
		return importer;
	}
}
