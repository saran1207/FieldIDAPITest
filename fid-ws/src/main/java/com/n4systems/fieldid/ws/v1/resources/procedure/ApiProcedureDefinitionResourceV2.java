package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.amazon.S3Service;
//import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.*;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
//@Path("procedureDefinitionV2")
public class ApiProcedureDefinitionResourceV2 extends SetupDataResource<ApiProcedureDefinitionV2, ProcedureDefinition> {

    private static final Logger log = Logger.getLogger(ApiProcedureDefinitionResource.class);

    @Autowired private ApiAttributeValueResource attrResource;
    @Autowired private S3Service s3Service;

    public ApiProcedureDefinitionResourceV2() {
        super(ProcedureDefinition.class, false);
    }

    @Override
    protected ApiProcedureDefinitionV2 convertEntityToApiModel(ProcedureDefinition procedureDef) {
        ApiProcedureDefinitionV2 apiProcedureDef = new ApiProcedureDefinitionV2();
        apiProcedureDef.setSid(procedureDef.getMobileId());
        apiProcedureDef.setModified(procedureDef.getModified());
        apiProcedureDef.setAssetId(procedureDef.getAsset().getMobileGUID());
        apiProcedureDef.setCompleteIsolationPointInOrder(procedureDef.isCompleteIsolationPointInOrder());
        apiProcedureDef.setElectronicIdentifier(procedureDef.getElectronicIdentifier());
        apiProcedureDef.setEquipmentDescription(procedureDef.getEquipmentDescription());
        apiProcedureDef.setIsolationPoints(convertIsolationPoints(procedureDef.getIsolationPoints()));
        apiProcedureDef.setEquipmentNumber(procedureDef.getEquipmentNumber());
        apiProcedureDef.setRevisionNumber(procedureDef.getRevisionNumber());
        apiProcedureDef.setProcedureCode(procedureDef.getProcedureCode());
        apiProcedureDef.setWarnings(procedureDef.getWarnings());
        apiProcedureDef.setActive(procedureDef.isActive());
        apiProcedureDef.setImages(convertImages(procedureDef, procedureDef.getImages()));
        return apiProcedureDef;
    }

    private List<ApiIsolationPoint> convertIsolationPoints(List<IsolationPoint> isolationPoints) {
        List<ApiIsolationPoint> apiIsolationPoints = new ArrayList<ApiIsolationPoint>();
        for (IsolationPoint isolationPoint : isolationPoints) {
            ApiIsolationPoint apiIsolationPoint = new ApiIsolationPoint();
            apiIsolationPoint.setActive(true);
            apiIsolationPoint.setModified(isolationPoint.getModified());
            apiIsolationPoint.setSid(isolationPoint.getId());
            apiIsolationPoint.setCheck(isolationPoint.getCheck());
            apiIsolationPoint.setDeviceDefinition(convertDefinition(isolationPoint.getDeviceDefinition()));
            apiIsolationPoint.setLockDefinition(convertDefinition(isolationPoint.getLockDefinition()));
            apiIsolationPoint.setSource(isolationPoint.getSourceType().name());
            apiIsolationPoint.setSourceText(isolationPoint.getSourceText());
            apiIsolationPoint.setIdentifier(isolationPoint.getIdentifier());
            apiIsolationPoint.setElectronicIdentifier(isolationPoint.getElectronicIdentifier());
            apiIsolationPoint.setAnnotation(convertAnnotation(isolationPoint.getAnnotation()));
            apiIsolationPoint.setLocation(isolationPoint.getLocation());
            apiIsolationPoint.setMethod(isolationPoint.getMethod());
			apiIsolationPoint.setFwdIdx(isolationPoint.getFwdIdx());
			apiIsolationPoint.setRevIdx(isolationPoint.getRevIdx());
            apiIsolationPoints.add(apiIsolationPoint);
        }
        return apiIsolationPoints;
    }

