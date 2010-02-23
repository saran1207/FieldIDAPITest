package com.n4systems.exporting;

import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

public class CustomerExporter implements Exporter {
	private final ExportMapMarshaler<FullExternalOrgView> marshaler;
	private final ListLoader<CustomerOrg> customerLoader;
	private final CustomerOrgViewConverter customerConverter;
	private final DivisionOrgByCustomerListLoader divisionLoader;
	private final DivisionOrgViewConverter divisionConverter;
	
	public CustomerExporter(ListLoader<CustomerOrg> customerLoader, SecurityFilter filter) {
		this (
				customerLoader,
				new ExportMapMarshaler<FullExternalOrgView>(FullExternalOrgView.class),
				new CustomerOrgViewConverter(filter),
				new DivisionOrgByCustomerListLoader(filter),
				new DivisionOrgViewConverter(filter)
			);
	}
	
	// TODO: this guy has too many dependencies.  We need to find a way to cut this down.
	protected CustomerExporter(
			ListLoader<CustomerOrg> customerLoader, 
			ExportMapMarshaler<FullExternalOrgView> marshaler,
			CustomerOrgViewConverter customerConverter, 
			DivisionOrgByCustomerListLoader divisionList, 
			DivisionOrgViewConverter divisionConverter
		) {

		this.customerLoader = customerLoader;
		this.marshaler = marshaler;
		this.customerConverter = customerConverter;
		this.divisionLoader = divisionList;
		this.divisionConverter = divisionConverter;
	}

	public void export(MapWriter mapWriter) throws ExportException {
		FullExternalOrgView export;
		
		for (CustomerOrg customer: customerLoader.load()) {
			
			try {
				export = customerConverter.toView(customer);
				
				mapWriter.write(marshaler.toBeanMap(export));
				
				exportDivisions(mapWriter, customer);
				
			} catch (ExportException e) {
				// Pass on failures from the division exporting
				throw e;
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Customer [%s]", customer.toString()), e);
			}
		}
	}
	
	private void exportDivisions(MapWriter mapWriter, CustomerOrg customer) throws ExportException {
		FullExternalOrgView export;
		for (DivisionOrg division: divisionLoader.setCustomer(customer).load()) {
			
			try {
				export = divisionConverter.toView(division);
			
				mapWriter.write(marshaler.toBeanMap(export));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Division [%s]", division.toString()), e);
			}
		}
	}
}
