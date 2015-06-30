package com.n4systems.fieldid.ws.v2.resources;

import com.n4systems.fieldid.ws.v2.resources.model.DateParam;
import com.n4systems.fieldid.ws.v2.resources.model.ListResponse;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Component
public abstract class SetupDataResource<A, E extends AbstractEntity> extends ApiResource<A, E> {
	private final Class<E> entityClass;
	private final boolean allowArchived;
	
	protected SetupDataResource(Class<E> entityClass, boolean allowArchived) {
		this.entityClass = entityClass;
		this.allowArchived = allowArchived;
	}
	
	protected QueryBuilder<E> createFindAllBuilder(Date after) {
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass, allowArchived);
		if (after != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GT, "modified", after));
		}
		builder.addOrder("id");
        addTermsToBuilder(builder);
		return builder;
	}

	protected QueryBuilder<E> createFindSingleBuilder(String id) {
		QueryBuilder<E> builder = createTenantSecurityBuilder(entityClass, true);
		builder.addWhere(WhereClauseFactory.create("id", Long.valueOf(id)));
        addTermsToBuilder(builder);
		return builder;
	}

    protected void addTermsToBuilder(QueryBuilder<E> builder) {}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<A> findAll(
			@QueryParam("after") DateParam after,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("500") @QueryParam("pageSize") int pageSize) {

		ListResponse<A> response = getApiPage(after, page, pageSize);
		return response;
	}

	protected ListResponse<A> getApiPage(DateParam after, int page, int pageSize) {
		QueryBuilder<E> builder = createFindAllBuilder(after);
		List<E> entityModels = persistenceService.findAll(builder, page, pageSize);
		Long total = persistenceService.count(builder);

		List<A> apiModels = convertAllEntitiesToApiModels(entityModels);
		return new ListResponse<A>(apiModels, page, pageSize, total);
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
	
}
