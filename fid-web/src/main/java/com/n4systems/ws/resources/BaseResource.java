package com.n4systems.ws.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.n4systems.util.ArrayUtils;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.ResourceContext;

public abstract class BaseResource<M, W> {
	protected final ConversionHelper converter;
	protected final ResourceContext context;
	protected final ResourceDefiner<M, W> definer;
	
	protected BaseResource(ResourceContext context, ConversionHelper converter, ResourceDefiner<M, W> definer) {
		this.context = context;
		this.converter = converter;
		this.definer = definer;
	}
	
	protected BaseResource(UriInfo uriInfo, ResourceDefiner<M, W> definer) {
		this(new ResourceContext(uriInfo), new ConversionHelper(), definer);
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public W[] getList() {
		List<W> wsList = converter.convertList(definer.getResourceListLoader(context.getLoaderFactory()), definer.getResourceConverter());
		return ArrayUtils.toArray(wsList, definer.getWsModelClass());
	}
	
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public W getSingle(@PathParam("id") long id) {
		W wsModel = converter.convertSingle(definer.getResourceIdLoader(context.getLoaderFactory()).setId(id), definer.getResourceConverter());
		return wsModel;
	}
}
