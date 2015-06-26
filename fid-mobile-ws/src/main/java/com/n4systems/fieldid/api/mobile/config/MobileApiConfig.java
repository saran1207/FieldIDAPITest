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

@Configuration
@Import({ FieldIdCoreConfig.class, FieldIdDownloadConfig.class, FieldIdEntityRemovalConfig.class })
public class MobileApiConfig {

	@Bean
	public CorsResponseFilter corsResponseFilter() {
		return new CorsResponseFilter();
	}

	@Bean
	public SecurityRequestFilter securityRequestFilter() {
		return new SecurityRequestFilter();
	}

	@Bean
	public CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}

	@Bean
	public ServerInformationResponseFilter serverInformationResponseFilter() {
		return new ServerInformationResponseFilter();
	}

	@Bean
	public PlatformContextRequestFilter platformContextRequestFilter() {
		return new PlatformContextRequestFilter();
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
	public ApiPersonResource apiPersonResource() {
		return new ApiPersonResource();
	}

	@Bean
	public ApiUserGroupResource apiUserGroupResource() {
		return new ApiUserGroupResource();
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
	public ApiPriorityCodeResource apiPriorityCodeResource() {
		return new ApiPriorityCodeResource();
	}

	@Bean
	public ApiAutoAttributeCriteriaResource apiAutoAttributeCriteriaResource() {
		return new ApiAutoAttributeCriteriaResource();
	}

	@Bean
	public ApiAutoAttributeDefinitionResource apiAutoAttributeDefinitionResource() {
		return new ApiAutoAttributeDefinitionResource();
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
	public ApiTriggerEventResource apiTriggerEventResource() {
		return new ApiTriggerEventResource();
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
	public ApiProcedureDefinitionResource apiProcedureDefinitionResource() {
		return new ApiProcedureDefinitionResource();
	}

	@Bean
	public ApiProcedureResource apiProcedureResource() {
		return new ApiProcedureResource();
	}

	@Bean
	public ApiDeviceLockResource apiDeviceLockResource() {
		return new ApiDeviceLockResource();
	}

	@Bean
	public ApiSavedEventResource apiSavedEventResource() {
		return new ApiSavedEventResource();
	}

	@Bean
	public ApiSavedEventFormResource apiSavedEventFormResource() {
		return new ApiSavedEventFormResource();
	}

	@Bean
	public ApiEventFormResultResource apiEventFormResultResource() {
		return new ApiEventFormResultResource();
	}

	@Bean
	public ApiExistingEventFormResultResource apiExistingEventFormResultResource() {
		return new ApiExistingEventFormResultResource();
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

	@Bean
	public ApiCriteriaImagesResource apiCriteriaImagesResource() {
		return new ApiCriteriaImagesResource();
	}

	@Bean
	public ApiAttributeValueResource apiAttributeValueResource() {
		return new ApiAttributeValueResource();
	}

	@Bean
	public ApiProcedureDefinitionResourceV2 apiProcedureDefinitionResourceV2() {
		return new ApiProcedureDefinitionResourceV2();
	}

	@Bean
	public ApiWarningTemplateResource apiWarningTemplateResource() {
		return new ApiWarningTemplateResource();
	}

	@Bean
	public ApiPreconfiguredDeviceResource apiPreconfiguredDeviceResource() {
		return new ApiPreconfiguredDeviceResource();
	}

	@Bean
	public ApiEnergySourceResource apiEnergySourceResource() {
		return new ApiEnergySourceResource();
	}
}
