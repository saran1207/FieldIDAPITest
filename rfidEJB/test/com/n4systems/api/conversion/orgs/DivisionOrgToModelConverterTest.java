package com.n4systems.api.conversion.orgs;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.FullExternalOrgViewBuilder;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.testutils.DummyTransaction;

public class DivisionOrgToModelConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test(expected=ConversionException.class)
	public void test_to_model_throws_exception_on_null_parent() throws ConversionException {
		DivisionOrgToModelConverter converter = new DivisionOrgToModelConverter(createMock(GlobalIdLoader.class));
		
		converter.toModel(FullExternalOrgViewBuilder.aCustomerView().build(), new DummyTransaction());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_add() throws ConversionException {
		GlobalIdLoader<DivisionOrg> globalIdLoader = createMock(GlobalIdLoader.class);
		
		FullExternalOrgView view = FullExternalOrgViewBuilder.aDivisionView().forAdd().withTestData().build();
		
		CustomerOrg parent = OrgBuilder.aCustomerOrg().buildCustomer();
		
		DivisionOrgToModelConverter converter = new DivisionOrgToModelConverter(globalIdLoader);
		
		replay(globalIdLoader);

		converter.setParentCustomer(parent);
		DivisionOrg model = converter.toModel(view, new DummyTransaction());
		
		verifyModel(model, view, parent);
		
		verify(globalIdLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_edit_does_not_change_parent_customer() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<DivisionOrg> globalIdLoader = createMock(GlobalIdLoader.class);
		
		CustomerOrg parent = OrgBuilder.aCustomerOrg().withName("old_name").buildCustomer();
		DivisionOrg editModel =  OrgBuilder.aDivisionOrg().withParent(parent).buildDivision();

		FullExternalOrgView view = FullExternalOrgViewBuilder.aDivisionView().forEdit().withTestData().build();
		view.setParentOrg("new_name");
		
		DivisionOrgToModelConverter converter = new DivisionOrgToModelConverter(globalIdLoader);
		
		expect(globalIdLoader.setGlobalId(view.getGlobalId())).andReturn(globalIdLoader);
		expect(globalIdLoader.load(trans)).andReturn(editModel);
		
		replay(globalIdLoader);

		converter.setParentCustomer(OrgBuilder.aCustomerOrg().buildCustomer());
		DivisionOrg model = converter.toModel(view, trans);
		
		verifyModel(model, view, parent);
		
		assertEquals(editModel.getId(), model.getId());
		
		verify(globalIdLoader);
	}
	
	private void verifyModel(DivisionOrg model, FullExternalOrgView view, CustomerOrg parent) {
		assertNotNull(model);
		assertNotNull(model.getContact());
		assertNotNull(model.getAddressInfo());
		assertEquals(view.getName(), model.getName());
		assertEquals(view.getCode(), model.getCode());
		assertEquals(parent, model.getParent());
		assertEquals(parent.getTenant(), model.getTenant());
		assertEquals(view.getContactName(), model.getContact().getName());
		assertEquals(view.getContactEmail(), model.getContact().getEmail());
		assertEquals(view.getStreetAddress(), model.getAddressInfo().getStreetAddress());
		assertEquals(view.getCity(), model.getAddressInfo().getCity());
		assertEquals(view.getState(), model.getAddressInfo().getState());
		assertEquals(view.getCountry(), model.getAddressInfo().getCountry());
		assertEquals(view.getZip(), model.getAddressInfo().getZip());
		assertEquals(view.getPhone1(), model.getAddressInfo().getPhone1());
		assertEquals(view.getPhone2(), model.getAddressInfo().getPhone2());
		assertEquals(view.getFax1(), model.getAddressInfo().getFax1());
	}
}
