package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetResource;
import com.n4systems.fieldid.ws.v1.resources.asset.ApiSubAssetResource;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.assetcount.ApiAssetCountResource;
import com.n4systems.fieldid.ws.v1.resources.assetstatus.ApiAssetStatusResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.ApiAssetTypeResource;
import com.n4systems.fieldid.ws.v1.resources.authentication.AuthenticationResource;
import com.n4systems.fieldid.ws.v1.resources.commenttemplate.ApiCommentTemplateResource;
import com.n4systems.fieldid.ws.v1.resources.event.ApiEventResource;
import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.eventbook.ApiEventBookResource;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.fieldid.ws.v1.resources.eventstatus.ApiEventStatusResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.hello.ApiHelloResource;
import com.n4systems.fieldid.ws.v1.resources.location.ApiPredefinedLocationResource;
import com.n4systems.fieldid.ws.v1.resources.logging.ApiLoggingResource;
import com.n4systems.fieldid.ws.v1.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v1.resources.org.ApiOrgResource;
import com.n4systems.fieldid.ws.v1.resources.search.ApiSearchResource;
import com.n4systems.fieldid.ws.v1.resources.smartsearch.ApiSmartSearchResource;
import com.n4systems.fieldid.ws.v1.resources.synchronization.ApiSynchronizationResource;
import com.n4systems.fieldid.ws.v1.resources.tenant.ApiTenantResource;
import com.n4systems.fieldid.ws.v1.resources.unit.ApiUnitResource;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUserResource;

@Configuration
public class FieldIdWsConfig {

	@Bean
	@Scope("singleton")
	public CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}

	@Bean
	public AuthenticationResource authenticationResource() {
		return new AuthenticationResource();
	}

	@Bean
	public ApiOrgResource apiOrgResource() {
		return new ApiOrgResource();
	}

	@Bean
	public ApiUserResource apiUserResource() {
		return new ApiUserResource();
	}

	@Bean
	public ApiPredefinedLocationResource apiPredefinedLocationResource() {
		return new ApiPredefinedLocationResource();
	}

	@Bean
	public ApiAssetStatusResource apiAssetStatusResource() {
		return new ApiAssetStatusResource();
	}
	
	@Bean
	public ApiEventStatusResource apiEventStatusResource() {
		return new ApiEventStatusResource();
	}

	@Bean
	public ApiCommentTemplateResource apiCommentTemplateResource() {
		return new ApiCommentTemplateResource();
	}

	@Bean
	public ApiEventBookResource apiEventBookResource() {
		return new ApiEventBookResource();
	}

	@Bean
	public ApiUnitResource apiUnitResource() {
		return new ApiUnitResource();
	}
	
	@Bean
	public ApiAssetTypeResource apiAssetTypeResource() {
		return new ApiAssetTypeResource();
	}
	
	@Bean
	public ApiAssetResource apiAssetResource() {
		return new ApiAssetResource();
	}
	
	@Bean
	public ApiSmartSearchResource apiSmartSearchResource() {
		return new ApiSmartSearchResource();
	}
	
	@Bean
	public ApiAssetCountResource apiAssetCountResource() {
		return new ApiAssetCountResource();
	}
	
	@Bean
	public ApiEventHistoryResource apiEventHistoryResource() {
		return new ApiEventHistoryResource();
	}
	
	@Bean
	public ApiEventScheduleResource apiEventScheduleResource() {
		return new ApiEventScheduleResource();
	}
	
	@Bean
	public ApiAssetAttachmentResource apiAssetAttachmentResource() {
		return new ApiAssetAttachmentResource();
	}
	
	@Bean
	public ApiEventTypeResource apiEventTypeResource() {
		return new ApiEventTypeResource();
	}
	
	@Bean
	public ApiOfflineProfileResource apiOfflineProfileResource() {
		return new ApiOfflineProfileResource();
	}
	
	@Bean
	public ApiSynchronizationResource apiSynchronizationResource() {
		return new ApiSynchronizationResource();
	}
	
	@Bean
	public ApiSearchResource apiSearchResource() {
		return new ApiSearchResource();
	}
	
	@Bean
	public ApiEventResource apiEventResource() {
		return new ApiEventResource();
	}
	
	@Bean
	public ApiEventAttachmentResource apiEventAttachmentResource() {
		return new ApiEventAttachmentResource();
	}
	
	@Bean
	public ApiTenantResource apiTenantResource() {
		return new ApiTenantResource();
	}
	
	@Bean
	public ApiHelloResource apiHelloResource() {
		return new ApiHelloResource();
	}
	
	@Bean
	public ApiLoggingResource apiLoggingResource() {
		return new ApiLoggingResource();
	}
	
	@Bean
	public ApiSubAssetResource apiSubAssetResource() {
		return new ApiSubAssetResource();
	}
}
