package com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute;

import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.attributevalues.ApiAttributeValueConverter;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rfid.ejb.entity.InfoOptionBean;

import javax.ws.rs.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Path("/autoAttribute")
public class ApiAutoAttributeResource extends SetupDataResourceReadOnly<ApiAutoAttribute, AutoAttributeCriteria> {

	@Autowired private ApiAttributeValueConverter apiAttributeValueConverter;

	private final Function<InfoOptionBean, Long> infoFieldIdExtractor = option -> option.getInfoField().getUniqueID();
	private final Function<InfoOptionBean, Object> infoOptionValueExtractor = option -> apiAttributeValueConverter.convertInfoOption(option).getValue();

	public ApiAutoAttributeResource() {
		super(AutoAttributeCriteria.class, false);
	}

	@Override
	protected ApiAutoAttribute convertEntityToApiModel(AutoAttributeCriteria auto) {
		ApiAutoAttribute apiAuto = new ApiAutoAttribute();
		apiAuto.setSid(auto.getId());
		apiAuto.setModified(auto.getModified());
		apiAuto.setActive(true);
		apiAuto.setAssetTypeId(auto.getAssetType().getId());
		apiAuto.setDefs(auto.getDefinitions().stream().map(this::convertDefinition).collect(Collectors.toList()));
		return apiAuto;
	}

	private ApiAutoAttributeDefinition convertDefinition(AutoAttributeDefinition def) {
		ApiAutoAttributeDefinition apiDef = new ApiAutoAttributeDefinition();
		apiDef.setIn(def.getInputs().stream().collect(Collectors.toMap(infoFieldIdExtractor, InfoOptionBean::getUniqueID)));
		apiDef.setOut(def.getSanitizedOutputs().stream().collect(Collectors.toMap(infoFieldIdExtractor, infoOptionValueExtractor)));
		return apiDef;
	}
}
