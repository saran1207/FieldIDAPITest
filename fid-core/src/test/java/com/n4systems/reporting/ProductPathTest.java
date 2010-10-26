package com.n4systems.reporting;

import static com.n4systems.model.builders.AssetBuilder.*;
import static org.junit.Assert.*;

import java.io.File;

import com.n4systems.model.Asset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.product.AssetAttachment;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;



public class ProductPathTest {
private ConfigContext oldContext;

	@Before
	public void changeConfigContext() {
		oldContext = ConfigContext.getCurrentContext();
		ConfigContext.setCurrentContext(new ConfigContextOverridableTestDouble());
	}
	
	@After 
	public void removeConfig() {
		ConfigContext.setCurrentContext(oldContext);
	}
		
	
	
	@Test 
	public void should_get_product_attachment_file() {
		Asset asset = anAsset().build();
		asset.setCreated(DateHelper.string2Date("yyyy-MM-dd h:mm a", "2009-06-01 12:01 pm"));
		asset.setId(2L);
		AssetAttachment attachment = new AssetAttachment();
		attachment.setId(1L);
		attachment.setAsset(asset);
		attachment.setTenant(asset.getTenant());
		attachment.setFileName("test.file");
		
		assertEquals(new File("/var/fieldid/private/products/attachments/" + asset.getTenant().getName() + "/09/06/2/1/test.file"), PathHandler.getProductAttachmentFile(attachment));
	}
	
}

