package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.*;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Path("procedureDefinitionV2")
public class ApiProcedureDefinitionResourceV2 extends ApiResource<ApiProcedureDefinitionV2, ProcedureDefinition> {

    private static final Logger log = Logger.getLogger(ApiProcedureDefinitionResource.class);

    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private AssetService assetService;
    @Autowired private AssetTypeService assetTypeService;
    @Autowired private UserService userService;
    @Autowired private ApiAttributeValueResource attrResource;
    @Autowired private S3Service s3Service;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response writeOrUpdateProcedureDefinition(ApiProcedureDefinitionV2 apiProcDef) {
        boolean isNew = false;

        try {
            //First thing we should do is see if this new Procedure Definition exists.  If it exists, then we're golden.
            //We're just going to be updating values into an already existing entity.  Easy, peasy.
            ProcedureDefinition procDef = procedureDefinitionService.findProcedureDefinitionByMobileId(apiProcDef.getSid());

            if (procDef == null) {
                procDef = new ProcedureDefinition();
                isNew = true;
            }

            procDef = convertApiModelToEntity(apiProcDef, procDef);

            //If we've made it this far, we can now attempt to save the ProcedureDefinition.
            procedureDefinitionService.saveProcedureDefinition(procDef);

            apiProcDef = convertEntityToApiModel(procDef);

            return Response.status(isNew ? Response.Status.CREATED : Response.Status.OK)
                    .entity(apiProcDef)
                    .build();

        } catch (ImageProcessingException ipe) {
            log.error("There was an error when attempting to process images in the ProcedureDefinition.  Error: " + ipe.getMessage(), ipe);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("There was an error when attempting to process images in the ProcedureDefinition.  Error: " + ipe.getMessage())
                           .build();
        } catch (PersistenceException pe) {
            log.error("There was an error retrieving data related to the ProcedureDefinition.  Error: " + pe.getMessage(), pe);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("There was an error retrieving data related to the ProcedureDefinition.  Error: " + pe.getMessage())
                           .build();
        } catch (Exception e) {
            log.error("Unexpected error!!!  Error: " + e.getMessage(), e);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Unexpected error!!!  Error: " + e.getMessage())
                           .build();
        }
    }

    @GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/")
	@Transactional(readOnly = true)
	public ListResponse<ApiProcedureDefinitionV2> findForAsset(
            @PathParam("assetId") String assetId,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize) {
        QueryBuilder<ProcedureDefinition> builder = createUserSecurityBuilder(ProcedureDefinition.class);
        builder.addWhere(WhereClauseFactory.create(Comparator.EQ, "asset.mobileGUID", assetId));
        List<ApiProcedureDefinitionV2> procs = convertAllProcedureDefinitionsToApiModels(persistenceService.findAll(builder, page, pageSize));

        return new ListResponse<ApiProcedureDefinitionV2>(procs, page, pageSize, persistenceService.count(builder));
	}

