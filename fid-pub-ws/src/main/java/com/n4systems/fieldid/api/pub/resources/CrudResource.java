package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.serialization.ListResponse;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.parents.AbstractEntity;
import com.squareup.wire.Extension;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class CrudResource<M extends AbstractEntity, A> extends FieldIdPersistenceService {

	protected abstract CrudService<M> crudService();
	protected abstract A marshal(M model);
	protected abstract M unmarshal(A apiModel);
	protected abstract Extension<ListResponse, List<A>> listResponseType();

	protected <T> void ifNotNull(T t, Consumer<T> then) {
		if (t != null) {
			then.accept(t);
		}
	}

	protected M testNotFound(M model) {
		if (model == null) {
			throw new NotFoundException();
		}
		return model;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse findAll(@QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
		List<A> items = crudService()
				.findAll(page, pageSize)
				.stream()
				.map(this::marshal)
				.collect(Collectors.toList());

		return new ListResponse.Builder()
				.page(page)
				.pageSize(pageSize)
				.total(crudService().count())
				.setExtension(listResponseType(), items)
				.build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public A find(@PathParam("id") String id) {
		return marshal(testNotFound(crudService().findByPublicId(id)));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public A save(A apiModel) {
		return marshal(crudService().save(unmarshal(apiModel)));
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public A update(@PathParam("id") String id, A apiModel) {
		return marshal(crudService().update(PublicIdEncoder.decode(id), unmarshal(apiModel)));
	}
}
