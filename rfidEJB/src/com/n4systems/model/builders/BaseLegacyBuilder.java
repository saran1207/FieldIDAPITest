package com.n4systems.model.builders;

import java.util.Random;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

public abstract class BaseLegacyBuilder<K extends LegacyBaseEntity>{

	protected Long uniqueId;
		
	public BaseLegacyBuilder() {
		uniqueId = generateNewId();
	}
	
	public abstract K build();
	
	public Long generateNewId() {
		return new Random().nextLong();
	}	
		
}
