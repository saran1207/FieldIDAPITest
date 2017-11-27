package com.n4systems.api.conversion.asset;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import org.apache.log4j.Logger;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

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

		Collection<InfoFieldBean> typeInfoOptions = model.getType().getInfoFields();
		Set<InfoOptionBean> assetInfoOptions = model.getInfoOptions();
		try {
			Map<String, String> infoOptionsOriginalMap = optionConverter.toMap(assetInfoOptions);
			List<InfoOptionBean> convertedOptions = optionConverter.convertAssetAttributes(infoOptionsOriginalMap, model.getType());
			convertedOptions.forEach((infoOption) -> convertToDisplayFormat(infoOption));
			view.getAttributes().putAll(optionConverter.toMap(convertedOptions));
		}
		catch(InfoOptionConversionException ex) {
			logger.error("Error in converting infoOption", ex);
			throw new ConversionException(ex);
		}

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

	private void convertToDisplayFormat(InfoOptionBean infoOptionBean) {
		String value = infoOptionBean.getName();
		if (value != null) {
			if (InfoFieldBean.DATEFIELD_FIELD_TYPE.equals(infoOptionBean.getInfoField().getFieldType())) {
				try {
					infoOptionBean.setName(df.get().format(new Date(Long.parseLong(value.toString()))));
				} catch (NumberFormatException e) {
					logger.warn("can't parse date InfoOption value of '" + value.toString() + "'");
				}
			}
		}
	}

}
