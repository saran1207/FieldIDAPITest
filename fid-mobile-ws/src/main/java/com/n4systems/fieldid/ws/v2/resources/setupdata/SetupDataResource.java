package com.n4systems.fieldid.ws.v2.resources.setupdata;

import com.n4systems.fieldid.ws.v2.resources.ApiKey;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.model.ApiModel;
import com.n4systems.fieldid.ws.v2.resources.model.DateParam;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public abstract class SetupDataResource<A extends ApiModel, E extends AbstractEntity, K extends ApiKey> extends ApiResource<A, E> {

	private final String idField;
	private final Class<E> entityClass;
	private final boolean allowArchived;

	protected SetupDataResource(String idField, Class<E> entityClass, boolean allowArchived) {
		this.idField = idField;
		this.entityClass = entityClass;
		this.allowArchived = allowArchived;
	}

	protected void addTermsToLatestQuery(QueryBuilder<?> query) {}

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<K> ids) {
		if (ids.isEmpty()) return new ArrayList<>();

		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(entityClass, securityContext.getUserSecurityFilter(allowArchived));
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, idField, "modified"));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, idField, unwrapKeys(ids)));

		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@GET
	@Path("query/latest")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryLatest(@QueryParam("since") DateParam since) {
		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(entityClass, securityContext.getUserSecurityFilter(allowArchived));
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, idField, "modified"));

		if (since != null) {
			query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GT, "modified", since));
		}
		addTermsToLatestQuery(query);

		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<A> findAll(@QueryParam("id") List<K> ids) {
		if (ids.isEmpty()) return new ArrayList<>();

		QueryBuilder<E> query = createTenantSecurityBuilder(entityClass, allowArchived);
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, idField, unwrapKeys(ids)));

		List<E> entityModels = persistenceService.findAll(query);
		List<A> apiModels = convertAllEntitiesToApiModels(entityModels);
		return apiModels;
	}
}
