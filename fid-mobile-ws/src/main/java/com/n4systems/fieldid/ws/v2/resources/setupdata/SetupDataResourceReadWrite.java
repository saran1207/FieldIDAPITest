package com.n4systems.fieldid.ws.v2.resources.setupdata;

import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;
import com.n4systems.model.parents.AbstractEntity;
import org.springframework.stereotype.Component;

@Component
public abstract class SetupDataResourceReadWrite<A extends ApiReadWriteModel, E extends AbstractEntity> extends SetupDataResource<A, E, ApiKeyString> {

	protected SetupDataResourceReadWrite(String idField, Class<E> entityClass, boolean allowArchived) {
		super(idField, entityClass, allowArchived);
	}

}
