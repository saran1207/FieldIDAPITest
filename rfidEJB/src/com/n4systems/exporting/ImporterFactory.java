package com.n4systems.exporting;

import com.n4systems.api.conversion.autoattribute.AutoAttributeToModelConverter;
import com.n4systems.api.conversion.orgs.CustomerOrgToModelConverter;
import com.n4systems.api.conversion.orgs.DivisionOrgToModelConverter;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;

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
	
	public CustomerImporter createCustomerImporter(MapReader reader) {
		return new CustomerImporter(reader, createViewValidator(), saverFactory.createOrgSaver(), createCustomerOrgToModelConverter(), createDivisionOrgToModelConverter());
	}

	public AutoAttributeImporter createAutoAttributeImporter(MapReader reader, AutoAttributeCriteria criteria) {
		return new AutoAttributeImporter(reader, createViewValidator(), saverFactory.createAutoAttributeDefinitionSaver(), createAutoAttributeToModelConverter(criteria));
	}
}
