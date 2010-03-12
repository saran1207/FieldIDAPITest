package com.n4systems.api.conversion.product;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.model.Product;
import com.n4systems.model.infooption.InfoOptionMapConverter;


public class ProductToViewConverter implements ModelToViewConverter<Product, ProductView> {
	private final InfoOptionMapConverter optionConverter;
	
	public ProductToViewConverter() {
		this(new InfoOptionMapConverter());	
	}
	
	public ProductToViewConverter(InfoOptionMapConverter optionConverter) {
		this.optionConverter = optionConverter;
	}
	
	@Override
	public ProductView toView(Product model) throws ConversionException {
		ProductView view = new ProductView();
		
		view.setSerialNumber(model.getSerialNumber());
		view.setRfidNumber(model.getRfidNumber());
		view.setCustomerRefNumber(model.getCustomerRefNumber());
		view.setOwner(model.getOwner().getName());
		view.setLocation(model.getLocation());
		view.setPurchaseOrder(model.getPurchaseOrder());
		view.setComments(model.getComments());
		view.setIdentified(model.getIdentified());
		
		if (model.getProductStatus() != null) {
			view.setStatus(model.getProductStatus().getName());
		}
		
		if (model.getShopOrder() != null) {
			view.setShopOrder(model.getShopOrder().getOrder().getOrderNumber());
		}

		view.getAttributes().putAll(optionConverter.toMap(model.getInfoOptions()));
		
		return view;
	}

}
