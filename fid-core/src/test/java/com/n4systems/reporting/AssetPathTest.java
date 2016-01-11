package com.n4systems.reporting;

import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.services.config.ConfigServiceTestManager;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.DateHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.n4systems.model.builders.AssetBuilder.anAsset;



public class AssetPathTest {

	@Before
	public void changeConfigContext() {
		ConfigServiceTestManager.setInstance(new ConfigContextOverridableTestDouble());
	}
	
	@After 
	public void removeConfig() {
		ConfigServiceTestManager.resetInstance();
	}

	@Test 
	public void should_get_asset_attachment_file() {
		Asset asset = anAsset().forTenant(TenantBuilder.n4()).build();
		asset.setCreated(DateHelper.string2Date("yyyy-MM-dd h:mm a", "2009-06-01 12:01 pm"));
		asset.setId(2L);
		AssetAttachment attachment = new AssetAttachment();
		attachment.setId(1L);
		attachment.setAsset(asset);
		attachment.setTenant(asset.getTenant());
		attachment.setFileName("test.file");
		
		//assertEquals(new File("/var/fieldid/private/products/attachments/" + asset.getTenant().getName() + "/09/06/2/1/test.file"), PathHandler.getAssetAttachmentFile(attachment));
	}
	
}

