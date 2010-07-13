package com.n4systems.api.conversion.product;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Product;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;


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
		
		PrimaryOrg primaryOrg = model.getOwner().getPrimaryOrg();
		
		view.setSerialNumber(model.getSerialNumber());
		view.setRfidNumber(model.getRfidNumber());
		view.setCustomerRefNumber(model.getCustomerRefNumber());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPurchaseOrder(model.getPurchaseOrder());
		view.setComments(model.getComments());
		view.setIdentified(model.getIdentified());
		
		convertOwnerFields(model.getOwner(), view);
		
		if (model.getProductStatus() != null) {
			view.setStatus(model.getProductStatus().getName());
		}
		
		// integration customers cannot use the 'Order Number' field as it would be impossible
		// to resolve the order number to a line item on import
		if (!primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
			if (model.getShopOrder() != null) {
				view.setShopOrder(model.getShopOrder().getOrder().getOrderNumber());
			}
		}

		view.getAttributes().putAll(optionConverter.toMap(model.getInfoOptions()));
		
		return view;
	}

	private void convertOwnerFields(BaseOrg owner, ProductView view) {
		view.setOrganization(owner.getInternalOrg().getName());
		
		if (owner.isExternal()) {
			view.setCustomer(owner.getCustomerOrg().getName());
		
			if (owner.isDivision()) {
				view.setDivision(owner.getDivisionOrg().getName());
			}
		}
	}
	
}
