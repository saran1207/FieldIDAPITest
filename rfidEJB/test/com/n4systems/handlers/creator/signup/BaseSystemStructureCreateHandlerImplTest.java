package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.Tenant;

public class BaseSystemStructureCreateHandlerImplTest extends TestUsesTransactionBase {


	@Before
	public void setup() {
		mockTransaction();
	}


	@Test(expected = InvalidArgumentException.class)
	public void should_throw_exception_if_no_tenant_is_provided() {
		BaseSystemStructureCreateHandlerImpl sut = new BaseSystemStructureCreateHandlerImpl(null, null);
		sut.create(mockTransaction);
	}

	@Test
	public void should_create_default_tag_options_and_setup_data_and_serial_number_format_for_tenant() {
		Tenant tenant = n4();

		BaseSystemTenantStructureCreateHandler mockTenantStructureCreateHandler = createMock(BaseSystemTenantStructureCreateHandler.class);
		expect(mockTenantStructureCreateHandler.forTenant(tenant)).andReturn(mockTenantStructureCreateHandler);
		mockTenantStructureCreateHandler.create(mockTransaction);
		replay(mockTenantStructureCreateHandler);

		BaseSystemSetupDataCreateHandler mockSetupDataCreateHandler = createMock(BaseSystemSetupDataCreateHandler.class);
		expect(mockSetupDataCreateHandler.forTenant(tenant)).andReturn(mockSetupDataCreateHandler);
		mockSetupDataCreateHandler.create(mockTransaction);
		replay(mockSetupDataCreateHandler);

		BaseSystemStructureCreateHandlerImpl sut = new BaseSystemStructureCreateHandlerImpl(mockTenantStructureCreateHandler, mockSetupDataCreateHandler);
	
		sut.forTenant(tenant).create(mockTransaction);
		
		verify(mockTenantStructureCreateHandler);
		verify(mockSetupDataCreateHandler);
		
	}
}
