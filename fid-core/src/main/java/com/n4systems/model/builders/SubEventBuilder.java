package com.n4systems.model.builders;

import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;

import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.SubEvent;

public class SubEventBuilder extends BaseBuilder<SubEvent> {

	private final String name;
	private final EventType eventType;
	private final Asset asset;
	
	public static SubEventBuilder aSubEvent(String name) {
		return new SubEventBuilder(name, anEventType().build(), anAsset().build());
	}

	public SubEventBuilder(String name, EventType eventType, Asset asset) {
		this.name = name;
		this.eventType = eventType;
		this.asset = asset;
	}
	
	public SubEventBuilder withType(EventType eventType) {
		return new SubEventBuilder(name, eventType, asset);
	}
	
	public SubEventBuilder withAsset(Asset asset) {
		return new SubEventBuilder(name, eventType, asset);
	}
	
	@Override
	public SubEvent createObject() {
		SubEvent subEvent = new SubEvent();
		subEvent.setId(getId());
		subEvent.setName(name);
		subEvent.setType(eventType);
		subEvent.setAsset(asset);
		return subEvent;
	}

}
