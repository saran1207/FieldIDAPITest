package com.n4systems.model.builders;

import com.n4systems.model.BaseEntity;

abstract public class BaseEntityBuilder<K extends BaseEntity> extends BaseBuilder<K> {

	public BaseEntityBuilder(Long id) {
        super(id);
	}
	
	protected K assignAbstractFields(K model) {
		model.setId(getId());
		return model;
	}
	
}
