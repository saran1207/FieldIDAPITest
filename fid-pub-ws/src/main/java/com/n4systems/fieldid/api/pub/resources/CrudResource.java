package com.n4systems.fieldid.api.pub.resources;


import com.google.protobuf.Extension;
import com.google.protobuf.GeneratedMessage;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.parents.AbstractEntity;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CrudResource<M extends AbstractEntity, A extends GeneratedMessage, B extends GeneratedMessage.Builder> extends FieldIdPersistenceService {

    private Logger logger = Logger.getLogger(CrudResource.class);
    private Mapper<M, B> modelToMessageBuilderMapper;
    private Mapper<A, M> messageToModelMapper;

    private final Extension<Messages.ListResponseMessage, List<A>> listResponseType;

    protected CrudResource(Extension<Messages.ListResponseMessage, List<A>> listResponseType) {
        this.listResponseType = listResponseType;
    }

    protected abstract CrudService<M> crudService();
    protected abstract M createModel(A message);
    protected abstract B createMessageBuilder(M model);
    protected abstract Mapper<M, B> createModelToMessageBuilderMapper(TypeMapperBuilder<M, B> mapperBuilder);
    protected abstract Mapper<A, M> createMessageToModelMapper(TypeMapperBuilder<A, M> mapperBuilder);

    public void init() {
        this.modelToMessageBuilderMapper = createModelToMessageBuilderMapper(TypeMapperBuilder.<M, B>newBuilder());
        this.messageToModelMapper = createMessageToModelMapper(TypeMapperBuilder.<A, M>newBuilder());
    }

    @Transactional(readOnly = true)
    public A toMessage(M model) {
        B messageBuilder = createMessageBuilder(model);
        modelToMessageBuilderMapper.map(model, messageBuilder);
        return (A) messageBuilder.build();
    }

    @Transactional
    public M toModel(A message) {
        return merge(message, createModel(message));
    }

    @Transactional
    public M merge(A message, M model) {
        messageToModelMapper.map(message, model);
        if (model instanceof HasTenant) {
            ((HasTenant)model).setTenant(getCurrentTenant());
        }
        return model;
    }

	@GET
	@Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Trace (dispatcher=true)
	@Transactional(readOnly = true)
	public Messages.ListResponseMessage findAll(
			@QueryParam("page") int page,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("delta") String date,
			@QueryParam("name") String name,
			@QueryParam("identifier") String identifier,
			@QueryParam("rfidNumber") String rfidNumber,
			@QueryParam("customerRefNumber") String customerRefNumber,
			@QueryParam("assetTypeName") String assetTypeName,
			@QueryParam("ownerName") String ownerName,
			@QueryParam("identifiedByUserLastName") String identifiedByUserLastName,
			@QueryParam("identifiedByUserFirstName") String identifiedByUserFirstName,
			@QueryParam("freeFormLocation") String freeFormLocation,
			@QueryParam("code") String code) {
		setEnhancedLoggingCustomParameters();
		List<M> allItems;
		List<A> items;
		Date delta = null;
		String logInfo = getLogInfo();
		String apiCall = listResponseType.getDescriptor().getName();
		String logMessage = logInfo + apiCall + " FIND All";
		logger.info(logMessage);

		if(date != null) {
			delta = convertDate(date);
			if (delta == null) {
				logger.error("Invalid delta '" + date + "'");
				throw new RuntimeException("Invalid delta '" + date + "', should be in yyyy-MM-dd HH:mm:ss format");
			}
		}

		Map<String, Object> optionalParameters = new HashMap<String, Object>();
		if (delta != null)
			optionalParameters.put("delta", delta);
		if (name != null)
			optionalParameters.put("name", name);
		if (code != null)
			optionalParameters.put("code", code);
		if (identifier != null)
			optionalParameters.put("identifier", identifier);
		if (rfidNumber != null)
			optionalParameters.put("rfidNumber", rfidNumber);
		if (customerRefNumber != null)
			optionalParameters.put("customerRefNumber", customerRefNumber);
		if (assetTypeName != null)
			optionalParameters.put("assetTypeName", assetTypeName);
		if (ownerName != null)
			optionalParameters.put("ownerName", ownerName);
		if (identifiedByUserLastName != null)
			optionalParameters.put("identifiedByUserLastName", identifiedByUserLastName);
		if (identifiedByUserFirstName != null)
			optionalParameters.put("identifiedByUserFirstName", identifiedByUserFirstName);
		if (freeFormLocation != null)
			optionalParameters.put("freeFormLocation", freeFormLocation);

		allItems = crudService().findAll(page, pageSize, optionalParameters);

		items = allItems
				.stream()
				.map(m -> toMessage(m))
				.collect(Collectors.toList());

		return Messages.ListResponseMessage.newBuilder()
				.setPageSize(pageSize)
				.setPage(page)
				.setTotal(crudService().count(optionalParameters))
				.setExtension(listResponseType, items)
				.build();
	}

    protected <M> M testNotFound(M model) {
        if (model == null) {
            throw new NotFoundException();
        }
        return model;
    }

    @GET
    @Path("{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional(readOnly = true)
    public A find(@PathParam("id") String id) {
        setEnhancedLoggingCustomParameters();
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " FIND with id " + id;
        logger.info(logMessage);

        return toMessage(testNotFound(crudService().findByPublicId(id)));
    }

    @POST
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public A save(A message) {
        setEnhancedLoggingCustomParameters();
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " CREATE with json: " + message.toString();
        logger.info(logMessage);

        return toMessage(crudService().save(toModel(message)));
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public A update(@PathParam("id") String id, A message) {
        setEnhancedLoggingCustomParameters();
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " UPDATE with id: " + id + " and json: " + message.toString();
        logger.info(logMessage);

        return toMessage(crudService().update(merge(message, testNotFound(crudService().findByPublicId(id)))));
    }

    @DELETE
    @Path("{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public A delete(@PathParam("id") String id) {
        setEnhancedLoggingCustomParameters();
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " DELETE for id: " + id;
        logger.info(logMessage);

        try {
            return toMessage(testNotFound(crudService().deleteByPublicId(id)));
        }
        catch(UnsupportedOperationException ex) {
            throw new NotAllowedException("DELETE not allowed for this entity type");
        }
    }

    @GET
    @Path("asset/{assetId}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public Messages.ListResponseMessage findByAssetId(@PathParam("assetId") String id, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize, @QueryParam("openInspections") boolean openInspections) {
        setEnhancedLoggingCustomParameters();
        List<M> allItems;
        List<A> items;
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " FIND INSPECTIONS with id " + id;
        logger.info(logMessage);

        try {
            allItems = crudService().findByAssetId(id, page, pageSize, openInspections);

            items = allItems
                    .stream()
                    .map(m -> toMessage(m))
                    .collect(Collectors.toList());

            return Messages.ListResponseMessage.newBuilder()
                    .setPageSize(pageSize)
                    .setPage(page)
                    .setTotal(allItems.size())
                    .setExtension(listResponseType, items)
                    .build();
        }
        catch(UnsupportedOperationException ex) {
            throw new NotAllowedException("DELETE not allowed for this entity type");
        }

    }

	@GET
	@Path("actionItems")
	@Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Trace (dispatcher=true)
	@Transactional
	public Messages.ListResponseMessage findAllActionItem(
			@QueryParam("page") int page,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("delta") String deltaDate,
			@QueryParam("workflowState") String workflowState,
			@QueryParam("ownerId") String ownerId,
			@QueryParam("fromCompletedDate") String fromCompletedDate,
			@QueryParam("toCompletedDate") String toCompletedDate) {
		setEnhancedLoggingCustomParameters();
		List<M> allItems;
		List<A> items;
		Date delta = null;

		String logInfo = getLogInfo();
		String apiCall = listResponseType.getDescriptor().getName();
		String logMessage = logInfo + apiCall + " FIND ALL ACTION ITEMS ";
		logger.info(logMessage);

		if(deltaDate != null) {
			delta = convertDate(deltaDate);
			if (delta == null) {
				logger.error("Invalid delta '" + deltaDate + "'");
				throw new RuntimeException("Invalid delta '" + deltaDate + "', should be in yyyy-MM-dd HH:mm:ss format");
			}
		}

		Map<String, Object> optionalParameters = new HashMap<String, Object>();
		if (delta != null) {
			optionalParameters.put("delta", delta);
		}
		if (workflowState != null) {
			optionalParameters.put("workflowState", workflowState);
		}
		if (ownerId != null) {
			optionalParameters.put("ownerId", ownerId);
		}
		if (fromCompletedDate != null) {
			Date date = convertDate(fromCompletedDate);
			if (date == null) {
				logger.error("Invalid fromCompletedDate '" + date + "'");
				throw new RuntimeException("Invalid fromCompletedDate '" + date + "', should be in yyyy-MM-dd HH:mm:ss format");
			}
			optionalParameters.put("fromCompletedDate", date);
		}
		if (toCompletedDate != null) {
			Date date = convertDate(toCompletedDate);
			if (date == null) {
				logger.error("Invalid toCompletedDate '" + date + "'");
				throw new RuntimeException("Invalid toCompletedDate '" + date + "', should be in yyyy-MM-dd HH:mm:ss format");
			}
			optionalParameters.put("toCompletedDate", date);
		}

		try {
			allItems = crudService().findAllActionItem(page, pageSize, optionalParameters);

			items = allItems
					.stream()
					.map(m -> toMessage(m))
					.collect(Collectors.toList());

			return Messages.ListResponseMessage.newBuilder()
					.setPageSize(pageSize)
					.setPage(page)
					.setTotal(crudService().countAllActionItem(optionalParameters))
					.setExtension(listResponseType, items)
					.build();
		} catch (UnsupportedOperationException ex) {
			throw new NotAllowedException("DELETE not allowed for this entity type");
		}
	}

    @GET
    @Path("actionItems/{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional(readOnly = true)
    public A findActionItem(@PathParam("id") String id) {
        setEnhancedLoggingCustomParameters();
        return this.find(id);
    }

    @POST
    @Path("actionItems/{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public A saveActionItem(A message) {
        setEnhancedLoggingCustomParameters();
        return this.save(message);
    }

    @PUT
    @Path("actionItems/{id}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public A updateActionItem(@PathParam("id") String id, A message) {
        setEnhancedLoggingCustomParameters();
        return this.update(id, message);
    }

    @GET
    @Path("actionItems/asset/{assetId}")
    @Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
    @Trace (dispatcher=true)
    @Transactional
    public Messages.ListResponseMessage findActionItemByAssetId(@PathParam("assetId") String id, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize, @QueryParam("openActionItems") boolean openActionItems) {
        setEnhancedLoggingCustomParameters();
        List<M> allItems;
        List<A> items;
        String logInfo = getLogInfo();
        String apiCall = listResponseType.getDescriptor().getName();
        String logMessage = logInfo + apiCall + " FIND ACTION ITEMS with id " + id;
        logger.info(logMessage);

        try {
            allItems = crudService().findActionItemByAssetId(id, page, pageSize, openActionItems);

            items = allItems
                    .stream()
                    .map(m -> toMessage(m))
                    .collect(Collectors.toList());

            return Messages.ListResponseMessage.newBuilder()
                    .setPageSize(pageSize)
                    .setPage(page)
                    .setTotal(allItems.size())
                    .setExtension(listResponseType, items)
                    .build();
        }
        catch(UnsupportedOperationException ex) {
            throw new NotAllowedException("DELETE not allowed for this entity type");
        }

    }

    public String getLogInfo() {
        String user = getCurrentUser().getUserID();
        String tenant = getCurrentTenant().getDisplayName();

        String message = user + " from tenant " + tenant + " made the API Call: ";

        return message;
    }

    public Extension<Messages.ListResponseMessage, List<A>> getListResponseType() {
        return listResponseType;
    }

    public Date convertDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate;
        try {
            parsedDate = formatter.parse(dateInString);
        } catch (Exception e) {
            parsedDate = null;
        }
        return parsedDate;
    }
}
