package com.n4systems.fieldid.ws.v2.resources.setupdata;

import com.n4systems.fieldid.ws.v2.resources.ApiKeyLong;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;
import com.n4systems.model.parents.AbstractEntity;

public abstract class SetupDataResourceReadOnly<A extends ApiReadonlyModel, E extends AbstractEntity> extends SetupDataResource<A, E, ApiKeyLong> {

	protected SetupDataResourceReadOnly(Class<E> entityClass, boolean allowArchived) {
		super("id", entityClass, allowArchived);
	}

}
