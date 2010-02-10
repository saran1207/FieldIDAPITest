package com.n4systems.exporting;

import java.io.Writer;

import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.CsvMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.SecurityFilter;

public class CustomerCSVExporter implements Exporter {
	private final Writer writer;
	private final ExportMapMarshaler<FullExternalOrgView> marshaler;
	private final CustomerOrgListLoader customerLoader;
	private final CustomerOrgViewConverter customerConverter;
	private final DivisionOrgByCustomerListLoader divisionList;
	private final DivisionOrgViewConverter divisionConverter;
	
	public CustomerCSVExporter(Writer writer, SecurityFilter filter) {
		this (
				writer,
				new ExportMapMarshaler<FullExternalOrgView>(FullExternalOrgView.class),
				new CustomerOrgListLoader(filter),
				new CustomerOrgViewConverter(filter),
				new DivisionOrgByCustomerListLoader(filter),
				new DivisionOrgViewConverter(filter)
			);
	}
	
	// TODO: this guy has too many dependencies.  We need to find a way to cut this down.
	protected CustomerCSVExporter(
			Writer writer, 
			ExportMapMarshaler<FullExternalOrgView> marshaler, 
			CustomerOrgListLoader customerLoader, 
			CustomerOrgViewConverter customerConverter, 
			DivisionOrgByCustomerListLoader divisionList, 
			DivisionOrgViewConverter divisionConverter
		) {
		
		this.writer = writer;
		this.marshaler = marshaler;
		this.customerLoader = customerLoader;
		this.customerConverter = customerConverter;
		this.divisionList = divisionList;
		this.divisionConverter = divisionConverter;
	}

	public void export() throws ExportException {
		MapWriter mapWriter = null;
		FullExternalOrgView export;
		
		for (CustomerOrg customer: customerLoader.withoutLinkedOrgs().load()) {
			
			try {
				export = customerConverter.toView(customer);
				
				if (mapWriter == null) {
					// we can't get our titles until we've converted the first view since
					// the view object is required to create them
					mapWriter = new CsvMapWriter(writer, marshaler.getTitles(export));
				}
				
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
		for (DivisionOrg division: divisionList.setCustomer(customer).load()) {
			
			try {
				export = divisionConverter.toView(division);
			
				mapWriter.write(marshaler.toBeanMap(export));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Division [%s]", division.toString()), e);
			}
		}
	}
}
