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
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CrudResource<M extends AbstractEntity, A extends GeneratedMessage, B extends GeneratedMessage.Builder> extends FieldIdPersistenceService {

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
	@Transactional(readOnly = true)
	public Messages.ListResponseMessage findAll(@QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
		List<A> items = crudService()
				.findAll(page, pageSize)
				.stream()
				.map(m -> toMessage(m))
				.collect(Collectors.toList());

        return Messages.ListResponseMessage.newBuilder()
                .setPageSize(pageSize)
                .setPage(page)
                .setTotal(crudService().count())
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
	@Transactional(readOnly = true)
	public A find(@PathParam("id") String id) {
		return toMessage(testNotFound(crudService().findByPublicId(id)));
	}

	@POST
	@Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Transactional
	public A save(A message) {
		return toMessage(crudService().save(toModel(message)));
	}

	@PUT
	@Path("{id}")
	@Consumes({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Produces({"application/x-protobuf64", MediaType.APPLICATION_JSON})
	@Transactional
	public A update(@PathParam("id") String id, A message) {
		return toMessage(crudService().update(merge(message, testNotFound(crudService().findByPublicId(id)))));
	}
}
