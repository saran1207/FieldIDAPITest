package com.n4systems.fieldid.api.mobile.config;

import com.n4systems.fieldid.api.mobile.filters.*;
import com.n4systems.fieldid.api.mobile.resources.asset.ApiAssetResource;
import com.n4systems.fieldid.api.mobile.resources.asset.ApiDeviceLockResource;
import com.n4systems.fieldid.api.mobile.resources.asset.ApiSubAssetResource;
import com.n4systems.fieldid.api.mobile.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.api.mobile.resources.assetcount.ApiAssetCountResource;
import com.n4systems.fieldid.api.mobile.resources.assetstatus.ApiAssetStatusResource;
import com.n4systems.fieldid.api.mobile.resources.assettype.ApiAssetTypeResource;
import com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.api.mobile.resources.autoattribute.ApiAutoAttributeCriteriaResource;
import com.n4systems.fieldid.api.mobile.resources.autoattribute.ApiAutoAttributeDefinitionResource;
import com.n4systems.fieldid.api.mobile.resources.commenttemplate.ApiCommentTemplateResource;
import com.n4systems.fieldid.api.mobile.resources.event.ApiEventFormResultResource;
import com.n4systems.fieldid.api.mobile.resources.event.ApiEventResource;
import com.n4systems.fieldid.api.mobile.resources.event.ApiExistingEventFormResultResource;
import com.n4systems.fieldid.api.mobile.resources.event.actions.prioritycode.ApiPriorityCodeResource;
import com.n4systems.fieldid.api.mobile.resources.event.criteria.ApiCriteriaImagesResource;
import com.n4systems.fieldid.api.mobile.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.fieldid.api.mobile.resources.eventbook.ApiEventBookResource;
import com.n4systems.fieldid.api.mobile.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.api.mobile.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.fieldid.api.mobile.resources.eventschedule.ApiTriggerEventResource;
import com.n4systems.fieldid.api.mobile.resources.eventstatus.ApiEventStatusResource;
import com.n4systems.fieldid.api.mobile.resources.eventtype.ApiEventTypeResource;
import com.n4systems.fieldid.api.mobile.resources.hello.ApiHelloResource;
import com.n4systems.fieldid.api.mobile.resources.location.ApiPredefinedLocationResource;
import com.n4systems.fieldid.api.mobile.resources.logging.ApiLoggingResource;
import com.n4systems.fieldid.api.mobile.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.api.mobile.resources.org.ApiOrgResource;
import com.n4systems.fieldid.api.mobile.resources.procedure.*;
import com.n4systems.fieldid.api.mobile.resources.savedEvent.ApiSavedEventFormResource;
import com.n4systems.fieldid.api.mobile.resources.savedEvent.ApiSavedEventResource;
import com.n4systems.fieldid.api.mobile.resources.search.ApiSearchResource;
import com.n4systems.fieldid.api.mobile.resources.smartsearch.ApiSmartSearchResource;
import com.n4systems.fieldid.api.mobile.resources.synchronization.ApiSynchronizationResource;
import com.n4systems.fieldid.api.mobile.resources.tenant.ApiTenantResource;
import com.n4systems.fieldid.api.mobile.resources.unit.ApiUnitResource;
import com.n4systems.fieldid.api.mobile.resources.user.ApiPersonResource;
import com.n4systems.fieldid.api.mobile.resources.user.ApiUserResource;
import com.n4systems.fieldid.api.mobile.resources.usergroup.ApiUserGroupResource;
import com.n4systems.fieldid.config.FieldIdCoreConfig;
import com.n4systems.fieldid.config.FieldIdDownloadConfig;
import com.n4systems.fieldid.config.FieldIdEntityRemovalConfig;
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
	public ApiOrgResource apiOrgResource() {
		return new ApiOrgResource();
	}

	@Bean
	@Scope("request")
	public ApiUserResource apiUserResource() {
		return new ApiUserResource();
	}

	@Bean
	@Scope("request")
	public ApiPersonResource apiPersonResource() {
		return new ApiPersonResource();
	}

	@Bean
	@Scope("request")
	public ApiUserGroupResource apiUserGroupResource() {
		return new ApiUserGroupResource();
	}

	@Bean
	@Scope("request")
	public ApiPredefinedLocationResource apiPredefinedLocationResource() {
		return new ApiPredefinedLocationResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetStatusResource apiAssetStatusResource() {
		return new ApiAssetStatusResource();
	}

	@Bean
	@Scope("request")
	public ApiEventStatusResource apiEventStatusResource() {
		return new ApiEventStatusResource();
	}

	@Bean
	@Scope("request")
	public ApiCommentTemplateResource apiCommentTemplateResource() {
		return new ApiCommentTemplateResource();
	}

	@Bean
	@Scope("request")
	public ApiPriorityCodeResource apiPriorityCodeResource() {
		return new ApiPriorityCodeResource();
	}

	@Bean
	@Scope("request")
	public ApiAutoAttributeCriteriaResource apiAutoAttributeCriteriaResource() {
		return new ApiAutoAttributeCriteriaResource();
	}

	@Bean
	@Scope("request")
	public ApiAutoAttributeDefinitionResource apiAutoAttributeDefinitionResource() {
		return new ApiAutoAttributeDefinitionResource();
	}

	@Bean
	@Scope("request")
	public ApiEventBookResource apiEventBookResource() {
		return new ApiEventBookResource();
	}

	@Bean
	@Scope("request")
	public ApiUnitResource apiUnitResource() {
		return new ApiUnitResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetTypeResource apiAssetTypeResource() {
		return new ApiAssetTypeResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetResource apiAssetResource() {
		return new ApiAssetResource();
	}

	@Bean
	@Scope("request")
	public ApiSmartSearchResource apiSmartSearchResource() {
		return new ApiSmartSearchResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetCountResource apiAssetCountResource() {
		return new ApiAssetCountResource();
	}

	@Bean
	@Scope("request")
	public ApiEventHistoryResource apiEventHistoryResource() {
		return new ApiEventHistoryResource();
	}

	@Bean
	@Scope("request")
	public ApiEventScheduleResource apiEventScheduleResource() {
		return new ApiEventScheduleResource();
	}

	@Bean
	@Scope("request")
	public ApiTriggerEventResource apiTriggerEventResource() {
		return new ApiTriggerEventResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetAttachmentResource apiAssetAttachmentResource() {
		return new ApiAssetAttachmentResource();
	}

	@Bean
	@Scope("request")
	public ApiEventTypeResource apiEventTypeResource() {
		return new ApiEventTypeResource();
	}

	@Bean
	@Scope("request")
	public ApiOfflineProfileResource apiOfflineProfileResource() {
		return new ApiOfflineProfileResource();
	}

	@Bean
	@Scope("request")
	public ApiSynchronizationResource apiSynchronizationResource() {
		return new ApiSynchronizationResource();
	}

	@Bean
	@Scope("request")
	public ApiSearchResource apiSearchResource() {
		return new ApiSearchResource();
	}

	@Bean
	@Scope("request")
	public ApiEventResource apiEventResource() {
		return new ApiEventResource();
	}

	@Bean
	@Scope("request")
	public ApiProcedureDefinitionResource apiProcedureDefinitionResource() {
		return new ApiProcedureDefinitionResource();
	}

	@Bean
	@Scope("request")
	public ApiProcedureResource apiProcedureResource() {
		return new ApiProcedureResource();
	}

	@Bean
	@Scope("request")
	public ApiDeviceLockResource apiDeviceLockResource() {
		return new ApiDeviceLockResource();
	}

	@Bean
	@Scope("request")
	public ApiSavedEventResource apiSavedEventResource() {
		return new ApiSavedEventResource();
	}

	@Bean
	@Scope("request")
	public ApiSavedEventFormResource apiSavedEventFormResource() {
		return new ApiSavedEventFormResource();
	}

	@Bean
	@Scope("request")
	public ApiEventFormResultResource apiEventFormResultResource() {
		return new ApiEventFormResultResource();
	}

	@Bean
	@Scope("request")
	public ApiExistingEventFormResultResource apiExistingEventFormResultResource() {
		return new ApiExistingEventFormResultResource();
	}

	@Bean
	@Scope("request")
	public ApiEventAttachmentResource apiEventAttachmentResource() {
		return new ApiEventAttachmentResource();
	}

	@Bean
	@Scope("request")
	public ApiTenantResource apiTenantResource() {
		return new ApiTenantResource();
	}

	@Bean
	@Scope("request")
	public ApiHelloResource apiHelloResource() {
		return new ApiHelloResource();
	}

	@Bean
	@Scope("request")
	public ApiLoggingResource apiLoggingResource() {
		return new ApiLoggingResource();
	}

	@Bean
	@Scope("request")
	public ApiSubAssetResource apiSubAssetResource() {
		return new ApiSubAssetResource();
	}

	@Bean
	@Scope("request")
	public ApiCriteriaImagesResource apiCriteriaImagesResource() {
		return new ApiCriteriaImagesResource();
	}

	@Bean
	@Scope("request")
	public ApiAttributeValueResource apiAttributeValueResource() {
		return new ApiAttributeValueResource();
	}

	@Bean
	@Scope("request")
	public ApiProcedureDefinitionResourceV2 apiProcedureDefinitionResourceV2() {
		return new ApiProcedureDefinitionResourceV2();
	}

	@Bean
	@Scope("request")
	public ApiWarningTemplateResource apiWarningTemplateResource() {
		return new ApiWarningTemplateResource();
	}

	@Bean
	@Scope("request")
	public ApiPreconfiguredDeviceResource apiPreconfiguredDeviceResource() {
		return new ApiPreconfiguredDeviceResource();
	}

	@Bean
	@Scope("request")
	public ApiEnergySourceResource apiEnergySourceResource() {
		return new ApiEnergySourceResource();
	}
}
