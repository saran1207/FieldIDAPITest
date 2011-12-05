package com.n4systems.fieldid.ws.v1.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
public abstract class SetupDataResource<A, E extends EntityWithTenant> extends FieldIdPersistenceService {
	private final Class<E> entityClass;
	
	protected SetupDataResource(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	protected abstract A convertEntityToApiModel(E entityModel);
	
	protected QueryBuilder<E> createFindAllBuilder(Date after) {
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass, true);
		if (after != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GT, "modified", after));
		}
		builder.addOrder("id");
		return builder;
	}

	protected QueryBuilder<E> createFindSingleBuilder(String id) {
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass, true);
		builder.addWhere(WhereClauseFactory.create("id", Long.valueOf(id)));
		return builder;
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<A> findAll(
			@QueryParam("after") DateParam after,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize) {
		
		QueryBuilder<E> builder = createFindAllBuilder(after);
		List<E> entityModels = persistenceService.findAll(builder, page, pageSize);
		Long total = persistenceService.count(builder);
		
		List<A> apiModels = convertAllEntitiesToApiModels(entityModels);
		ListResponse<A> response = new ListResponse<A>(apiModels, page, pageSize, total);
		return response;
	}
	
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public A find(@PathParam("id") String id) {
		QueryBuilder<E> builder = createFindSingleBuilder(id);
		E entityModel = persistenceService.find(builder);
		if (entityModel == null) {
			throw new NotFoundException("Resource not found at [" + id + "]");
		}
		
		A apiModel = convertEntityToApiModel(entityModel);
		return apiModel;
	}
	
	private List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
		List<A> apiModel = new ArrayList<A>();
		for (E entityModel: entityModels) {
			apiModel.add(convertEntityToApiModel(entityModel));
		}
		return apiModel;
	}
}
