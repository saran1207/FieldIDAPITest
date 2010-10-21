package com.n4systems.model.utils;

import static com.n4systems.model.builders.InfoFieldBuilder.*;
import static com.n4systems.model.builders.AssetTypeBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;



public class CleanProductTypeFactoryTest {

	@Test
	public void test_copy_basic() {
		Tenant n4 = aTenant().named("n4").build();
		AssetType originalAssetType = anAssetType().named("chain").build();
		
		CleanProductTypeFactory sut = new CleanProductTypeFactory(originalAssetType, n4);
		
		sut.clean();
		
		assertTrue(originalAssetType.isNew());
		assertNull(originalAssetType.getImageName());
		assertTrue(originalAssetType.getSchedules().isEmpty());
	}
	
	@Test
	public void test_copying_info_feilds() {
		Tenant n4 = aTenant().named("n4").build();
		List<InfoFieldBean> infoFields =  new ArrayList<InfoFieldBean>();
		infoFields.add(anInfoField().build());
		
		AssetType originalAssetType = anAssetType().withFields(infoFields.get(0)).build();
		
		CleanProductTypeFactory sut = new CleanProductTypeFactory(originalAssetType, n4);
		
		sut.cleanInfoFields();
		
		assertEquals(infoFields.size(), originalAssetType.getInfoFields().size());
		for (InfoFieldBean infoFieldBean : originalAssetType.getInfoFields()) {
			assertEquals(infoFields.iterator().next().getName(), infoFieldBean.getName());
			assertNull(infoFieldBean.getUniqueID());
		}
		
	}

}
