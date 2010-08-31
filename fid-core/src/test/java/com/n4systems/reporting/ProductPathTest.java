package com.n4systems.reporting;

import static com.n4systems.model.builders.ProductBuilder.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.product.ProductAttachment;
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
		Product product = aProduct().build();
		product.setCreated(DateHelper.string2Date("yyyy-MM-dd h:mm a", "2009-06-01 12:01 pm"));
		product.setId(2L);
		ProductAttachment attachment = new ProductAttachment();
		attachment.setId(1L);
		attachment.setProduct(product);
		attachment.setTenant(product.getTenant());
		attachment.setFileName("test.file");
		
		assertEquals(new File("/var/fieldid/private/products/attachments/" + product.getTenant().getName() + "/09/06/2/1/test.file"), PathHandler.getProductAttachmentFile(attachment));	
	}
	
}

