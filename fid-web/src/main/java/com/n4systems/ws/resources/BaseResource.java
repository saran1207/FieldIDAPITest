package com.n4systems.ws.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.lastmod.WsLastModified;
import com.n4systems.ws.model.lastmod.WsLastModifiedConverter;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.ResourceContext;

public abstract class BaseResource<M, W> {
	protected final ConversionHelper converter;
	protected final ResourceContext context;
	protected final WsModelConverter<LastModified, WsLastModified> lastModifiedConverter;
	protected final ResourceDefiner<M, W> definer;
	
	protected BaseResource(ResourceContext context, ConversionHelper converter, WsModelConverter<LastModified, WsLastModified> lastModifiedConverter, ResourceDefiner<M, W> definer) {
		this.context = context;
		this.converter = converter;
		this.definer = definer;
		this.lastModifiedConverter = lastModifiedConverter;
	}
	
	protected BaseResource(UriInfo uriInfo, ResourceDefiner<M, W> definer) {
		this(new ResourceContext(uriInfo), new ConversionHelper(), new WsLastModifiedConverter(), definer);
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public WsLastModified[] getList() {
		List<WsLastModified> wsList = converter.convertList(definer.getLastModifiedLoader(context.getLoaderFactory()), lastModifiedConverter);
		return wsList.toArray(new WsLastModified[wsList.size()]);
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
