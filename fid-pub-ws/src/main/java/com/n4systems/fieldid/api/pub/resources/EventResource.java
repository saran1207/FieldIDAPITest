package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.ConversionContext;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.ApiModelWithNameToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.AssetToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.EventMessage;
import com.n4systems.fieldid.api.pub.model.Messages.EventMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.*;
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

    @Autowired private EventService eventService;
    @Autowired private UserService userService;
    @Autowired private AssetService assetService;

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
                .add(ThingEvent::getPublicId, Builder::setId)
                .addDateToString(ThingEvent::getCreated, Builder::setCreatedDate)
                .addDateToString(ThingEvent::getModified, Builder::setModifiedDate)
                .addDateToString(ThingEvent::getCompletedDate, Builder::setCompletedDate)
                .addModelToMessage(ThingEvent::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
                .addModelToMessage(ThingEvent::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
                .addModelToMessage(ThingEvent::getAssignee, new UserToMessage<>(Builder::setAssignedToUserId, Builder::setAssignedToUserName))
                .addModelToMessage(ThingEvent::getPerformedBy, new UserToMessage<>(Builder::setCompletedByUserId, Builder::setCompletedByUserName))
                .addModelToMessage(ThingEvent::getOwner, new ApiModelWithNameToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
                .addModelToMessage(ThingEvent::getEventType, new ApiModelWithNameToMessage<>(Builder::setEventTypeId, Builder::setEventTypeName))
                .addModelToMessage(ThingEvent::getAsset, new AssetToMessage<>(Builder::setId, Builder::setIdentifier, Builder::setRfidNumber, Builder::setCustomerRefNumber))
                .add(ThingEvent::getComments, Builder::setComments)
                .addToString(ThingEvent::getScore, Builder::setScore)
                .add(ThingEvent::getEventResult, Builder::setEventResult, this::convertResultToMessage)
                .add(ThingEvent::getWorkflowState, Builder::setWorkflowState, this::convertWorkflowStateToMessage)
                .build();
    }

    @Override
    protected Mapper<EventMessage, ThingEvent> createMessageToModelMapper(TypeMapperBuilder<EventMessage, ThingEvent> mapperBuilder) {
        return mapperBuilder
                .add(EventMessage::getComments, ThingEvent::setComments)
                .addToDouble(EventMessage::getScore, ThingEvent::setScore)
                .add(EventMessage::getEventResult, ThingEvent::setEventResult, this::convertResultToModel)
                .add(EventMessage::getWorkflowState, ThingEvent::setWorkflowState, this::convertWorkflowStateToModel)
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
