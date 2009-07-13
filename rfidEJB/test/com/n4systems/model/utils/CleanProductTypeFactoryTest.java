package com.n4systems.model.utils;

import static com.n4systems.model.builders.InfoFieldBuilder.*;
import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;



public class CleanProductTypeFactoryTest {

	@Test
	public void test_copy_basic() {
		TenantOrganization n4 = aTenant().named("n4").build();
		ProductType originalProductType = aProductType().named("chain").build();
		
		CleanProductTypeFactory sut = new CleanProductTypeFactory(originalProductType, n4);
		
		sut.clean();
		
		assertTrue(originalProductType.isNew());
		assertNull(originalProductType.getImageName());
		assertTrue(originalProductType.getSchedules().isEmpty());
	}
	
	@Test
	public void test_copying_info_feilds() {
		TenantOrganization n4 = aTenant().named("n4").build();
		List<InfoFieldBean> infoFields =  new ArrayList<InfoFieldBean>();
		infoFields.add(anInfoField().build());
		
		ProductType originalProductType = aProductType().withFields(infoFields.get(0)).build();
		
		CleanProductTypeFactory sut = new CleanProductTypeFactory(originalProductType, n4);
		
		sut.cleanInfoFields();
		
		assertEquals(infoFields.size(), originalProductType.getInfoFields().size());
		for (InfoFieldBean infoFieldBean : originalProductType.getInfoFields()) {
			assertEquals(infoFields.iterator().next().getName(), infoFieldBean.getName());
			assertNull(infoFieldBean.getUniqueID());
		}
		
	}

}
