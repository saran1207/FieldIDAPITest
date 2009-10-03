package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;



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
	public void should_create_new_primary_org() {
		Tenant tenant = aTenant().build();
		
		AccountCreationInformationStub accountInfo = new AccountCreationInformationStub();
		accountInfo.setCompanyName("some company").setTenantName("some-tenant").setFullTimeZone("Cananda:Ontario - Toronto")
				.setSignUpPackage(new SignUpPackage(SignUpPackageDetails.Basic, new ArrayList<ContractPricing>())).setNumberOfUsers(10)
				.setEmail("someemail@email.com");
		
		
		Capture<PrimaryOrg> capturedPrimaryOrg = new Capture<PrimaryOrg>(); 
		
		OrgSaver mockOrgSaver = createMock(OrgSaver.class);
		mockOrgSaver.save(same(mockTransaction), capture(capturedPrimaryOrg));
		replay(mockOrgSaver);
		
		
		
		
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(mockOrgSaver);
		sut.forTenant(tenant).forAccountInfo(accountInfo);
		
		sut.create(mockTransaction);
		
		PrimaryOrg createdPrimaryOrg = capturedPrimaryOrg.getValue();
		
		assertEquals("some company", createdPrimaryOrg.getDisplayName());
		assertEquals("Cananda:Ontario - Toronto", createdPrimaryOrg.getDefaultTimeZone());
		assertNotNull(createdPrimaryOrg.getExternalPassword());
		assertEquals("someemail@email.com", createdPrimaryOrg.getExternalUserName());
	}

}
