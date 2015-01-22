package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.ApiModelWithName;

public class ApiModelWithNameToMessage<T> extends TypeMapper<ApiModelWithName, T> {

	public ApiModelWithNameToMessage(SetterReference<T, String> setId, SetterReference<T, String> setName) {
		super(TypeMapperBuilder.<ApiModelWithName, T>newBuilder()
				.add(ApiModelWithName::getPublicId, setId, false)
				.add(ApiModelWithName::getName, setName, false)
				.build());
	}
}
