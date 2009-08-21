package com.n4systems.handlers.creator;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;


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
		accountInfo.setCompanyName("some company").setTenantName("Canada: Toronto");
		
		OrganizationSaver mockOrgSaver = createMock(OrganizationSaver.class);
		mockOrgSaver.save(same(mockTransaction), isA(PrimaryOrg.class));
		replay(mockOrgSaver);
		
		UserSaver mockUserSaver = createMock(UserSaver.class);
		mockUserSaver.save(same(mockTransaction), isA(UserBean.class));
		expectLastCall().times(2);
		replay(mockUserSaver);
		
		PrimaryOrgCreateHandler sut = new PrimaryOrgCreateHandlerImpl(mockOrgSaver, mockUserSaver);
		sut.forTenant(tenant).forAccountInfo(accountInfo);
		
		sut.create(mockTransaction);
		
		verify(mockOrgSaver);
		verify(mockUserSaver);
	}

}
