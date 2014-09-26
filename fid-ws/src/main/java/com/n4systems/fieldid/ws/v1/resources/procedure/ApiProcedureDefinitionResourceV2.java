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
    @Transactional
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

            //Make sure to write these as a DRAFT.  Mobile doesn't need/want the ability to actually publish.  These are
            //just saved in the field and then modified by someone in an office later before being published.
            procedureDefinitionService.saveProcedureDefinitionDraft(procDef);

            procDef = procedureDefinitionService.findProcedureDefinitionByMobileId(procDef.getMobileId());


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
        } catch(IsolationPointProcessingException ippe) {
            log.error("There was an error processing an Isolation Point in the provided JSON!  Error: " + ippe.getMessage(), ippe);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("There was an error processing an Isolation Point in the provided JSON!  Error: " + ippe.getMessage())
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

    /**
     * This method is called to delete ProcedureDefinitions from the database.  It should be noted that a user may only
     * delete ProcedureDefinitions in DRAFT status that they authored themselves.
     *
     * @param procDefSid - The MobileID of the ProcedureDefinition as a String.
     * @return A Response object containing only the Status of the request (204 for success, 400 or 409 for error).
     */
    @DELETE
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteDraftProcedureDefinition(@QueryParam("procDefSid") String procDefSid) {

        ProcedureDefinition deleteMe = procedureDefinitionService.findProcedureDefinitionByMobileId(procDefSid);

        Response.Status responseStatus;
        if(deleteMe != null) {
            if (deleteMe.getDevelopedBy().equals(getCurrentUser())) {
                procedureDefinitionService.deleteProcedureDefinition(deleteMe);
                responseStatus = Response.Status.NO_CONTENT;
            } else {
                responseStatus = Response.Status.CONFLICT;
            }
        } else {
            responseStatus = Response.Status.BAD_REQUEST;
        }

        //If there was no success, we return a 409, meaning that there was a conflict in the request.
        //If there WAS success, we return a 204, meaning that everything is OK, but there is no content being returned.
        return Response.status(responseStatus)
                       .build();
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
    private ProcedureDefinition convertApiModelToEntity(ApiProcedureDefinitionV2 apiProcDef, ProcedureDefinition procDef) throws ImageProcessingException, PersistenceException, IsolationPointProcessingException {
        //Don't forget, you're passing this object in, either full of information or null... point being, it's already
        //initialised.
        procDef.setMobileId(apiProcDef.getSid());

        //Transfer the easy fields from the ApiProcedureDefinitionV2 object.
        procDef.setCompleteIsolationPointInOrder(apiProcDef.isCompleteIsolationPointInOrder());
        procDef.setElectronicIdentifier(apiProcDef.getElectronicIdentifier());
        procDef.setEquipmentDescription(apiProcDef.getEquipmentDescription());
        procDef.setEquipmentNumber(apiProcDef.getEquipmentNumber());
        procDef.setRevisionNumber(apiProcDef.getRevisionNumber());
        procDef.setProcedureCode(apiProcDef.getProcedureCode());
        procDef.setWarnings(apiProcDef.getWarnings());

        //Ideally, the client should always be providing the ProcedureType, but this just prevents an error if they
        //happened to forget.
        procDef.setProcedureType(apiProcDef.getProcedureType() == null ? ProcedureType.SUB : ProcedureType.valueOf(apiProcDef.getProcedureType()));
        procDef.setEquipmentLocation(apiProcDef.getEquipmentLocation());
        procDef.setPublishedState(PublishedState.valueOf(apiProcDef.getStatus()));

        //Keep in mind if the user can't be found, it's going to fail silently and try to set "developedBy" as null.
        procDef.setDevelopedBy(userService.getUser(apiProcDef.getDevelopedBy()));

        procDef.setBuilding(apiProcDef.getEquipmentBuilding());
        procDef.setApplicationProcess(apiProcDef.getApplicationProcess());
        procDef.setRemovalProcess(apiProcDef.getRemovalProcess());

        //You can't directly set this one on our model... so we have to use it as a switch for the appropriate methods.
        if(apiProcDef.isActive()) {
            procDef.activateEntity();
        } else {
            procDef.archiveEntity();
        }

        //If this is a new ProcedureDefinition, there are a few other things we need to set.
        if(procDef.isNew()) {
            procDef.setTenant(getCurrentTenant());

            procDef.setDevelopedBy(userService.getUser(apiProcDef.getDevelopedBy()));

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


        //1) Set the images.
        procDef.setImages(convertToEntityImages(apiProcDef.getImages(), procDef));

        //2) Set the isolation points.
        procDef = convertToEntityIsolationPoints(procDef, apiProcDef.getIsolationPoints());


        return procDef;
    }

    private ProcedureDefinition convertToEntityIsolationPoints(ProcedureDefinition procDef, List<ApiIsolationPoint> isolationPoints) throws IsolationPointProcessingException {
        List<IsolationPoint> addUs = Lists.newArrayList();

        for(ApiIsolationPoint apiIsoPoint : isolationPoints) {
            //New Isolation Point
            if(apiIsoPoint.getSource() == null) {
                throw new IsolationPointProcessingException("Could not process JSON, Isolation Point Source Type ('source') was null!");
            }
            if(apiIsoPoint.getSid() == null) {
                IsolationPoint isoPoint = new IsolationPoint();

                isoPoint.setTenant(getCurrentTenant());

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

                if(apiIsoPoint.getAnnotation() != null) {
                    ProcedureDefinitionImage theImage = findMatchingImage(procDef.getImages(),
                            apiIsoPoint.getAnnotation().getImageId());

                    isoPoint.setAnnotation(createNewAnnotation(apiIsoPoint.getAnnotation(), theImage));
                }

                procDef.addIsolationPoint(isoPoint);
            } else {
                //Existing isoPoint, so we update only the fields that changed.
                for(IsolationPoint isoPoint : procDef.getIsolationPoints()) {
                    if(isoPoint.getId().equals(apiIsoPoint.getSid())) {
                        procDef.removeIsolationPoint(isoPoint);

                        isoPoint = updateEntityIsolationPoint(apiIsoPoint,
                                                              isoPoint,
                                                              procDef.getImages());

                        procDef.addIsolationPoint(isoPoint);
                    }
                }
            }
        }

        //While this is deprecated, we pretty much need to do this.  The new entities contain some of the old ones,
        //but there's no straight forward way to update existing items in the list.  As such, we need to make a NEW
        //list and use it to overwrite the old.
//        procDef.setIsolationPoints(addUs);

        return procDef;
    }

    private ProcedureDefinitionImage findMatchingImage(List<ProcedureDefinitionImage> images, String mobileGUID) {
        ProcedureDefinitionImage theImage = null;

        for(ProcedureDefinitionImage image : images) {
            if(mobileGUID != null &&
               mobileGUID.equals(image.getMobileGUID())) {

                theImage = image;
                break;
            }
        }

        return theImage;
    }

    private IsolationPoint updateEntityIsolationPoint(ApiIsolationPoint apiIsolationPoint,
                                                      IsolationPoint isolationPoint,
                                                      List<ProcedureDefinitionImage> images) {

        //Why are we setting the ID from the Client side when we already know it's right?!
//        isolationPoint.setId(apiIsolationPoint.getSid());
        isolationPoint.setSourceType(IsolationPointSourceType.valueOf(apiIsolationPoint.getSource()));
        isolationPoint.setSourceText(apiIsolationPoint.getSourceText());
        isolationPoint.setIdentifier(apiIsolationPoint.getIdentifier());
        isolationPoint.setElectronicIdentifier(apiIsolationPoint.getElectronicIdentifier());
        isolationPoint.setLocation(apiIsolationPoint.getLocation());
        isolationPoint.setMethod(apiIsolationPoint.getMethod());
        isolationPoint.setFwdIdx(apiIsolationPoint.getFwdIdx());
        isolationPoint.setRevIdx(apiIsolationPoint.getRevIdx());
        isolationPoint.setCheck(apiIsolationPoint.getCheck());
        isolationPoint.setDeviceDefinition(convertDefinition(apiIsolationPoint.getDeviceDefinition()));
        isolationPoint.setLockDefinition(convertDefinition(apiIsolationPoint.getLockDefinition()));
        ProcedureDefinitionImage theImage = null;
        if(images.size() > 0) {
            theImage = findMatchingImage(images, apiIsolationPoint.getAnnotation().getImageId());
        }


        isolationPoint.setAnnotation(convertApiAnnotation(apiIsolationPoint.getAnnotation(), isolationPoint.getAnnotation(), theImage));

        return isolationPoint;
    }

    private List<ProcedureDefinitionImage> convertToEntityImages(List<ApiProcedureDefinitionImage> images, ProcedureDefinition procDef) throws ImageProcessingException {
        List<ProcedureDefinitionImage> imageList = Lists.newArrayList();


        if(procDef.isNew()) {
            //1) If the ProcDef is NEW, we just convert the images from the API model to Entity.  They should only
            //   exist here... this is super easy.
            for(ApiProcedureDefinitionImage image : images) {
                ProcedureDefinitionImage convertedImage = createNewImage(image, procDef);

                imageList.add(convertedImage);
            }
        } else {
            //2) If the ProcDef is NOT NEW, we need to try and match the images from the old one to the new one.
            //  a) We need to find all images that match between the two and transfer over any properties to the Entity
            for(ApiProcedureDefinitionImage apiImage : images) {
                for(ProcedureDefinitionImage procImage : procDef.getImages()) {
                    if(apiImage.getSid() != null &&
                       apiImage.getSid().equals(procImage.getMobileGUID())) {

                        procImage.setMobileGUID(apiImage.getSid());

                        procImage.setFileName(apiImage.getFileName());

                        procImage.setTenant(getCurrentTenant());

                        procImage.setProcedureDefinition(procDef); //May not be necessary to do this... we're just updating.

                        procImage.setAnnotations(convertToEntityAnnotations(apiImage.getAnnotations(), procImage));

                        imageList.add(procImage);
                        break;
                    }
                }
            }


            //  b) We need to find all images that are in the API model, but not the Entity and add them.
            for(ApiProcedureDefinitionImage apiImage : images) {
                boolean notFound = true;

                for(ProcedureDefinitionImage procDefImage : procDef.getImages()) {
                    if(procDefImage.getMobileGUID().equals(apiImage.getSid())) {
                        notFound = false;
                        break;
                    }
                }

                if(notFound) {
                    imageList.add(createNewImage(apiImage, procDef));
                }
            }
        }

        return imageList;
    }

    private ProcedureDefinitionImage createNewImage(ApiProcedureDefinitionImage image,
                                                    ProcedureDefinition procDef) throws ImageProcessingException {
        ProcedureDefinitionImage convertedImage = new ProcedureDefinitionImage();
        convertedImage.setMobileGUID(image.getSid());

        //Currently we don't handle anything to do with whether an image is active or not... skip that field.

        convertedImage.setFileName(image.getFileName());

        convertedImage.setTenant(getCurrentTenant());

        convertedImage.setProcedureDefinition(procDef);


        convertedImage = s3Service.uploadTempProcedureDefImage(convertedImage, "image/jpeg", image.getData());

        if (convertedImage == null) {
            throw new ImageProcessingException("The image with SID '" + image.getSid() + "' failed to upload to S3!!!");
        }

        convertedImage.setAnnotations(convertToEntityAnnotations(image.getAnnotations(), convertedImage));

        return convertedImage;
    }

    private List<ImageAnnotation> convertToEntityAnnotations(List<ApiImageAnnotation> annotations, ProcedureDefinitionImage image) {
        List<ImageAnnotation> convertedAnnotations = Lists.newArrayList();

        for(ApiImageAnnotation imageAnnotation : annotations) {
            for(ImageAnnotation originalAnnotation : image.getAnnotations()) {
                if(imageAnnotation.getSid().equals(originalAnnotation.getId())) {
                    convertedAnnotations.add(convertApiAnnotation(imageAnnotation, originalAnnotation, image));
                }
            }
//            convertedAnnotations.add(convertAnnotation(imageAnnotation, image));
        }

        return convertedAnnotations;
    }

    private ImageAnnotation convertApiAnnotation(ApiImageAnnotation imageAnnotation,
                                                 ImageAnnotation originalAnnotation,
                                                 ProcedureDefinitionImage image) {
        if(imageAnnotation == null) {
            return originalAnnotation;
        }

        originalAnnotation.setType(ImageAnnotationType.valueOf(imageAnnotation.getAnnotationType()));
        originalAnnotation.setX(imageAnnotation.getX());
        originalAnnotation.setY(imageAnnotation.getY());
        //I don't think we actually need to set this either.
//        originalAnnotation.setId(imageAnnotation.getSid());

        originalAnnotation.setTenant(getCurrentTenant());

        originalAnnotation.setImage(image);

        return originalAnnotation;
    }

    private ImageAnnotation createNewAnnotation(ApiImageAnnotation annotation, ProcedureDefinitionImage image) {
        if(annotation == null) {
            return null;
        }

        ImageAnnotation convertedAnnotation = new ImageAnnotation();

        //NOTE: We no longer care about this, because this is CLEARLY new and thus doesn't get an ID.
//        convertedAnnotation.setId(annotation.getSid());

        convertedAnnotation.setType(ImageAnnotationType.valueOf(annotation.getAnnotationType()));
        convertedAnnotation.setX(annotation.getX());
        convertedAnnotation.setY(annotation.getY());

        convertedAnnotation.setTenant(getCurrentTenant());

        convertedAnnotation.setImage(image);

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

        //This is irrelevant, since our saving process effectively invalidates these IDs.
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
        apiProcedureDef.setEquipmentLocation(procedureDef.getEquipmentLocation());
        apiProcedureDef.setProcedureType(procedureDef.getProcedureType() == null ? null : procedureDef.getProcedureType().getName());
        apiProcedureDef.setDevelopedBy(procedureDef.getDevelopedBy() == null ? null : procedureDef.getDevelopedBy().getId());
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
        apiProcedureDef.setStatus(procedureDef.getPublishedState().getName());
        apiProcedureDef.setImages(convertImages(procedureDef, procedureDef.getImages()));
        apiProcedureDef.setRejectedReason(procedureDef.getRejectedReason());

        apiProcedureDef.setEquipmentBuilding(procedureDef.getBuilding());
        apiProcedureDef.setApplicationProcess(procedureDef.getApplicationProcess());
        apiProcedureDef.setRemovalProcess(procedureDef.getRemovalProcess());

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

    private class IsolationPointProcessingException extends Exception {
        public IsolationPointProcessingException(String message) {
            super(message);
        }
    }
}

