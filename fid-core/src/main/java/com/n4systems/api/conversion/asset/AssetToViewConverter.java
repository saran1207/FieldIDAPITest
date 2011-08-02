package com.n4systems.api.conversion.asset;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;


public class AssetToViewConverter implements ModelToViewConverter<Asset, AssetView> {
	private final InfoOptionMapConverter optionConverter;
	
	public AssetToViewConverter() {
		this(new InfoOptionMapConverter());	
	}
	
	public AssetToViewConverter(InfoOptionMapConverter optionConverter) {
		this.optionConverter = optionConverter;
	}
	
	@Override
	public AssetView toView(Asset model) throws ConversionException {
		AssetView view = new AssetView();
		
		PrimaryOrg primaryOrg = model.getOwner().getPrimaryOrg();
		
		view.setIdentifier(model.getIdentifier());
		view.setRfidNumber(model.getRfidNumber());
		view.setCustomerRefNumber(model.getCustomerRefNumber());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPurchaseOrder(model.getPurchaseOrder());
		view.setComments(model.getComments());
		view.setIdentified(model.getIdentified());
		
		convertOwnerFields(model.getOwner(), view);
		
		if (model.getAssetStatus() != null) {
			view.setStatus(model.getAssetStatus().getName());
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

	private void convertOwnerFields(BaseOrg owner, AssetView view) {
		view.setOrganization(owner.getInternalOrg().getName());
		
		if (owner.isExternal()) {
			view.setCustomer(owner.getCustomerOrg().getName());
		
			if (owner.isDivision()) {
				view.setDivision(owner.getDivisionOrg().getName());
			}
		}
	}
	
}
