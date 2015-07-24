package com.n4systems.fieldid.ws.v2.config;

import com.n4systems.fieldid.config.FieldIdCoreConfig;
import com.n4systems.fieldid.config.FieldIdDownloadConfig;
import com.n4systems.fieldid.config.FieldIdEntityRemovalConfig;
import com.n4systems.fieldid.ws.v2.filters.*;
import com.n4systems.fieldid.ws.v2.resources.authentication.ApiTenantConverter;
import com.n4systems.fieldid.ws.v2.resources.authentication.AuthenticationResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.ApiAssetResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.attributevalues.ApiAttributeValueConverter;
import com.n4systems.fieldid.ws.v2.resources.customerdata.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.event.ApiCriteriaResultConverter;
import com.n4systems.fieldid.ws.v2.resources.customerdata.event.ApiEventResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.event.ApiEventToModelConverter;
import com.n4systems.fieldid.ws.v2.resources.customerdata.eventattachment.ApiEventAttachmentResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.procedure.ApiProcedureResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.proceduredefinition.ApiProcedureDefinitionResource;
import com.n4systems.fieldid.ws.v2.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.assetstatus.ApiAssetStatusResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.assettype.ApiAssetTypeResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.assettypeattachment.ApiAssetTypeAttachmentResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute.ApiAutoAttributeResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.commenttemplate.ApiCommentTemplateResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventbook.ApiEventBookResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.ApiEventFormResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria.ApiCriteriaConverter;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventstatus.ApiEventStatusResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype.ApiEventTypeResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.location.ApiPredefinedLocationResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.org.ApiOrgResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.person.ApiPersonResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.prioritycode.ApiPriorityCodeResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.energysources.ApiEnergySourceResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.isolationpointsourcetypes.ApiIsolationPointSourceTypeResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.preconfigureddevices.ApiPreconfiguredDeviceResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.warningtemplates.ApiWarningTemplateResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.unit.ApiUnitResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.user.ApiUserResource;
import com.n4systems.fieldid.ws.v2.resources.setupdata.usergroup.ApiUserGroupResource;
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
	public ApiAttributeValueConverter apiAttributeValueConverter() {
		return new ApiAttributeValueConverter();
	}

	@Bean
	@Scope("request")
	public ApiEventToModelConverter apiEventToModelConverter() {
		return new ApiEventToModelConverter();
	}

	@Bean
	@Scope("request")
	public ApiTenantConverter apiTenantResource() {
		return new ApiTenantConverter();
	}

	@Bean
	@Scope("request")
	public ApiCriteriaConverter apiCriteriaConverter() {
		return new ApiCriteriaConverter();
	}

	@Bean
	@Scope("request")
	public ApiCriteriaResultConverter apiCriteriaResultConverter() {
		return new ApiCriteriaResultConverter();
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
	public ApiEventHistoryResource apiEventHistoryResource() {
		return new ApiEventHistoryResource();
	}

	@Bean
	@Scope("request")
	public ApiOrgResource apiOrgResource() {
		return new ApiOrgResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetTypeResource apiAssetTypeResource() {
		return new ApiAssetTypeResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetStatusResource apiAssetStatusResource() {
		return new ApiAssetStatusResource();
	}

	@Bean
	@Scope("request")
	public ApiCommentTemplateResource apiCommentTemplateResource() {
		return new ApiCommentTemplateResource();
	}

	@Bean
	@Scope("request")
	public ApiEventStatusResource apiEventStatusResource() {
		return new ApiEventStatusResource();
	}

	@Bean
	@Scope("request")
	public ApiPredefinedLocationResource apiPredefinedLocationResource() {
		return new ApiPredefinedLocationResource();
	}

	@Bean
	@Scope("request")
	public ApiUnitResource apiUnitResource() {
		return new ApiUnitResource();
	}

	@Bean
	@Scope("request")
	public ApiUserGroupResource apiUserGroupResource() {
		return new ApiUserGroupResource();
	}

	@Bean
	@Scope("request")
	public ApiEventTypeResource apiEventTypeResource() {
		return new ApiEventTypeResource();
	}

	@Bean
	@Scope("request")
	public ApiPriorityCodeResource apiPriorityCodeResource() {
		return new ApiPriorityCodeResource();
	}

	@Bean
	@Scope("request")
	public ApiEventBookResource apiEventBookResource() {
		return new ApiEventBookResource();
	}

	@Bean
	@Scope("request")
	public ApiEventFormResource apiEventFormResource() {
		return new ApiEventFormResource();
	}

	@Bean
	@Scope("request")
	public ApiAssetTypeAttachmentResource apiAssetTypeAttachmentResource() {
		return new ApiAssetTypeAttachmentResource();
	}

	@Bean
	@Scope("request")
	public ApiPreconfiguredDeviceResource apiPreconfiguredDeviceResource() {
		return new ApiPreconfiguredDeviceResource();
	}

	@Bean
	@Scope("request")
	public ApiIsolationPointSourceTypeResource apiIsolationPointSourceTypeResource() {
		return new ApiIsolationPointSourceTypeResource();
	}

	@Bean
	@Scope("request")
	public ApiEnergySourceResource apiEnergySourceResource() {
		return new ApiEnergySourceResource();
	}

	@Bean
	@Scope("request")
	public ApiWarningTemplateResource apiWarningTemplateResource() {
		return new ApiWarningTemplateResource();
	}

	@Bean
	@Scope("request")
	public ApiProcedureResource apiProcedureResource() {
		return new ApiProcedureResource();
	}

	@Bean
	@Scope("request")
	public ApiProcedureDefinitionResource apiProcedureDefinitionResource() {
		return new ApiProcedureDefinitionResource();
	}

	@Bean
	@Scope("request")
	public ApiAutoAttributeResource apiAutoAttributeResource() {
		return new ApiAutoAttributeResource();
	}
}
