package com.n4systems.api.conversion.asset;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import org.apache.log4j.Logger;
import rfid.ejb.entity.InfoFieldBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class AssetToViewConverter implements ModelToViewConverter<Asset, AssetView> {

	private Logger logger = Logger.getLogger(AssetToViewConverter.class);

	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

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
        view.setLocation(model.getAdvancedLocation().getFullName());
		view.setPurchaseOrder(model.getPurchaseOrder());
		view.setComments(model.getComments());
		view.setIdentified(model.getIdentified());

		convertOwnerFields(model.getOwner(), view);
		
		if (model.getAssetStatus() != null) {
			view.setStatus(model.getAssetStatus().getName());
		}

        if(model.getAssignedUser() != null) {
            view.setAssignedUser(model.getAssignedUser().getFullName());
        }

		// integration customers cannot use the 'Order Number' field as it would be impossible
		// to resolve the order number to a line item on import
		if (!primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
			if (model.getShopOrder() != null) {
				view.setShopOrder(model.getShopOrder().getOrder().getOrderNumber());
			}
		}

		Collection<InfoFieldBean> requiredInfoOptions = model.getType().getInfoFields();
		Map<String, String> providedInfoOptionsMap = optionConverter.toMap(model.getInfoOptions());
		Map<String, String> finalOptionsMap = new LinkedHashMap<String, String>(requiredInfoOptions.size());

		for (InfoFieldBean field: requiredInfoOptions) {
			String optionValue = providedInfoOptionsMap.get(field.getName());
			if (optionValue != null) {
				finalOptionsMap.put(field.getName(), convertToDisplayFormat(field, optionValue));
			} else {
				finalOptionsMap.put(field.getName(), "");
			}
		}
		view.getAttributes().putAll(finalOptionsMap);

		view.setGlobalId(model.getMobileGUID());
		
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

	private String convertToDisplayFormat(InfoFieldBean infoFieldBean, String value) {
		if (InfoFieldBean.DATEFIELD_FIELD_TYPE.equals(infoFieldBean.getFieldType())) {
			try {
				return df.get().format(new Date(Long.parseLong(value.toString())));
			} catch (NumberFormatException e) {
				logger.warn("can't parse date InfoOption value of '" + value.toString() + "'");
			}
		}
		return value;
	}

}
