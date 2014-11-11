package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.parents.AbstractEntity;
import org.springframework.transaction.annotation.Transactional;

public abstract class ModelResolver<T extends AbstractEntity> extends FieldIdPersistenceService implements ValueConverter<String, T> {

	private final Class<T> modelClass;

	protected ModelResolver(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	@Transactional(readOnly = true)
	public T convert(String publicId) {
		return (publicId != null) ? persistenceService.findById(modelClass, PublicIdEncoder.decode(publicId)) : null;
	}
}
