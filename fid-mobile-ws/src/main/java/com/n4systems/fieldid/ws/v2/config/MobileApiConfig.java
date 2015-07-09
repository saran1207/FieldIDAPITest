package com.n4systems.fieldid.ws.v2.config;

import com.n4systems.fieldid.config.FieldIdCoreConfig;
import com.n4systems.fieldid.config.FieldIdDownloadConfig;
import com.n4systems.fieldid.config.FieldIdEntityRemovalConfig;
import com.n4systems.fieldid.ws.v2.filters.*;
import com.n4systems.fieldid.ws.v2.resources.asset.ApiAssetResource;
import com.n4systems.fieldid.ws.v2.resources.asset.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.ws.v2.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v2.resources.authentication.AuthenticationResource;
import com.n4systems.fieldid.ws.v2.resources.event.ApiEventResource;
import com.n4systems.fieldid.ws.v2.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.fieldid.ws.v2.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v2.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v2.resources.tenant.ApiTenantResource;
import com.n4systems.fieldid.ws.v2.resources.user.ApiPersonResource;
import com.n4systems.fieldid.ws.v2.resources.user.ApiUserResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import({ FieldIdCoreConfig.class, FieldIdDownloadConfig.class, FieldIdEntityRemovalConfig.class })
public class MobileApiConfig {

	@Bean
	@Scope("request")
	public RequestContext requestContext() {
		return new RequestContext();
	}

	@Bean
	@Scope("request")
	public CorsResponseFilter corsResponseFilter() {
		return new CorsResponseFilter();
	}

	@Bean
	@Scope("request")
	public SecurityRequestFilter securityRequestFilter() {
		return new SecurityRequestFilter();
	}

	@Bean
	@Scope("request")
	public CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}

	@Bean
	@Scope("request")
	public ServerInformationResponseFilter serverInformationResponseFilter() {
		return new ServerInformationResponseFilter();
	}

	@Bean
	@Scope("request")
	public PlatformContextRequestFilter platformContextRequestFilter() {
		return new PlatformContextRequestFilter();
	}

	@Bean
	@Scope("request")
	public ApiAttributeValueResource apiAttributeValueResource() {
		return new ApiAttributeValueResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetResource apiAssetResource() {
		return new ApiAssetResource();
	}

	@Bean
	@Scope("request")
	public AuthenticationResource authenticationResource() {
		return new AuthenticationResource();
	}

	@Bean
	@Scope("request")
	public ApiOfflineProfileResource apiOfflineProfileResource() {
		return new ApiOfflineProfileResource();
	}

	@Bean
	@Scope("request")
	public ApiTenantResource apiTenantResource() {
		return new ApiTenantResource();
	}

	@Bean
	@Scope("request")
	public ApiPersonResource apiPersonResource() {
		return new ApiPersonResource();
	}

	@Bean
	@Scope("request")
	public ApiUserResource apiUserResource() {
		return new ApiUserResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetAttachmentResource apiAssetAttachmentResource() {
		return new ApiAssetAttachmentResource();
	}

	@Bean
	@Scope("request")
	public ApiEventResource apiEventResource() {
		return new ApiEventResource();
	}

	@Bean
	@Scope("request")
	public ApiEventAttachmentResource apiEventAttachmentResource() {
		return new ApiEventAttachmentResource();
	}

	@Bean
	@Scope("request")
	public ApiEventHistoryResource ApiEventHistoryResource() {
		return new ApiEventHistoryResource();
	}
}
