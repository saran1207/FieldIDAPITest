package com.n4systems.model.utils;

import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import org.junit.Test;
import rfid.ejb.entity.InfoFieldBean;

import java.util.ArrayList;
import java.util.List;

import static com.n4systems.model.builders.AssetTypeBuilder.anAssetType;
import static com.n4systems.model.builders.InfoFieldBuilder.anInfoField;
import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static org.junit.Assert.*;



public class CleanAssetTypeFactoryTest {

	@Test
	public void test_copy_basic() {
		Tenant n4 = aTenant().named("n4").build();
		AssetType originalAssetType = anAssetType().named("chain").build();
		
		CleanAssetTypeFactory sut = new CleanAssetTypeFactory(originalAssetType, n4);
		
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
		
		CleanAssetTypeFactory sut = new CleanAssetTypeFactory(originalAssetType, n4);
		
		sut.cleanInfoFields();
		
		assertEquals(infoFields.size(), originalAssetType.getInfoFields().size());
		for (InfoFieldBean infoFieldBean : originalAssetType.getInfoFields()) {
			assertEquals(infoFields.iterator().next().getName(), infoFieldBean.getName());
			assertNull(infoFieldBean.getUniqueID());
		}
		
	}

}
