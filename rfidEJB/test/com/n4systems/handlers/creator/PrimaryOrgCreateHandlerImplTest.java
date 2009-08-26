package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DataUnit;



public class PrimaryOrgCreateHandlerImplTest {

	private Transaction mockTransaction;

	@Before
	public void setup() {
		mockTransaction();
	}

	private void mockTransaction() {
		mockTransaction = createMock(Transaction.class);
		replay(mockTransaction);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_throw_exception_if_tenant_is_not_set() {
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(null, null);
		sut.forAccountInfo(new AccountCreationInformationStub());
		
		sut.create(mockTransaction);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_throw_exception_if_account_info_is_not_set() {
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(null, null);
		sut.forTenant(aTenant().build());
		
		sut.create(mockTransaction);
	}
	
	
	
	@Test
	public void should_create_new_primary_org() {
		Tenant tenant = aTenant().build();
		
		AccountCreationInformationStub accountInfo = new AccountCreationInformationStub();
		accountInfo.setCompanyName("some company").setTenantName("some-tenant").setFullTimeZone("Cananda:Ontario - Toronto")
				.setSignUpPackage(SignUpPackage.Basic).setNumberOfUsers(10);
		
		
		Capture<PrimaryOrg> capturedPrimaryOrg = new Capture<PrimaryOrg>(); 
		
		OrganizationSaver mockOrgSaver = createMock(OrganizationSaver.class);
		mockOrgSaver.save(same(mockTransaction), capture(capturedPrimaryOrg));
		replay(mockOrgSaver);
		
		UserSaver mockUserSaver = createMock(UserSaver.class);
		mockUserSaver.save(same(mockTransaction), isA(UserBean.class));
		expectLastCall().times(2);
		replay(mockUserSaver);
		
		
		
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(mockOrgSaver, mockUserSaver);
		sut.forTenant(tenant).forAccountInfo(accountInfo);
		
		sut.create(mockTransaction);
		
		PrimaryOrg createdPrimaryOrg = capturedPrimaryOrg.getValue();
		
		assertEquals("some company", createdPrimaryOrg.getDisplayName());
		assertEquals("Cananda:Ontario - Toronto", createdPrimaryOrg.getDefaultTimeZone());
		assertEquals(new HashSet<ExtendedFeature>(Arrays.asList(SignUpPackage.Basic.getExtendedFeatures())), createdPrimaryOrg.getExtendedFeatures());
		
		
		assertEquals(SignUpPackage.Basic.getAssets(), createdPrimaryOrg.getLimits().getAssets());
		assertEquals(new Long(10), createdPrimaryOrg.getLimits().getUsers());
		assertEquals(SignUpPackage.Basic.getDiskSpaceInMB(), new Long(DataUnit.convert(BigInteger.valueOf(createdPrimaryOrg.getLimits().getDiskSpaceInBytes()), DataUnit.BYTES, DataUnit.MEGABYTES).longValue()));
		
		
	}

}
