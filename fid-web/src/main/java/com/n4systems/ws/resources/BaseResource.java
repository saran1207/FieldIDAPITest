package com.n4systems.ws.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.ArrayUtils;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.lastmod.WsLastModified;
import com.n4systems.ws.model.lastmod.WsLastModifiedConverter;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.ResourceContext;
import com.n4systems.ws.utils.WsLoaderHelper;

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
	public WsLastModified[] list() {
		List<WsLastModified> wsList = converter.convertList(definer.getLastModifiedLoader(context.getLoaderFactory()), lastModifiedConverter);
		return wsList.toArray(new WsLastModified[wsList.size()]);
	}
	
	@GET
	@Path("{csvIds}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public W[] get(@PathParam("csvIds") String ids) {
		IdLoader<? extends Loader<M>> loader = definer.getResourceIdLoader(context.getLoaderFactory());
		
		List<M> models = new ArrayList<M>();
		for (Long id: parseIds(ids)) {
			models.add(WsLoaderHelper.load(loader.setId(id)));
		}
		
		List<W> wsModels = definer.getResourceConverter().fromModels(models);
		return ArrayUtils.toArray(wsModels, definer.getWsModelClass());
	}
	
	private List<Long> parseIds(String ids) {
		List<Long> idList = new ArrayList<Long>();
		for (String strId: ids.split(",")) {
			try {
				idList.add(Long.parseLong(strId));
			} catch (NumberFormatException e) {}
		}
		return idList;
	}
}
