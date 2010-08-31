package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.Tenant;
import com.n4systems.model.serialnumbercounter.SerialNumberCounterSaver;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;



public class BaseSystemTenantStructureCreateHandlerImplTest extends TestUsesTransactionBase {


	@Before
	public void setup() {
		mockTransaction();
	}


	@Test(expected = InvalidArgumentException.class)
	public void should_throw_exception_if_no_tenant_is_provided() {
		BaseSystemTenantStructureCreateHandler sut = new BaseSystemTenantStructureCreateHandlerImpl(null, null);
		sut.create(mockTransaction);
	}
	
	@Test
	public void should_create_default_tag_option_and_mod_date_structure() {
		Tenant tenant = aTenant().build();
		
		SetupDataLastModDatesSaver mockModDateSaver = createMock(SetupDataLastModDatesSaver.class);
		mockModDateSaver.save(same(mockTransaction), isA(SetupDataLastModDates.class));
		replay(mockModDateSaver);
		
		SerialNumberCounterSaver mockSerialNumberSaver = createMock(SerialNumberCounterSaver.class);
		mockSerialNumberSaver.save(same(mockTransaction), isA(SerialNumberCounterBean.class));
		replay(mockSerialNumberSaver);
		
		BaseSystemTenantStructureCreateHandler sut = new BaseSystemTenantStructureCreateHandlerImpl(mockModDateSaver, mockSerialNumberSaver);
		sut.forTenant(tenant).create(mockTransaction);
		
		verify(mockModDateSaver);
		verify(mockSerialNumberSaver);
	}
	
	
}
