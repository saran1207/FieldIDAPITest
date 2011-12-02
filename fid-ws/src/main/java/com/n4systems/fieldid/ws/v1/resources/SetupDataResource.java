package com.n4systems.fieldid.ws.v1.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public abstract class SetupDataResource<A, E extends EntityWithTenant> extends FieldIdPersistenceService {
	
	private final Class<E> entityClass;
	
	protected SetupDataResource(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	public abstract A convertEntityToApiModel(E entityModel);
	
	private List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
		List<A> apiModel = new ArrayList<A>();
		for (E entityModel: entityModels) {
			apiModel.add(convertEntityToApiModel(entityModel));
		}
		return apiModel;
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public ListResponse<A> findAll(
			@QueryParam("after") DateParam after,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize) {
		
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass);
		if (after != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GT, "modified", after));
		}
		builder.addOrder("id");
		
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
	public A find(@PathParam("id") Long id) {
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass);
		builder.addWhere(WhereClauseFactory.create("id", id));
		
		E entityModel = persistenceService.find(builder);
		if (entityModel == null) {
			throw new NotFoundException("Resource not found at [" + id + "]");
		}
		
		A apiModel = convertEntityToApiModel(entityModel);
		return apiModel;
	}
}
