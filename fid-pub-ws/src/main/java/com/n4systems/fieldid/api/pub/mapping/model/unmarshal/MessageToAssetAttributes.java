package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.exceptions.NotImplementedException;
import com.n4systems.fieldid.api.pub.mapping.ConversionContext;
import com.n4systems.fieldid.api.pub.mapping.Converter;
import com.n4systems.fieldid.api.pub.mapping.ValueConverterWithContext;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage.AttributeMessage;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.PublicIdEncoder;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class MessageToAssetAttributes extends FieldIdPersistenceService implements ValueConverterWithContext<AttributeMessage, InfoOptionBean, AssetMessage, Asset> {

	@Override
	@Transactional(readOnly = true)
	public InfoOptionBean convert(AttributeMessage attributeMessage, ConversionContext<AssetMessage, Asset> context) {
		Set<InfoOptionBean> existingOptions = context.getTo().getInfoOptions();
		Long infoFieldID = PublicIdEncoder.decode(attributeMessage.getId());
		String value = attributeMessage.getValue();

		Optional<InfoOptionBean> infoOptionBeanOptional = existingOptions.stream().filter((o) -> o.getInfoField().getUniqueID().equals(infoFieldID)).findFirst();

		InfoFieldBean infoField;
		InfoOptionBean infoOptionBean;
		if(infoOptionBeanOptional.isPresent()) {
			infoOptionBean = infoOptionBeanOptional.get();
			infoField = infoOptionBean.getInfoField();
		} else {
			infoField = persistenceService.findNonSecure(InfoFieldBean.class, infoFieldID);
			infoOptionBean = new InfoOptionBean();
			infoOptionBean.setInfoField(infoField);
		}

		switch (infoField.getType()) {
			case TextField:
			case UnitOfMeasure:
				infoOptionBean.setName(value);
				break;
			case DateField:
				Date dateValue = Converter.convertStringToDateTime().convert(value);
				if (dateValue != null) {
					infoOptionBean.setName(Long.toString(dateValue.getTime()));
				}
				break;
			case SelectBox:
			case ComboBox:
				// currently handling Select and Combo the same
				Optional<InfoOptionBean> staticOptionOptional = infoField.getUnfilteredInfoOptions().stream().filter((o) -> o.getName().equals(value)).findFirst();
				if (staticOptionOptional.isPresent()) {
					infoOptionBean = staticOptionOptional.get();
				} else {
					infoOptionBean.setName(value);
				}
				break;
			default:
				throw new NotImplementedException(infoField.getType().toString());
		}

		return infoOptionBean;
	}
}
