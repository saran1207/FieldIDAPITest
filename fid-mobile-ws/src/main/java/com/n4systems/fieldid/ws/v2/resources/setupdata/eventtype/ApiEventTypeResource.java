package com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.EventType;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.ThingEventType;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("eventType")
public class ApiEventTypeResource extends SetupDataResourceReadOnly<ApiEventType, EventType> {

	public ApiEventTypeResource() {
		super(EventType.class, true);
	}

	@Override
	protected ApiEventType convertEntityToApiModel(EventType eventType) {
		// for now we are filtering out PlaceEventTypes.  The nulls will be removed by the convertAllEntitiesToApiModels call
		if (eventType instanceof PlaceEventType) {
			return null;
		}

		ApiEventType apiEventType = new ApiEventType();
		apiEventType.setSid(eventType.getId());
		apiEventType.setActive(eventType.isActive());
		apiEventType.setModified(eventType.getModified());
		
		// We only set the rest of the fields if the entity is active.
		if (eventType.isActive()) {
			apiEventType.setAssignedToAvailable(eventType.isAssignedToAvailable());
			apiEventType.setDescription(eventType.getDescription());
			apiEventType.setMaster(eventType instanceof ThingEventType && ((ThingEventType)eventType).isMaster());
			apiEventType.setName(eventType.getName());
			apiEventType.setAction(eventType.isActionEventType());
			apiEventType.setPrintable(eventType.isPrintable());
			apiEventType.setHasPrintOut(eventType.getGroup().hasPrintOut());
			apiEventType.setHasObservationPrintOut(eventType.getGroup().hasObservationPrintOut());
			apiEventType.getAttributes().addAll(eventType.getInfoFieldNames());
	
			if (eventType.getGroup() != null) {
				apiEventType.setGroupName(eventType.getGroup().getName());
			}
	
			if (eventType.getEventForm() != null) {
				apiEventType.setFormId(eventType.getEventForm().getId());
			}
		}

		return apiEventType;
	}

}
