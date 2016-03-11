package com.n4systems.model.builders;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

import java.util.Random;

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
