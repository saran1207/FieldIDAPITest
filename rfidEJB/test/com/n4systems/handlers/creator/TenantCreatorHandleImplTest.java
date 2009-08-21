package com.n4systems.handlers.creator;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.persistence.Transaction;


public class TenantCreatorHandleImplTest {

	@Test(expected=InvalidArgumentException.class)
	public void should_require_an_account_info() {
		TenantCreateHandler sut = new TenantCreatorHandleImpl(null);
		sut.create(null);
	}
	
	@Test
	public void should_save_tenant_and_set_up_base_required_structure() {
		Transaction mockTransaction = createNiceMock(Transaction.class);
		replay(mockTransaction);
		AccountCreationInformationStub accountInfo = new AccountCreationInformationStub();
		
		TenantSaver mockSaver = createMock(TenantSaver.class);
		mockSaver.save(same(mockTransaction), (Tenant)anyObject());
		replay(mockSaver);
		
		
		
		TenantCreateHandler sut = new TenantCreatorHandleImpl(mockSaver);
		sut.forAccountInfo(accountInfo);
		sut.create(mockTransaction);
		
		verify(mockSaver);
	}
}
