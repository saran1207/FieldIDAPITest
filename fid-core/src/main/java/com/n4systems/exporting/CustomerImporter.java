package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.orgs.CustomerOrgToModelConverter;
import com.n4systems.api.conversion.orgs.DivisionOrgToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.savers.Saver;

public class CustomerImporter extends AbstractImporter<FullExternalOrgView> {
	private final Saver<BaseOrg> orgSaver;
	private final CustomerOrgToModelConverter customerConverter;
	private final DivisionOrgToModelConverter divisionConverter;
	
	public CustomerImporter(MapReader mapReader, Validator<ExternalModelView> validator, Saver<BaseOrg> orgSaver, CustomerOrgToModelConverter customerConverter, DivisionOrgToModelConverter divisionConverter) {
		super(FullExternalOrgView.class, mapReader, validator);
		this.orgSaver = orgSaver;
		this.customerConverter = customerConverter;
		this.divisionConverter = divisionConverter;
	}
	
	@Override 
	protected void importView(Transaction transaction, FullExternalOrgView view) throws ConversionException {
		// validation should have caught if the view is neither a customer/division
		if (view.isCustomer()) {
			importCustomer(transaction, view);
		} else if (view.isDivision()) {
			importDivision(transaction, view);
		}
	}

	private void importCustomer(Transaction transaction, FullExternalOrgView view) throws ConversionException {
		CustomerOrg customer = customerConverter.toModel(view, transaction);
		
		customer = (CustomerOrg)orgSaver.saveOrUpdate(transaction, customer);
		
		// very important: the division converter needs to be updated when the customer changes otherwise
		// it won't know what customer to set (or will be setting the wrong customer)
		divisionConverter.setParentCustomer(customer);
	}
	
	private void importDivision(Transaction transaction, FullExternalOrgView view) throws ConversionException {
		DivisionOrg division = divisionConverter.toModel(view, transaction);
		
		orgSaver.saveOrUpdate(transaction, division);
	}

}
