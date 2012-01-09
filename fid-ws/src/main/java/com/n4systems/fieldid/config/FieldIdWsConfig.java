package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetResource;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.assetcount.ApiAssetCountResource;
import com.n4systems.fieldid.ws.v1.resources.assetstatus.ApiAssetStatusResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.ApiAssetTypeResource;
import com.n4systems.fieldid.ws.v1.resources.authentication.AuthenticationResource;
import com.n4systems.fieldid.ws.v1.resources.commenttemplate.ApiCommentTemplateResource;
import com.n4systems.fieldid.ws.v1.resources.eventbook.ApiEventBookResource;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.fieldid.ws.v1.resources.location.ApiPredefinedLocationResource;
import com.n4systems.fieldid.ws.v1.resources.org.ApiOrgResource;
import com.n4systems.fieldid.ws.v1.resources.smartsearch.ApiSmartSearchResource;
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
	public ApiAssetAttachmentResource apiAssetAttachmentResource() {
		return new ApiAssetAttachmentResource();
	}
	
	@Bean
	public ApiEventHistoryResource apiEventHistoryResource() {
		return new ApiEventHistoryResource();
	}
	
	@Bean
	public ApiEventScheduleResource apiEventScheduleResource() {
		return new ApiEventScheduleResource();
	}
	
}
