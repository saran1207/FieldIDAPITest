package com.n4systems.servicedto.converts;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.servicedto.builders.InspectionServiceDTOBuilder.*;
import static com.n4systems.servicedto.builders.ProductServiceDTOBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class PopulateAssignedUserConverterTest {

	private static final LoaderFactory UNUSED_LOADER_FACTORY = null;
	private static final Long INVALID_SERVER_ID = 0L;
	private static final User MISSING_USER = null;
	private final Product product = aProduct().build();
	private final User assignedUser = aUser().withId(5L).build();
	
	
	
	@Test
	public void should_set_assigned_user_to_product_if_productservicedto_has_assignedid() throws Exception {
		ProductServiceDTO productServiceDTO = aProductServiceDTO().withAssignedUserId(2L).build();
		LoaderFactory loaderFactory = loaderFactoryWith(createUserFilteredLoaderFor(assignedUser));
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(loaderFactory);
		
		sut.convert(productServiceDTO, product);
		
		assertEquals(product.getAssignedUser(), assignedUser);
		
	}

	@Test
	public void should_set_assinged_user_to_unassigned_if_productservicedto_does_not_have_assignedid_as_a_valid_server_id() throws Exception {
	
		ProductServiceDTO productServiceDTO = aProductServiceDTO().build();
		
		LoaderFactory loaderFactory = loaderFactoryWith(createUserFilteredLoaderFor(assignedUser));
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(loaderFactory);
		
		sut.convert(productServiceDTO, product);
		
		assertNull(product.getAssignedUser());
	}
	
	
	
	private LoaderFactory loaderFactoryWith(UserFilteredLoader userFilteredLoader) {
		
		TestDoubleLoaderFactory testDoubleLoaderFactory = new TestDoubleLoaderFactory(new TenantOnlySecurityFilter(1L));
		testDoubleLoaderFactory.setUserFileterLoader(userFilteredLoader);
		return testDoubleLoaderFactory;
		
	}

	
	
	
	
	
	@Test
	public void should_not_apply_an_assign_to_update_when_the_assignment_is_not_included_on_the_dto() throws Exception {
		InspectionServiceDTO inspectionServiceDTO = anInspectionServiceDTO().withNoAssignmentIncluded().build();
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(UNUSED_LOADER_FACTORY);
		
		Inspection targetInspection = anInspection().build();
		sut.convert(inspectionServiceDTO, targetInspection);
		
		
		assertThat(targetInspection.hasAssignToUpdate(), is(false));
	}

	@Test
	public void should_apply_an_assign_to_update_to_unassigned_when_the_assignment_is_included_on_the_dto_and_the_assigned_user_is_not_a_valid_server_id() throws Exception {
		InspectionServiceDTO inspectionServiceDTO = anInspectionServiceDTO().withAssignmentTo(INVALID_SERVER_ID).build();
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(UNUSED_LOADER_FACTORY);
		
		Inspection targetInspection = anInspection().build();
		sut.convert(inspectionServiceDTO, targetInspection);
		
		
		assertThat(targetInspection.getAssignedTo(), equalTo(assignmentToUnassigned()));
	}
	
	
	
	@Test
	public void should_fetch_assigned_user_from_a_the_user_loader() throws Exception {
		InspectionServiceDTO inspectionServiceDTO = anInspectionServiceDTO().withAssignmentTo(assignedUser.getId()).build();
		
		UserFilteredLoader loader = createMock(UserFilteredLoader.class);
		expect(loader.setId(assignedUser.getId())).andReturn(loader);
		expect(loader.load()).andReturn(assignedUser);
		replay(loader);
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(loaderFactoryWith(loader));
		
		Inspection targetInspection = anInspection().build();
		
		sut.convert(inspectionServiceDTO, targetInspection);
		
		
		verify(loader);
	}
	
	
	@Test
	public void should_set_the_create_the_assigned_to_update_with_the_assigned_user() throws Exception {
		InspectionServiceDTO inspectionServiceDTO = anInspectionServiceDTO().withAssignmentTo(assignedUser.getId()).build();
		
		UserFilteredLoader loader = createUserFilteredLoaderFor(assignedUser);
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(loaderFactoryWith(loader));
		
		Inspection targetInspection = anInspection().build();
		
		sut.convert(inspectionServiceDTO, targetInspection);
		
		assertThat(targetInspection.getAssignedTo(), equalTo(assigmentTo(assignedUser)));
	}
	
	
	private AssignedToUpdate assigmentTo(User assignedUser) {
		return AssignedToUpdate.assignAssetToUser(assignedUser);
	}

	@Test(expected=ConversionException.class)
	public void should_throw_conversion_exception_if_assigned_to_user_can_not_be_laoded() throws Exception {
		InspectionServiceDTO inspectionServiceDTO = anInspectionServiceDTO().withAssignmentTo(assignedUser.getId()).build();
		
		
		UserFilteredLoader loader = createUserFilteredLoaderFor(MISSING_USER);
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(loaderFactoryWith(loader));
		
		Inspection targetInspection = anInspection().build();
		
		sut.convert(inspectionServiceDTO, targetInspection);
		
	}

	
	
	private UserFilteredLoader createUserFilteredLoaderFor(User assignedUser) {
		UserFilteredLoader loader = createMock(UserFilteredLoader.class);
		expect(loader.setId(anyLong())).andReturn(loader);
		expect(loader.load()).andReturn(assignedUser);
		replay(loader);
		
		return loader;
	}
	
	

	private AssignedToUpdate assignmentToUnassigned() {
		return AssignedToUpdate.unassignAsset();
	}
}
