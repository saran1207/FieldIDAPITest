package com.n4systems.ws.model.eventtype;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeConverter extends WsModelConverter<EventType, WsEventType> {
	private final WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter;
	
	public WsEventTypeConverter() {
		this(new WsEventTypeGroupConverter());
	}
	
	protected WsEventTypeConverter(WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter) {
		this.groupConverter = groupConverter;
	}
	
	@Override
	public WsEventType fromModel(EventType model) {
		WsEventType wsEventType = new WsEventType();
		wsEventType.setId(model.getId());
		wsEventType.setName(model.getName());
		wsEventType.setDescription(model.getDescription());
		wsEventType.setActive(model.isActive());
		wsEventType.setPrintable(model.isPrintable());
		wsEventType.setMaster(model.isMaster());
		wsEventType.setAssignedToAvailable(model.isAssignedToAvailable());
		wsEventType.setInfoFieldNames(model.getInfoFieldNames());
		wsEventType.setGroup(groupConverter.fromModel(model.getGroup()));
        if (model.getEventForm() != null)
        	wsEventType.setFormId(model.getEventForm().getId());
		return wsEventType;
	}

}
