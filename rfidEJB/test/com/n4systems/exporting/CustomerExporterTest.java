package com.n4systems.exporting;


import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.persistence.loaders.ListLoader;

public class CustomerExporterTest {

	@Test
	@SuppressWarnings("unchecked")
	public void test_export() throws Exception {
		ExportMapMarshaler<FullExternalOrgView> marshaler = createMock(ExportMapMarshaler.class);
		ListLoader<CustomerOrg> customerLoader = createMock(ListLoader.class);
		CustomerOrgViewConverter customerConverter = createMock(CustomerOrgViewConverter.class);
		DivisionOrgByCustomerListLoader divisionLoader = createMock(DivisionOrgByCustomerListLoader.class);
		DivisionOrgViewConverter divisionConverter = createMock(DivisionOrgViewConverter.class);
		
		MapWriter writer = createMock(MapWriter.class);
		
		OrgBuilder cOrg = OrgBuilder.aCustomerOrg();
		OrgBuilder dOrg = OrgBuilder.aDivisionOrg();
		List<CustomerOrg> customers = Arrays.asList(cOrg.buildCustomer(), cOrg.buildCustomer());
		List[] divisions = { Arrays.asList(dOrg.buildDivision(), dOrg.buildDivision()), Arrays.asList(dOrg.buildDivision(), dOrg.buildDivision()) };
		
		FullExternalOrgView[] views = { new FullExternalOrgView(), new FullExternalOrgView() };
		Map[] beanMaps = { new HashMap<String, String>(), new HashMap<String, String>() };
		
		CustomerExporter exporter = new CustomerExporter(customerLoader, marshaler, customerConverter, divisionLoader, divisionConverter);
		
		expect(customerLoader.load()).andReturn(customers);
		
		for (int i = 0; i < 2; i++) {
			expect(customerConverter.toView(customers.get(i))).andReturn(views[i]);
			expect(marshaler.toBeanMap(views[i])).andReturn(beanMaps[i]);
			writer.write(beanMaps[i]);
			
			expect(divisionLoader.setCustomer(customers.get(i))).andReturn(divisionLoader);
			expect(divisionLoader.load()).andReturn(divisions[i]);
			
			for (int j = 0; j < 2; j++) {
				expect(divisionConverter.toView((DivisionOrg)divisions[i].get(j))).andReturn(views[j]);
				expect(marshaler.toBeanMap(views[j])).andReturn(beanMaps[j]);
				writer.write(beanMaps[j]);
			}
		}
		
		replay(marshaler);
		replay(customerLoader);
		replay(customerConverter);
		replay(divisionLoader);
		replay(divisionConverter);
		
		exporter.export(writer);
		
		
		verify(marshaler);
		verify(customerLoader);
		verify(customerConverter);
		verify(divisionLoader);
		verify(divisionConverter);
		
	}
	
}
