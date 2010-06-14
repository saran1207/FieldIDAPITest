package com.n4systems.servicedto.converts;

import static com.n4systems.model.builders.ProductServiceDTOBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class PopulateAssignedUserImplTest {

	private Product product;
	private ProductServiceDTO productServiceDTO;
	private User assignedUser;
	
	@Before
	public void setUp() {
		
		product = ProductBuilder.aProduct().build();
		productServiceDTO = aProductServiceDTO().withAssignedUserId(2L).build();
		assignedUser = UserBuilder.aUser().withId(5L).build();
	}
	
	@Test
	public void should_set_assigned_user_to_product_if_productservicedto_has_assignedid() throws Exception {
		
		TestDoubleLoaderFactory testDoubleLoaderFactory = userFilteredLoaderMock();
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(testDoubleLoaderFactory);
		
		sut.convert(productServiceDTO, product);
		
		Assert.assertEquals(product.getAssignedUser(), assignedUser);
		
	}

	@Test
	public void should_not_set_assinged_user_if_productservicedto_does_not_have_assignedid() throws Exception {
	
		productServiceDTO = aProductServiceDTO().build();
		
		TestDoubleLoaderFactory testDoubleLoaderFactory = userFilteredLoaderMock();
		
		PopulateAssignedUserConverter sut = new PopulateAssignedUserConverter(testDoubleLoaderFactory);
		
		sut.convert(productServiceDTO, product);
		
		Assert.assertEquals(product.getAssignedUser(), null);
		
	}
	
	@SuppressWarnings("unchecked")
	private TestDoubleLoaderFactory userFilteredLoaderMock() {
		
		UserFilteredLoader userFilteredLoader = createMock(UserFilteredLoader.class);
		expect(userFilteredLoader.setId(productServiceDTO.getAssignedUserId())).andReturn(userFilteredLoader);
		expect(userFilteredLoader.load()).andReturn(assignedUser);
		replay(userFilteredLoader);
		
		TestDoubleLoaderFactory testDoubleLoaderFactory = new TestDoubleLoaderFactory(new TenantOnlySecurityFilter(1L));
		testDoubleLoaderFactory.setUserFileterLoader(userFilteredLoader);
		return testDoubleLoaderFactory;
		
	}

	
}
