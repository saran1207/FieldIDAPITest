package com.n4systems.handlers.creator.signup;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContextRequiredTestCase;


public class AccountPlaceHolderCreateHandlerImplTest extends ConfigContextRequiredTestCase {

	protected Transaction mockTransaction;
	
	@Before
	public void setup() {
		mockTransaction();
	}

	protected void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_throw_exception_if_no_account_creation_info_given() {
		AccountPlaceHolderCreateHandler sut = new AccountPlaceHolderCreateHandlerImpl(null, null, null);
		sut.createWithUndoInformation(mockTransaction);
	}
	
	@Test
	public void should_create_tenant_primary_org_admin_user_and_system_user() {
		AccountCreationInformation accountInformation = new AccountCreationInformationStub().setTenantName("some_tenant");
		
		Capture<Tenant> createdTenant = new Capture<Tenant>();
		
		TenantSaver mockTenantSaver = createMock(TenantSaver.class);
		mockTenantSaver.save(same(mockTransaction), capture(createdTenant));
		replay(mockTenantSaver);
		
		Capture<Tenant> passedTenant = new Capture<Tenant>();
		PrimaryOrgCreateHandler mockPrimaryOrgCreator = createMock(PrimaryOrgCreateHandler.class);
		expect(mockPrimaryOrgCreator.forAccountInfo(accountInformation)).andReturn(mockPrimaryOrgCreator);
		expect(mockPrimaryOrgCreator.forTenant(capture(passedTenant))).andReturn(mockPrimaryOrgCreator);
		expect(mockPrimaryOrgCreator.createWithUndoInformation(mockTransaction)).andReturn(new PrimaryOrg());
		replay(mockPrimaryOrgCreator);
		
		UserSaver mockUserSaver = createMock(UserSaver.class);
		mockUserSaver.save(same(mockTransaction), isA(UserBean.class));
		expectLastCall().times(2);
		replay(mockUserSaver);
		
		
		AccountPlaceHolderCreateHandler sut = new AccountPlaceHolderCreateHandlerImpl(mockTenantSaver, mockPrimaryOrgCreator, mockUserSaver);
		sut.forAccountInfo(accountInformation);
		
		sut.createWithUndoInformation(mockTransaction);
		
		verify(mockTenantSaver);
		verify(mockPrimaryOrgCreator);
		verify(mockUserSaver);
	}
	
	
	
	
	
	
}
