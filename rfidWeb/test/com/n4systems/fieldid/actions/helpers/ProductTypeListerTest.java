package com.n4systems.fieldid.actions.helpers;

import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;



public class ProductTypeListerTest {

	private static final String GROUP_1 = "group 1";
	private List<ProductTypeGroup> groups;
	private List<ProductType> types;
	private ProductTypeLister sut;
	
	
	@Before 
	public void setup() {
		ProductTypeGroup group = createProductTypeGroup();
		
		groups = ImmutableList.of(group);;
		
		
		
		types = ImmutableList.of(aProductType().named("type 1").withGroup(group).build(),
						aProductType().named("type 2").build()
					);
		
		sut = new ProductTypeLister(persistenceManagerToLoadProductTypesAndGroups(), new OpenSecurityFilter());
	}

	private ProductTypeGroup createProductTypeGroup() {
		ProductTypeGroup group = new ProductTypeGroup();
		group.setName(GROUP_1);
		group.setId(1L);
		return group;
	}

	@SuppressWarnings("unchecked")
	private PersistenceManager persistenceManagerToLoadProductTypesAndGroups() {
		PersistenceManager persistenceManager = createMock(PersistenceManager.class);
		expect(persistenceManager.findAll((QueryBuilder<ProductTypeGroup>) anyObject())).andReturn(groups);
		expect(persistenceManager.findAllLP(same(ProductType.class), (SecurityFilter)anyObject(), same("name"))).andReturn(ListHelper.longListableToListingPair(types));
		expect(persistenceManager.findAllLP(same(ProductType.class), (SecurityFilter)anyObject(), same("group.name"))).andReturn(ImmutableList.of(new ListingPair(types.get(0).getId(), GROUP_1)));
		replay(persistenceManager);
		return persistenceManager;
	}
	
	
	@Test
	public void should_get_list_of_all_product_type_groups() throws Exception {
		List<String> expectedGroups = new FluentArrayList<String>(GROUP_1);
		
		List<String> actualGroups = sut.getGroups();
		
		assertArrayEquals(expectedGroups.toArray(), actualGroups.toArray());
	}
	
	
	@Test
	public void should_get_list_of_all_product_types() throws Exception {
		List<ListingPair> expectedTypes = ListHelper.longListableToListingPair(types);
		
		List<ListingPair> actualTypes = sut.getProductTypes();
		
		assertArrayEquals(expectedTypes.toArray(), actualTypes.toArray());
	}
	
	
	@Test
	public void should_find_product_types_for_group() throws Exception {
		List<ListingPair> expectedTypes = ListHelper.longListableToListingPair(types.subList(0, 1));
	
		List<ListingPair> actualTypes = sut.getGroupedProductTypes(GROUP_1);
		
		assertArrayEquals(expectedTypes.toArray(), actualTypes.toArray());
	}
	
	@Test
	public void should_find_product_types_for_group_by_id() throws Exception {
		List<ListingPair> expectedTypes = ListHelper.longListableToListingPair(types.subList(0, 1));
		
		List<ListingPair> actualTypes = sut.getGroupedProductTypesById(groups.get(0).getId());
		
		assertArrayEquals(expectedTypes.toArray(), actualTypes.toArray());
	}
	
	@Test
	public void should_find_product_types_for_all_group_by_id() throws Exception {
		List<ListingPair> expectedTypes = ListHelper.longListableToListingPair(types.subList(0, 1));
		
		List<ListingPair> actualTypes = sut.getGroupedProductTypesById(groups.get(0).getId());
		
		assertArrayEquals(expectedTypes.toArray(), actualTypes.toArray());
	}
	
}
