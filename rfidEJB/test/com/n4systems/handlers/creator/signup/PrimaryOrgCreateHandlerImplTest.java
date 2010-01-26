package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;



public class PrimaryOrgCreateHandlerImplTest extends TestUsesTransactionBase {

	@Before
	public void setup() {
		mockTransaction();
	}

	@Test(expected=InvalidArgumentException.class)
	public void should_throw_exception_if_tenant_is_not_set() {
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(null);
		sut.forAccountInfo(new AccountCreationInformationStub());
		
		sut.create(mockTransaction);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_throw_exception_if_account_info_is_not_set() {
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(null);
		sut.forTenant(aTenant().build());
		
		sut.create(mockTransaction);
	}
	
	
	
	@Test
	public void should_fill_in_address() {
		Tenant tenant = aTenant().build();
		
		AccountCreationInformationStub accountInfo = new AccountCreationInformationStub();
		com.n4systems.subscription.AddressInfo address = new com.n4systems.subscription.AddressInfo();
		address.setAddressLine1("street Address1");
		address.setAddressLine2("street Address2");
		address.setCity("city");
		address.setState("state");
		address.setCountry("country short name");
		address.setCountryFullName("country full name");
		address.setPostal("zip");
		accountInfo.setBillingAddress(address);
		accountInfo.setPhone("phone1");
		
		Capture<PrimaryOrg> capturedPrimaryOrg = new Capture<PrimaryOrg>(); 
		
		OrgSaver mockOrgSaver = createMock(OrgSaver.class);
		mockOrgSaver.save(same(mockTransaction), capture(capturedPrimaryOrg));
		replay(mockOrgSaver);
		
		
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(mockOrgSaver);
		sut.forTenant(tenant).forAccountInfo(accountInfo);
		sut.create(mockTransaction);
		
		PrimaryOrg createdPrimaryOrg = capturedPrimaryOrg.getValue();
		
		
		
		AddressInfo expectedAddress = AddressInfoBuilder.anAddress()
												.streetAddress("street Address1" + " " + "street Address2")
												.city("city")
												.state("state")
												.country("country full name")
												.zip("zip")
												.phone1("phone1").build();
		
		
		assertAddressEquals(expectedAddress, createdPrimaryOrg.getAddressInfo());
		
	}
	
	private void assertAddressEquals(AddressInfo expectedAddress, AddressInfo actualAddress) {
		assertAddressEquals("address " + expectedAddress + " is not equal to " + actualAddress, expectedAddress, actualAddress);
	}

	private void assertAddressEquals(String message, AddressInfo expectedAddress, AddressInfo actualAddress) {
		assertEquals("street address " + message, expectedAddress.getStreetAddress(), actualAddress.getStreetAddress());
		assertEquals("city " + message, expectedAddress.getCity(), actualAddress.getCity());
		assertEquals("state " + message, expectedAddress.getState(), actualAddress.getState());
		assertEquals("country  " + message, expectedAddress.getCountry(), actualAddress.getCountry());
		assertEquals("zip " + message, expectedAddress.getZip(), actualAddress.getZip());
		assertEquals("phone 1 " + message, expectedAddress.getPhone1(), actualAddress.getPhone1());
		
		
	}
	
	

}
