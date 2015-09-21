package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.isolationpointsourcetypes;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v2.resources.ApiKey;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyLong;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.model.DateParam;
import com.n4systems.model.IsolationPointSourceType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("isolationPointSourceType")
public class ApiIsolationPointSourceTypeResource extends FieldIdPersistenceService {

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<ApiKeyLong> ids) {
		if (ids.isEmpty()) return new ArrayList<>();

		return ApiKey.unwrap(ids)
				.stream()
				.map(IsolationPointSourceType::forId)
				.map(st -> new ApiModelHeader<>(st.getId(), st.getModified()))
				.collect(Collectors.toList());
	}

	@GET
	@Path("query/latest")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryLatest(@QueryParam("since") DateParam since) {
		return IsolationPointSourceType.modifiedAfter(since)
				.stream()
				.map(st -> new ApiModelHeader<>(st.getId(), st.getModified()))
				.collect(Collectors.toList());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiIsolationPointSourceType> findAll(@QueryParam("id") List<ApiKeyLong> ids) {
		if (ids.isEmpty()) return new ArrayList<>();

		return ApiKey.unwrap(ids)
				.stream()
				.map(IsolationPointSourceType::forId)
				.map(st -> {
					ApiIsolationPointSourceType ast = new ApiIsolationPointSourceType();
					ast.setSid(st.getId());
					ast.setActive(true);
					ast.setModified(st.getModified());
					ast.setSource(st.name());
					ast.setSourceText(st.getIdentifier());
					return ast;
				})
				.collect(Collectors.toList());
	}
}
