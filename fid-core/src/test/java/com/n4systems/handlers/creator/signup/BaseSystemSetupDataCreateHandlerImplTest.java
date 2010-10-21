package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.AssetType;
import com.n4systems.model.StateSet;
import com.n4systems.model.TagOption;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.productstatus.ProductStatusSaver;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;



public class BaseSystemSetupDataCreateHandlerImplTest extends TestUsesTransactionBase {


	@Before
	public void setup() {
		mockTransaction();
	}

	@Test(expected = InvalidArgumentException.class)
	public void should_throw_exception_if_no_tenant_is_provided() {
		BaseSystemSetupDataCreateHandler sut = new BaseSystemSetupDataCreateHandlerImpl(null, null, null, null, null);
		sut.create(mockTransaction);
	}
	
	@Test
	public void should_create_default_product_type_and_itgs_and_tag_option_and_state_sets() {
		Tenant tenant = aTenant().build();
		
		TagOptionSaver mockTagSaver = createMock(TagOptionSaver.class);
		mockTagSaver.save(same(mockTransaction), isA(TagOption.class));
		replay(mockTagSaver);
		
		ProductTypeSaver mockProductTypeSaver = createMock(ProductTypeSaver.class);
		mockProductTypeSaver.save(same(mockTransaction), isA(AssetType.class));
		replay(mockProductTypeSaver);

		InspectionTypeGroupSaver mockInspectionTypeGroupSaver = createMock(InspectionTypeGroupSaver.class);
		mockInspectionTypeGroupSaver.save(same(mockTransaction), isA(InspectionTypeGroup.class));
		replay(mockInspectionTypeGroupSaver);

		StateSetSaver mockStateSetSaver = createMock(StateSetSaver.class);
		mockStateSetSaver.save(same(mockTransaction), isA(StateSet.class));
		expectLastCall().times(2);
		replay(mockStateSetSaver);
		
		ProductStatusSaver mockStatusSaver = createMock(ProductStatusSaver.class);
		mockStatusSaver.save(same(mockTransaction), isA(AssetStatus.class));
		expectLastCall().times(5);
		replay(mockStatusSaver);
		
		BaseSystemSetupDataCreateHandler sut = new BaseSystemSetupDataCreateHandlerImpl(mockTagSaver, mockProductTypeSaver, mockInspectionTypeGroupSaver, mockStateSetSaver, mockStatusSaver);
		sut.forTenant(tenant).create(mockTransaction);
		
		verify(mockTagSaver);
		verify(mockProductTypeSaver);
		verify(mockInspectionTypeGroupSaver);
		verify(mockStateSetSaver);
		verify(mockStatusSaver);
	}
	

	
}
