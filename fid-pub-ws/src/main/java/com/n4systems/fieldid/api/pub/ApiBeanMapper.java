package com.n4systems.fieldid.api.pub;

import com.google.protobuf.GeneratedMessage;
import com.n4systems.model.parents.AbstractEntity;

public interface ApiBeanMapper<M extends AbstractEntity, A extends GeneratedMessage> {
	public A toMessage(M model);
	public M toModel(A message);
}
