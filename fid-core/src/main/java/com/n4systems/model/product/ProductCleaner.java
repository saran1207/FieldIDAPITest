package com.n4systems.model.product;

import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.Product;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.infooption.InfoOptionCleaner;

public class ProductCleaner extends AbstractEntityCleaner<Product> {
	
	private final Cleaner<InfoOptionBean> infoOptionCleaner;
	private final Cleaner<ProductSerialExtensionValueBean> productSerialExtensionValueCleaner;
	
	public ProductCleaner() {
		this(new InfoOptionCleaner(), new ProductSerialExtensionValueCleaner());
	}
	
	public ProductCleaner(Cleaner<InfoOptionBean> infoOptionCleaner, Cleaner<ProductSerialExtensionValueBean> productSerialExtensionValueCleaner) {
		this.infoOptionCleaner = infoOptionCleaner;
		this.productSerialExtensionValueCleaner = productSerialExtensionValueCleaner;
	}
	
	@Override
	public void clean(Product product) {
		super.clean(product);
		
		for (InfoOptionBean option: product.getInfoOptions()) {
			// we only want to clean non static options
			if (!option.isStaticData()) {
				infoOptionCleaner.clean(option);
			}
		}
		
		for (ProductSerialExtensionValueBean extension: product.getProductSerialExtensionValues()) {
			productSerialExtensionValueCleaner.clean(extension);
		}
	}

}
