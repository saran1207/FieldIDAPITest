package com.n4systems.ws.model.eventtype;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeConverter extends WsModelConverter<EventType, WsEventType> {
	private final WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter;
	private final WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter;
	
	public WsEventTypeConverter() {
		this(new WsCriteriaSectionConverter(), new WsEventTypeGroupConverter());
	}
	
	protected WsEventTypeConverter(WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter, WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter) {
		this.sectionConverter = sectionConverter;
		this.groupConverter = groupConverter;
	}
	
	@Override
	public WsEventType fromModel(EventType model) {
		WsEventType wsEventType = new WsEventType();
		wsEventType.setId(model.getId());
		wsEventType.setName(model.getName());
		wsEventType.setDescription(model.getDescription());
		wsEventType.setPrintable(model.isPrintable());
		wsEventType.setMaster(model.isMaster());
		wsEventType.setAssignedToAvailable(model.isAssignedToAvailable());
		wsEventType.setGroup(groupConverter.fromModel(model.getGroup()));
		wsEventType.setSections(sectionConverter.fromModels(model.getSections()));
		return wsEventType;
	}

}
