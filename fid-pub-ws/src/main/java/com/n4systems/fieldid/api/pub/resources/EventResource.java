package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.ConversionContext;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.ApiModelWithNameToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.AssetToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.PriorityCodeToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.BaseOrgResolver;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.PriorityCodeResolver;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.UserResolver;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.EventMessage;
import com.n4systems.fieldid.api.pub.model.Messages.EventMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;

/**
 * Created by rrana on 2018-08-23.
 */

@Path("inspections")
@Component
public class EventResource extends CrudResource<ThingEvent, EventMessage, Builder> {

    private Logger logger = Logger.getLogger(EventResource.class);
    @Autowired private EventService eventService;
    @Autowired private BaseOrgResolver baseOrgResolver;
    @Autowired private UserResolver userResolver;
    @Autowired private PriorityCodeResolver priorityCodeResolver;

    public EventResource() {
        super(Messages.events);
    }

    @Override
    protected CrudService<ThingEvent> crudService() { return eventService; }

    @Override
    protected ThingEvent createModel(EventMessage message) {
        return new ThingEvent();
    }

    @Override
    protected Messages.EventMessage.Builder createMessageBuilder(ThingEvent model) {
        return Messages.EventMessage.newBuilder();
    }

    @Override
    protected Mapper<ThingEvent, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<ThingEvent, Messages.EventMessage.Builder> mapperBuilder) {
        return mapperBuilder
                .add(Event::getPublicId, Builder::setId)
                .addDateToString(ThingEvent::getCreated, Builder::setCreatedDate)
                .addDateToString(ThingEvent::getModified, Builder::setModifiedDate)
                .addDateToString(ThingEvent::getCompletedDate, Builder::setCompletedDate)
                .addDateToString(ThingEvent::getDueDate, Builder::setDueDate)
                .addModelToMessage(ThingEvent::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
                .addModelToMessage(ThingEvent::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
                .addModelToMessage(ThingEvent::getAssignee, new UserToMessage<>(Builder::setAssignedToUserId, Builder::setAssignedToUserName))
                .addModelToMessage(ThingEvent::getPerformedBy, new UserToMessage<>(Builder::setCompletedByUserId, Builder::setCompletedByUserName))
                .addModelToMessage(ThingEvent::getOwner, new ApiModelWithNameToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
                .addModelToMessage(ThingEvent::getEventType, new ApiModelWithNameToMessage<>(Builder::setEventTypeId, Builder::setEventTypeName))
                .addModelToMessage(ThingEvent::getAsset, new AssetToMessage<>(Builder::setAssetId, Builder::setIdentifier, Builder::setRfidNumber, Builder::setCustomerRefNumber))
                .add(ThingEvent::getComments, Builder::setComments)
                .add(ThingEvent::getNotes, Builder::setNotes)
                .addToString(ThingEvent::getScore, Builder::setScore)
                .add(ThingEvent::getEventResult, Builder::setEventResult, this::convertResultToMessage)
                .add(ThingEvent::getWorkflowState, Builder::setWorkflowState, this::convertWorkflowStateToMessage)
                .addModelToMessage(ThingEvent::getPriority, new PriorityCodeToMessage<>(Builder::setPriorityCodeId, Builder::setPriorityCodeName))
                .build();
    }

    @Override
    protected Mapper<EventMessage, ThingEvent> createMessageToModelMapper(TypeMapperBuilder<EventMessage, ThingEvent> mapperBuilder) {
        return mapperBuilder
                .add(EventMessage::getComments, ThingEvent::setComments)
                .add(EventMessage::getNotes, ThingEvent::setNotes)
                .addToDouble(EventMessage::getScore, ThingEvent::setScore)
                .add(EventMessage::getEventResult, ThingEvent::setEventResult, this::convertResultToModel)
                .add(EventMessage::getWorkflowState, ThingEvent::setWorkflowState, this::convertWorkflowStateToModel)
                .add(EventMessage::getOwnerId, ThingEvent::setOwner, baseOrgResolver)
                .add(EventMessage::getAssignedToUserId, ThingEvent::setAssignee, userResolver)
                .add(EventMessage::getCompletedByUserId, ThingEvent::setPerformedBy, userResolver)
                .add(EventMessage::getPriorityCodeId, ThingEvent::setPriority, priorityCodeResolver)
                .build();
    }

    private EventResult convertResultToModel(Messages.EventMessage.EventResult result, ConversionContext<EventMessage, ThingEvent> context) {
        switch (result) {
            case PASS:
                return EventResult.PASS;
            case FAIL:
                return EventResult.FAIL;
            case NA:
                return EventResult.NA;
            case VOID:
                return EventResult.VOID;
            default:
                throw new InternalServerErrorException("Unhandled EventMessage.EventResult: " + result.name());
        }
    }

    private WorkflowState convertWorkflowStateToModel(Messages.EventMessage.WorkflowState state, ConversionContext<EventMessage, ThingEvent> context) {
        switch (state) {
            case OPEN:
                return WorkflowState.OPEN;
            case COMPLETED:
                return WorkflowState.COMPLETED;
            case CLOSED:
                return WorkflowState.CLOSED;
            case NONE:
                return WorkflowState.NONE;
            default:
                throw new InternalServerErrorException("Unhandled EventMessage.WorkflowState: " + state.name());
        }
    }

    private EventMessage.EventResult convertResultToMessage(EventResult result) {
        switch (result) {
            case PASS:
                return EventMessage.EventResult.PASS;
            case FAIL:
                return EventMessage.EventResult.FAIL;
            case NA:
                return EventMessage.EventResult.NA;
            case VOID:
                return EventMessage.EventResult.VOID;
            default:
                throw new InternalServerErrorException("Unhandled EventResult: " + result.name());
        }
    }

    private EventMessage.WorkflowState convertWorkflowStateToMessage(WorkflowState state) {
        switch (state) {
            case OPEN:
                return EventMessage.WorkflowState.OPEN;
            case COMPLETED:
                return EventMessage.WorkflowState.COMPLETED;
            case CLOSED:
                return EventMessage.WorkflowState.CLOSED;
            case NONE:
                return EventMessage.WorkflowState.NONE;
            default:
                throw new InternalServerErrorException("Unhandled WorkflowState: " + state.name());
        }
    }

}
