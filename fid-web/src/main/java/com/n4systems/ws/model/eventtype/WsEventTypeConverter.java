package com.n4systems.ws.model.eventtype;

import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeConverter extends WsModelConverter<EventType, WsEventType> {
	private final WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter;
	private final WsModelConverter<EventForm, WsEventForm> formConverter;
	
	public WsEventTypeConverter() {
		this(new WsEventFormConverter(), new WsEventTypeGroupConverter());
	}
	
	protected WsEventTypeConverter(WsModelConverter<EventForm, WsEventForm> formConverter, WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter) {
		this.formConverter = formConverter;
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
		wsEventType.setInfoFieldNames(model.getInfoFieldNames());
		wsEventType.setGroup(groupConverter.fromModel(model.getGroup()));
		wsEventType.setFormVersion(model.getFormVersion());
        if (model.getEventForm() != null)
        	wsEventType.setForm(formConverter.fromModel(model.getEventForm()));
		return wsEventType;
	}

}
