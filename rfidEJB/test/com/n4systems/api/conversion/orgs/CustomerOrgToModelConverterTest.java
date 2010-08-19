package com.n4systems.api.conversion.orgs;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.FullExternalOrgViewBuilder;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.internal.InternalOrgByNameLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.testutils.DummyTransaction;

public class CustomerOrgToModelConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_add() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<CustomerOrg> globalIdLoader = createMock(GlobalIdLoader.class);
		InternalOrgByNameLoader orgLoader = createMock(InternalOrgByNameLoader.class);
		
		FullExternalOrgView view = FullExternalOrgViewBuilder.aCustomerView().forAdd().withTestData().build();
		
		PrimaryOrg parentOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		CustomerOrgToModelConverter converter = new CustomerOrgToModelConverter(globalIdLoader, orgLoader);
		
		expect(orgLoader.setName(view.getParentOrg())).andReturn(orgLoader);
		expect(orgLoader.load(trans)).andReturn(parentOrg);
		
		replay(globalIdLoader);
		replay(orgLoader);
		
		CustomerOrg model = converter.toModel(view, trans);
		
		verifyModel(model, view, parentOrg);
		
		verify(globalIdLoader);
		verify(orgLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_edit_does_not_change_parent_org_if_same() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<CustomerOrg> globalIdLoader = createMock(GlobalIdLoader.class);
		InternalOrgByNameLoader orgLoader = createMock(InternalOrgByNameLoader.class);
		
		PrimaryOrg parentOrg = OrgBuilder.aPrimaryOrg().withName("my customer").buildPrimary();
		CustomerOrg editOrg = OrgBuilder.aCustomerOrg().withParent(parentOrg).buildCustomer();
		
		FullExternalOrgView view = FullExternalOrgViewBuilder.aCustomerView().forEdit().withTestData().build();
		view.setParentOrg(editOrg.getParent().getName());
		
		CustomerOrgToModelConverter converter = new CustomerOrgToModelConverter(globalIdLoader, orgLoader);
		
		expect(globalIdLoader.setGlobalId(view.getGlobalId())).andReturn(globalIdLoader);
		expect(globalIdLoader.load(trans)).andReturn(editOrg);
		
		replay(globalIdLoader);
		replay(orgLoader);
		
		CustomerOrg model = converter.toModel(view, trans);
		
		verifyModel(model, view, editOrg.getPrimaryOrg());
		
		assertEquals(editOrg.getId(), editOrg.getId());
		
		verify(globalIdLoader);
		verify(orgLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_edit_does_changes_parent_org_when_chagned() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<CustomerOrg> globalIdLoader = createMock(GlobalIdLoader.class);
		InternalOrgByNameLoader orgLoader = createMock(InternalOrgByNameLoader.class);
		
		PrimaryOrg oldParent = OrgBuilder.aPrimaryOrg().withName("old parent").buildPrimary();
		PrimaryOrg newParent = OrgBuilder.aPrimaryOrg().withName("new parent").buildPrimary();
		
		CustomerOrg editOrg =  OrgBuilder.aCustomerOrg().withParent(oldParent).buildCustomer();
		
		FullExternalOrgView view = FullExternalOrgViewBuilder.aCustomerView().forEdit().withTestData().build();
		view.setParentOrg(newParent.getName());
		
		CustomerOrgToModelConverter converter = new CustomerOrgToModelConverter(globalIdLoader, orgLoader);
		
		expect(globalIdLoader.setGlobalId(view.getGlobalId())).andReturn(globalIdLoader);
		expect(globalIdLoader.load(trans)).andReturn(editOrg);
		
		expect(orgLoader.setName(view.getParentOrg())).andReturn(orgLoader);
		expect(orgLoader.load(trans)).andReturn(newParent);
		
		replay(globalIdLoader);
		replay(orgLoader);
		
		CustomerOrg model = converter.toModel(view, trans);
		
		verifyModel(model, view, newParent);
		
		assertEquals(editOrg.getId(), editOrg.getId());
		
		verify(globalIdLoader);
		verify(orgLoader);
	}
	
	private void verifyModel(CustomerOrg model, FullExternalOrgView view, InternalOrg parentOrg) {
		assertNotNull(model);
		assertNotNull(model.getContact());
		assertNotNull(model.getAddressInfo());
		assertEquals(view.getName(), model.getName());
		assertEquals(view.getCode(), model.getCode());
		assertEquals(parentOrg, model.getParent());
		assertEquals(parentOrg.getTenant(), model.getTenant());
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
