package com.n4systems.fieldid.ws.v1.resources;

import java.time.Duration;
import java.time.Instant;
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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
public abstract class SetupDataResource<A, E extends AbstractEntity> extends ApiResource<A, E> {

	private static Logger logger = Logger.getLogger(SetupDataResource.class);

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
		Instant b = Instant.now();
		List<E> entityModels = persistenceService.findAll(builder, page, pageSize);
		Instant a = Instant.now();
		logger.info("List Query: " + Duration.between(b,a).toNanos());

		Instant b1 = Instant.now();
		Long total = persistenceService.count(builder);
		Instant a1 = Instant.now();
		logger.info("Count Query: " + Duration.between(b1,a1).toNanos());

		Instant b2 = Instant.now();
		List<A> apiModels = convertAllEntitiesToApiModels(entityModels);
		Instant a2 = Instant.now();
		logger.info("Convert Entities: " + Duration.between(b2,a2).toNanos());
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
