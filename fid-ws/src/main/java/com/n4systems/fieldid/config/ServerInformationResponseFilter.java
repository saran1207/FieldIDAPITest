package com.n4systems.fieldid.config;

import com.n4systems.fieldid.version.FieldIdVersion;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import java.util.Date;

public class ServerInformationResponseFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		response.getHttpHeaders().putSingle("X-Timestamp", new Date().getTime());
		response.getHttpHeaders().putSingle("Server", "Field ID/" + FieldIdVersion.getVersion());
		return response;
	}

}
