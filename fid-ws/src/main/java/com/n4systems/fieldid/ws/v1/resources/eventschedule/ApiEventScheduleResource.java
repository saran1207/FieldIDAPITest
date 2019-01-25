package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetLink;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.synchronization.ApiSynchronizationResource;
import com.n4systems.model.*;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Path("eventSchedule")
public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, ThingEvent> {
    private static Logger logger = Logger.getLogger(ApiEventScheduleResource.class);
    
    @Autowired private EventScheduleService eventScheduleService;
    @Autowired private PersistenceService persistenceService;
    @Autowired private AssetService assetService;
    @Autowired private ApiTriggerEventResource apiTriggerEventResource;    
    
    public List<ApiEventSchedule>  findAllSchedules(Long assetId, SyncDuration syncDuration) {
        List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();

        QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class)
        .addOrder("dueDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
        .addWhere(WhereClauseFactory.create("asset.id", assetId));
        
        if(syncDuration != SyncDuration.ALL) {
            // If we have a specific syncduration, we should filter schedules that is bounded by start and end dates.
            // This is currently only used for taking assets offline.
            Date startDate = new LocalDate().toDate();
            Date endDate = ApiSynchronizationResource.getSyncEndDate(syncDuration, startDate);
            query.addWhere(WhereClauseFactory.create(Comparator.GE, "dueDate", startDate));
            query.addWhere(WhereClauseFactory.create(Comparator.LE, "dueDate", endDate));
        }
        
        List<ThingEvent> schedules = persistenceService.findAll(query);
        
        for (ThingEvent schedule: schedules) {
            apiEventSchedules.add(convertEntityToApiModel(schedule));
        }

        return apiEventSchedules;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional
    public void saveEventSchedule(ApiEventSchedule apiEventSchedule) {
        setNewRelicWithAppInfoParameters();
        ThingEvent event = eventScheduleService.findByMobileId(apiEventSchedule.getSid());

        if (event == null) {
            event = converApiEventSchedule(apiEventSchedule);
            persistenceService.save(event);
            logger.info("Saved New Scheduled Event("  + event.getMobileGUID() + ") for " + 
                    event.getEventType().getName() + " on Asset " + event.getAsset().getMobileGUID());
        } else if (event.getWorkflowState() == WorkflowState.OPEN) {
            event.setDueDate(apiEventSchedule.getNextDate());
            event.setType(persistenceService.find(ThingEventType.class, apiEventSchedule.getEventTypeId()));
            event.setAssignee(getAssigneeUser(apiEventSchedule));
            if(event.getAssignee() == null)
                event.setAssignedGroup(getAssigneeUserGroup(apiEventSchedule));
            
            persistenceService.update(event);
            logger.warn("(Legacy Client Detected) Updated Scheduled Event for " + event.getEventType().getName() + " on Asset " + event.getMobileGUID());
        } else {
            logger.warn("(Legacy Client Detected) Failed Updating Completed Scheduled Event" + event.getId());
        }
    }

    @PUT
    @Path("multi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional
    public void saveMultipleEventSchedules(ApiMultiEventSchedule apiMultiEventSchedule) {
        setNewRelicWithAppInfoParameters();
        ApiEventSchedule eventScheduleTemplate = apiMultiEventSchedule.getEventScheduleTemplate();
        boolean copyOwner = eventScheduleTemplate.getOwnerId() == null; //If client had copyOwner, we will have ownerId = null.
        for (ApiAssetLink assetLink: apiMultiEventSchedule.getEventSchedules()) {
            eventScheduleTemplate.setSid(assetLink.getSid());
            eventScheduleTemplate.setAssetId(assetLink.getAssetId());
            
            if(copyOwner) {
                Asset asset = assetService.findByMobileId(assetLink.getAssetId(), true);
                eventScheduleTemplate.setOwnerId(asset.getOwner() != null ? asset.getOwner().getId() : null);
            }

            logger.info("Creating Event Schedule " + eventScheduleTemplate.getSid());
            saveEventSchedule(eventScheduleTemplate);
        }

        logger.info("Saved " + apiMultiEventSchedule.getEventSchedules().size() + " Schedules");
    }
    
    @DELETE
    @Path("{eventScheduleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional
    public void deleteEventSchedule(@PathParam("eventScheduleId") String eventScheduleId) {
        logger.info("Attempting to archive Scheduled Event with mobileId " + eventScheduleId);
        setNewRelicWithAppInfoParameters();
        Event event = eventScheduleService.findByMobileId(eventScheduleId, true);
        if(event != null) {
            if(event.isActive()) {
                event.archiveEntity();
                persistenceService.update(event);
                logger.info("Archived Scheduled Event for " + event.getEventType().getName() + " on Event " + event.getMobileGUID());
            } else {
                logger.warn("Failed Deleting Event Schedule. Inative Event " + eventScheduleId);
            }
        } else {
            logger.warn("Failed Deleting Event Schedule. Unable to find Event " + eventScheduleId);
        }
    }

    @Override
    protected ApiEventSchedule convertEntityToApiModel(ThingEvent event) {
        ApiEventSchedule apiSchedule = new ApiEventSchedule();
        // For backward compatibility, we must still use the GUID of the Schedule, since all
        // existing schedules out there in mobile land will refer to schedule GUIDs.
        apiSchedule.setSid(event.getMobileGUID());
        apiSchedule.setActive(true);
        apiSchedule.setModified(event.getModified());
        apiSchedule.setOwnerId(event.getOwner().getId());
        apiSchedule.setAssetId(event.getAsset().getMobileGUID());
        apiSchedule.setAssetIdentifier(event.getAsset().getIdentifier());
        apiSchedule.setEventTypeId(event.getEventType().getId());
        apiSchedule.setEventTypeName(event.getEventType().getName());
        apiSchedule.setOwner(event.getOwner().getDisplayName());
        apiSchedule.setNextDate(event.getDueDate());
        if (event.getAssignee() != null) {
            apiSchedule.setAssigneeUserId(event.getAssignee().getId());
        }
        if(event.getAssignedGroup() != null) {
            apiSchedule.setAssigneeUserGroupId(event.getAssignedGroup().getId());
        }
        
        
        if (event.isAction()) {
            apiSchedule.setAction(true);
            apiSchedule.setPriorityId(event.getPriority().getId());
            apiSchedule.setNotes(event.getNotes());
            apiSchedule.setTriggerEventInfo(apiTriggerEventResource.convertEntityToApiModel(event));
        }
        
        return apiSchedule;
    }
    
    public ThingEvent converApiEventSchedule(ApiEventSchedule apiEventSchedule) {
        BaseOrg owner = persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEventSchedule.getOwnerId());

        Asset asset = assetService.findByMobileId(apiEventSchedule.getAssetId(), true);

        ThingEvent event = new ThingEvent();

        event.setMobileGUID(apiEventSchedule.getSid());
        event.setDueDate(apiEventSchedule.getNextDate());
        event.setTenant(owner.getTenant());
        event.setAsset(asset);
        event.setType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
        event.setOwner(asset.getOwner());
        event.setAssignee(getAssigneeUser(apiEventSchedule));

        if (event.getAssignee() == null) {
            event.setAssignedGroup(getAssigneeUserGroup(apiEventSchedule));
        }
        
        if (event.getEventType() instanceof ActionEventType) {
            event.setPriority(persistenceService.find(PriorityCode.class, apiEventSchedule.getPriorityId()));
            event.setNotes(apiEventSchedule.getNotes());
        }
        
        if (asset.isArchived()) {
            event.archiveEntity();
        }

        return event;
    }
    
    private User getAssigneeUser(ApiEventSchedule apiEventSchedule) {
        if(apiEventSchedule.getAssigneeUserId() != null && apiEventSchedule.getAssigneeUserId() > 0) {
            User assigneeUser = persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEventSchedule.getAssigneeUserId());
            return assigneeUser;
        }
        
        return null;
    }
    
    private UserGroup getAssigneeUserGroup(ApiEventSchedule apiEventSchedule) {
        if(apiEventSchedule.getAssigneeUserGroupId() != null && apiEventSchedule.getAssigneeUserGroupId() > 0) {
            UserGroup assigneeUserGroup = persistenceService.findUsingTenantOnlySecurityWithArchived(UserGroup.class, apiEventSchedule.getAssigneeUserGroupId());
            return assigneeUserGroup;
        }
        
        return null;
    }
    
    @GET
    @Path("assignedList")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ListResponse<ApiEventSchedule> findAssignedOpenEvents(
            @QueryParam("startDate") Date startDate, 
            @QueryParam("endDate") Date endDate,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        setNewRelicWithAppInfoParameters();
        User user = getCurrentUser();

        QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class)
        .addOrder("dueDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
        .addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "dueDate", startDate))
        .addWhere(WhereClauseFactory.create(Comparator.LT, "endDate", "dueDate", endDate));    //excludes end date.
        
        if (user.getGroups().isEmpty()) {
            query.addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", user.getId()));
        } else {
            // WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )                
            WhereParameterGroup group = new WhereParameterGroup();
            group.setChainOperator(WhereClause.ChainOp.AND);
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
            query.addWhere(group);                
        }
        
        List<ThingEvent> events = persistenceService.findAll(query, page, pageSize);
        Long total = persistenceService.count(query);
        List<ApiEventSchedule> apiSchedules = convertAllEntitiesToApiModels(events);
        ListResponse<ApiEventSchedule> response = new ListResponse<ApiEventSchedule>(apiSchedules, page, pageSize, total);
        
        logger.info("findAssignedOpenEvents: >= startDate: " + startDate + " < endDate: " + endDate);
        
        return response;
    }
    
    @GET
    @Path("assignedListCounts")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public List<Long> findAssignedOpenEventCounts(
            @QueryParam("year") int year,
            @QueryParam("month") int month) {
        setNewRelicWithAppInfoParameters();
        List<Long> counts = new ArrayList<Long>();        
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 1; i <= days; i++) {
            Date startDate = new DateTime(year, month + 1, i, 0, 0).toDate();
            Date endDate = new DateTime(year, month + 1, i, 0, 0).plusDays(1).toDate();            
            User user = getCurrentUser();
            
            QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
            .addOrder("dueDate")
            .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
            .addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "dueDate", startDate))
            .addWhere(WhereClauseFactory.create(Comparator.LT, "endDate", "dueDate", endDate));
            
            if (user.getGroups().isEmpty()) {
                query.addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", user.getId()));
            } else {
                // WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )                
                WhereParameterGroup group = new WhereParameterGroup();
                group.setChainOperator(WhereClause.ChainOp.AND);
                group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
                group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
                query.addWhere(group);                
            }
            
            Long count = persistenceService.count(query);
            counts.add(count);
        }
        return counts;
    }
}