    private List<ApiProcedureDefinitionImage> convertImages(ProcedureDefinition definition, List<ProcedureDefinitionImage> images) {
        List<ApiProcedureDefinitionImage> convertedImages = new ArrayList<ApiProcedureDefinitionImage>();

        for (ProcedureDefinitionImage image : images) {
            ApiProcedureDefinitionImage convertedImage = new ApiProcedureDefinitionImage();
            convertedImage.setSid(image.getMobileGUID());
            convertedImage.setModified(image.getModified());
            convertedImage.setActive(true);
            convertedImage.setFileName(image.getFileName());
            convertedImage.setAnnotations(convertAnnotations(image.getAnnotations()));

            try {
                convertedImage.setData(s3Service.downloadProcedureDefinitionMediumImage(image));
            } catch (IOException e) {
                log.error("IOException downloading procedure def image: " + image.getId(), e);
            }

            convertedImages.add(convertedImage);
        }

        return convertedImages;
    }

    private List<ApiImageAnnotation> convertAnnotations(List<ImageAnnotation> annotations) {
        List<ApiImageAnnotation> convertedAnnotations = new ArrayList<ApiImageAnnotation>();
        for (ImageAnnotation annotation : annotations) {
            convertedAnnotations.add(convertAnnotation(annotation));
        }
        return convertedAnnotations;
    }

    private ApiImageAnnotation convertAnnotation(ImageAnnotation annotation) {
        if (annotation == null) {
            return null;
        }
        ApiImageAnnotation convertedAnnotation = new ApiImageAnnotation();
        convertedAnnotation.setSid(annotation.getId());
        convertedAnnotation.setModified(annotation.getModified());
        convertedAnnotation.setActive(true);
        convertedAnnotation.setAnnotationType(annotation.getType().name());
        convertedAnnotation.setX(annotation.getX());
        convertedAnnotation.setY(annotation.getY());
        convertedAnnotation.setImageId(annotation.getImage().getMobileGUID());
        return convertedAnnotation;
    }

    private ApiDeviceDescription convertDefinition(IsolationDeviceDescription deviceDefinition) {
        if (deviceDefinition == null) {
            return null;
        }
        ApiDeviceDescription apiDescription = new ApiDeviceDescription();
        apiDescription.setActive(true);
        apiDescription.setAssetTypeSid(deviceDefinition.getAssetType() == null ? null : deviceDefinition.getAssetType().getId());
        apiDescription.setAttributes(attrResource.convertInfoOptions(deviceDefinition.getAttributeValues()));
        apiDescription.setFreeformDescription(deviceDefinition.getFreeformDescription());
        apiDescription.setSid(deviceDefinition.getId());
        return apiDescription;
    }

    @Override
	public ListResponse<ApiProcedureDefinitionV2> findAll(
			@QueryParam("after") DateParam after,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("500") @QueryParam("pageSize") int pageSize) {

        return null;
	}

    @GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/")
	@Transactional(readOnly = true)
	public ListResponse<ApiProcedureDefinitionV2> FindForAsset(
            @PathParam("assetId") String assetId,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize) {
        QueryBuilder<ProcedureDefinition> builder = createUserSecurityBuilder(ProcedureDefinition.class);
        builder.addWhere(WhereClauseFactory.create(Comparator.EQ, "asset.mobileGUID", assetId));
        List<ApiProcedureDefinitionV2> procs = convertAllProcedureDefinitionsToApiModels(persistenceService.findAll(builder, page, pageSize));

        return new ListResponse<ApiProcedureDefinitionV2>(procs, page, pageSize, persistenceService.count(builder));
	}

    @GET
    @Path("/asset/{assetId}/procedures/test")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public String FindForAssetTest(
            //@PathParam("assetId") String assetId,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize) {
        QueryBuilder<ProcedureDefinition> builder = createUserSecurityBuilder(ProcedureDefinition.class);
        builder.addWhere(WhereClauseFactory.create(Comparator.EQ, "asset.mobileGUID", "0"));

        return builder.getQueryString();
	}

    protected List<ApiProcedureDefinitionV2> convertAllProcedureDefinitionsToApiModels(List<ProcedureDefinition> procedureDefinitions) {
        List<ApiProcedureDefinitionV2> apiProcedureDefinitions = new ArrayList<ApiProcedureDefinitionV2>();

        for(ProcedureDefinition pd : procedureDefinitions) {
            apiProcedureDefinitions.add(convertEntityToApiModel(pd));
        }

        return apiProcedureDefinitions;
    }

    /*@Override
    protected void addTermsToBuilder(QueryBuilder<ProcedureDefinition> builder) {
        builder.addSimpleWhere("publishedState", PublishedState.PUBLISHED);
    }*/

}

