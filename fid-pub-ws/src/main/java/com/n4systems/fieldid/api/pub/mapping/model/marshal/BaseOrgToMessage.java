package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.orgs.BaseOrg;

public class BaseOrgToMessage<T> extends TypeMapper<BaseOrg, T> {

	public BaseOrgToMessage(SetterReference<T, String> idSetter, SetterReference<T, String> nameSetter) {
		super(TypeMapperBuilder.<BaseOrg, T>newBuilder()
				.add(BaseOrg::getPublicId, idSetter, false)
				.add(BaseOrg::getName, nameSetter, false)
				.build());
	}
}