    @GET
    @Path("list")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true)
    public ListResponse<ApiProcedureDefinitionV2> findAll(
            @QueryParam("id") List<String> assetIds) {
        QueryBuilder<ProcedureDefinition> builder = createUserSecurityBuilder(ProcedureDefinition.class);
        builder.addWhere(WhereClauseFactory.create(Comparator.IN, "asset.mobileGUID", assetIds));

        List<ProcedureDefinition> procs = persistenceService.findAll(builder);
        List<ApiProcedureDefinitionV2> apiProcs = convertAllProcedureDefinitionsToApiModels(procs);

        return new ListResponse<ApiProcedureDefinitionV2>(apiProcs, 0, apiProcs.size(), apiProcs.size());
    }

    @GET
    @Path("/asset/{assetId}/procedures/test")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public String FindForAssetTest(
            @PathParam("assetId") String assetId,
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

    //API Model to Entity Methods
    private ProcedureDefinition convertApiModelToEntity(ApiProcedureDefinitionV2 apiProcDef, ProcedureDefinition procDef) throws ImageProcessingException, PersistenceException {
        //Don't forget, you're passing this object in, either full of information or null... point being, it's already
        //initialised.
        procDef.setMobileId(apiProcDef.getSid());
        procDef.setModified(apiProcDef.getModified());
        procDef.setModifiedBy(getCurrentUser());

        //Transfer the easy fields from the ApiProcedureDefinitionV2 object.
        procDef.setCompleteIsolationPointInOrder(apiProcDef.isCompleteIsolationPointInOrder());
        procDef.setElectronicIdentifier(apiProcDef.getElectronicIdentifier());
        procDef.setEquipmentDescription(apiProcDef.getEquipmentDescription());
        procDef.setEquipmentNumber(apiProcDef.getEquipmentNumber());
        procDef.setRevisionNumber(apiProcDef.getRevisionNumber());
        procDef.setProcedureCode(apiProcDef.getProcedureCode());
        procDef.setWarnings(apiProcDef.getWarnings());

        //You can't directly set this one on our model... so we have to use it as a switch for the appropriate methods.
        if(apiProcDef.isActive()) {
            procDef.activateEntity();
        } else {
            procDef.archiveEntity();
        }

        //If this is a new ProcedureDefinition, there are a few other things we need to set.
        if(procDef.isNew()) {
            procDef.setCreatedBy(getCurrentUser());
            procDef.setTenant(getCurrentTenant());

            //Pretty straight forward... if you made it, you developed it.
            procDef.setDevelopedBy(getCurrentUser());

            //We start off saving it in draft state.  From there, it automatically figures out how to do things
            //regarding whether or not the user can publish or an approver has to publish.
            procDef.setPublishedState(PublishedState.DRAFT);

            //Read the associated Asset from the DB.
            Asset asset = assetService.findByMobileId(apiProcDef.getAssetId());
            if(asset == null) {
                //If the Asset couldn't be located, kick off an exception.  We're done here, because the
                //ProcedureDefinition is invalid.
                throw new PersistenceException("Associated Asset (Mobile ID: " + apiProcDef.getAssetId() + ") not found!  Cannot save ProcedureDefinition with non-existing Asset!");
            }

            //Now set the asset.
            procDef.setAsset(asset);
        }

        //Now we do the hard stuff:
        //1) Set the isolation points.
        procDef = convertToEntityIsolationPoints(procDef, apiProcDef.getIsolationPoints());

        //2) Set the images.
        procDef.setImages(convertToEntityImages(apiProcDef.getImages()));

        return procDef;
    }

    private ProcedureDefinition convertToEntityIsolationPoints(ProcedureDefinition procDef, List<ApiIsolationPoint> isolationPoints) {
        List<IsolationPoint> addUs = Lists.newArrayList();

        for(ApiIsolationPoint apiIsoPoint : isolationPoints) {
            //New Isolation Point
            if(apiIsoPoint.getSid() == null) {
                IsolationPoint isoPoint = new IsolationPoint();

                isoPoint.setTenant(getCurrentTenant());
                isoPoint.setModified(apiIsoPoint.getModified());
                isoPoint.setModifiedBy(getCurrentUser());
                isoPoint.setCreatedBy(getCurrentUser());
                isoPoint.setCreated(new Date(System.currentTimeMillis()));

                isoPoint.setId(apiIsoPoint.getSid());
                isoPoint.setCheck(apiIsoPoint.getCheck());

                isoPoint.setSourceType(IsolationPointSourceType.valueOf(apiIsoPoint.getSource()));
                isoPoint.setSourceText(apiIsoPoint.getSourceText());
                isoPoint.setIdentifier(apiIsoPoint.getIdentifier());
                isoPoint.setElectronicIdentifier(apiIsoPoint.getElectronicIdentifier());
                isoPoint.setLocation(apiIsoPoint.getLocation());
                isoPoint.setMethod(apiIsoPoint.getMethod());
                isoPoint.setFwdIdx(apiIsoPoint.getFwdIdx());
                isoPoint.setRevIdx(apiIsoPoint.getRevIdx());

                isoPoint.setDeviceDefinition(convertDefinition(apiIsoPoint.getDeviceDefinition()));
                isoPoint.setLockDefinition(convertDefinition(apiIsoPoint.getLockDefinition()));

                isoPoint.setAnnotation(convertAnnotation(apiIsoPoint.getAnnotation()));

                addUs.add(isoPoint);
            } else {
                //Existing isoPoint, so we update only the fields that changed.
                for(IsolationPoint isoPoint : procDef.getLockIsolationPoints()) {
                    if(isoPoint.getId().equals(apiIsoPoint.getSid())) {
                        isoPoint = updateEntityIsolationPoint(apiIsoPoint, isoPoint);

                        addUs.add(isoPoint);
                    }
                }
            }
        }

        //While this is deprecated, we pretty much need to do this.  The new entities contain some of the old ones,
        //but there's no straight forward way to update existing items in the list.  As such, we need to make a NEW
        //list and use it to overwrite the old.
        procDef.setIsolationPoints(addUs);

        return procDef;
    }

    private IsolationPoint updateEntityIsolationPoint(ApiIsolationPoint apiIsolationPoint, IsolationPoint isolationPoint) {
        isolationPoint.setModifiedBy(getCurrentUser());
        isolationPoint.setModified(new Date(System.currentTimeMillis()));

        isolationPoint.setSourceType(IsolationPointSourceType.valueOf(apiIsolationPoint.getSource()));
        isolationPoint.setSourceText(apiIsolationPoint.getSourceText());
        isolationPoint.setIdentifier(apiIsolationPoint.getIdentifier());
        isolationPoint.setElectronicIdentifier(apiIsolationPoint.getElectronicIdentifier());
        isolationPoint.setLocation(apiIsolationPoint.getLocation());
        isolationPoint.setMethod(apiIsolationPoint.getMethod());
        isolationPoint.setFwdIdx(apiIsolationPoint.getFwdIdx());
        isolationPoint.setRevIdx(apiIsolationPoint.getRevIdx());
        isolationPoint.setCheck(apiIsolationPoint.getCheck());

        //We'll just blatantly update these, regardless of changes or not.  Otherwise there's way more checking than is
        //logical.
        isolationPoint.setDeviceDefinition(convertDefinition(apiIsolationPoint.getDeviceDefinition()));
        isolationPoint.setLockDefinition(convertDefinition(apiIsolationPoint.getLockDefinition()));

        isolationPoint.setAnnotation(convertAnnotation(apiIsolationPoint.getAnnotation()));

        return isolationPoint;
    }

    private List<ProcedureDefinitionImage> convertToEntityImages(List<ApiProcedureDefinitionImage> images) throws ImageProcessingException {
        List<ProcedureDefinitionImage> imageList = Lists.newArrayList();

        for(ApiProcedureDefinitionImage image : images) {
            ProcedureDefinitionImage convertedImage = new ProcedureDefinitionImage();
            convertedImage.setMobileGUID(image.getSid());
            convertedImage.setModified(image.getModified());
            convertedImage.setModifiedBy(getCurrentUser());

            if(convertedImage.isNew()) {
                convertedImage.setCreatedBy(getCurrentUser());
            }

            //Currently we don't handle anything to do with whether an image is active or not... skip that field.

            convertedImage.setFileName(image.getFileName());
            convertedImage.setAnnotations(convertToEntityAnnotations(image.getAnnotations()));

            convertedImage.setTenant(getCurrentTenant());

            //If the image is new, we want to upload it... if it isn't, then we shouldn't need to.
            //Caveat being that we're expecting the image not to have changed...
            if(convertedImage.isNew()) {
                convertedImage = s3Service.uploadTempProcedureDefImage(convertedImage, "image/jpeg", image.getData());

                if (convertedImage == null) {
                    throw new ImageProcessingException("The image with SID '" + image.getSid() + "' failed to upload to S3!!!");
                }
            }

            imageList.add(convertedImage);
        }

        return imageList;
    }

    private List<ImageAnnotation> convertToEntityAnnotations(List<ApiImageAnnotation> annotations) {
        List<ImageAnnotation> convertedAnnotations = Lists.newArrayList();

        for(ApiImageAnnotation imageAnnotation : annotations) {
            convertedAnnotations.add(convertAnnotation(imageAnnotation));
        }

        return convertedAnnotations;
    }

    private ImageAnnotation convertAnnotation(ApiImageAnnotation annotation) {
        if(annotation == null) {
            return null;
        }

        ImageAnnotation convertedAnnotation = new ImageAnnotation();

        //This may not be a good idea, since new items should not have an ID set by the mobile
        //side, unless it has already written the isolation points to the DB and retrieved an ID.
        //These come from the database.  NOTE: Kirill says not to worry about this one.
        convertedAnnotation.setId(annotation.getSid());
        convertedAnnotation.setModified(annotation.getModified());

        //This could cause problems if the values on the Client side aren't the same as those on the Server side.
        convertedAnnotation.setType(ImageAnnotationType.valueOf(annotation.getAnnotationType()));
        convertedAnnotation.setX(annotation.getX());
        convertedAnnotation.setY(annotation.getY());

        convertedAnnotation.setTenant(getCurrentTenant());
        convertedAnnotation.setModifiedBy(getCurrentUser());

        if(convertedAnnotation.isNew()) {
            convertedAnnotation.setCreatedBy(getCurrentUser());
        }

        return convertedAnnotation;
    }

    private IsolationDeviceDescription convertDefinition(ApiDeviceDescription deviceDefinition) {
        if(deviceDefinition == null) {
            return null;
        }

        IsolationDeviceDescription description = new IsolationDeviceDescription();
        description.setAssetType(deviceDefinition.getAssetTypeSid() == null ? null : assetTypeService.getAssetType(deviceDefinition.getAssetTypeSid()));

        description.setAttributeValues(new ArrayList(attrResource.convertAttributeValues(deviceDefinition.getAttributes(), null)));

        description.setFreeformDescription(deviceDefinition.getFreeformDescription());

        description.setId(deviceDefinition.getSid());

        return description;
    }

    //Entity to API Methods
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

    //These internal exceptions are kept here, because they're ONLY of use here.  We use these to track where
    //exceptions occur in the service, so that we can provide a more detailed response to the client, rather
    //than simply, "Didn't work.  Tough bacon."
    private class ImageProcessingException extends Exception {
        public ImageProcessingException(String message) {
            super(message);
        }
    }

    private class PersistenceException extends Exception {
        public PersistenceException(String message) {
            super(message);
        }
    }
}

