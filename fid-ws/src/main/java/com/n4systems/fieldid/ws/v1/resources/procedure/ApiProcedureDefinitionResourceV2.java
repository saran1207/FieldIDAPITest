package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.EditableImage;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired private SvgGenerationService svgGenerationService;

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

            procDef = convertApiModelToEntity(apiProcDef, procDef, isNew);

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

        return new ListResponse<>(procs, page, pageSize, persistenceService.count(builder));
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

        return new ListResponse<>(apiProcs, 0, apiProcs.size(), apiProcs.size());
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
        return procedureDefinitions.stream().map(this::convertEntityToApiModel).collect(Collectors.toList());
    }

    /*@Override
    protected void addTermsToBuilder(QueryBuilder<ProcedureDefinition> builder) {
        builder.addSimpleWhere("publishedState", PublishedState.PUBLISHED);
    }*/

    //API Model to Entity Methods
    private ProcedureDefinition convertApiModelToEntity(ApiProcedureDefinitionV2 apiProcDef, ProcedureDefinition procDef, boolean isNew) throws ImageProcessingException, PersistenceException {
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

        //Technically, a situation like this should never happen, but it might be remotely possible that a user is
        //working on a brand new client that hasn't set their default annotation type yet.  In situations like this,
        //we'll just cleanly default to Arrow Style annotation types, which are the default.
	    procDef.setAnnotationType(procedureDefinitionService.getLotoSettings() == null ? AnnotationType.ARROW_STYLE : procedureDefinitionService.getLotoSettings().getAnnotationType());

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
        procDef.setTestingAndVerification(apiProcDef.getTestingAndVerification());

        //You can't directly set this one on our model... so we have to use it as a switch for the appropriate methods.
        if(apiProcDef.isActive()) {
            procDef.activateEntity();
        } else {
            procDef.archiveEntity();
        }

        //If this is a new ProcedureDefinition, there are a few other things we need to set.
        if(isNew) {
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

        //Stop, drop and dump it to the DB.  Before we continue, we're going to need the ID from the ProcDef... to get
        //that, we want to save first:
        procedureDefinitionService.saveProcedureDefinitionDraft(procDef);

        //Now we do the hard stuff:
        //1) Set the isolation points.
        procDef = convertToEntityIsolationPoints(procDef, apiProcDef, isNew);

        //2) Update Annotations.  You've already saved the images with the isolation points, but you now
        //   need to update those images with the Annotations from all of the isolation points... then
        //   you need to go back to those isolation points and update the referenced images with the updated
        procDef = updateAnnotationRelationships(procDef);

        return procDef;
    }

    /*
        Image Processing Beginning...
     */

    public ProcedureDefinition updateAnnotationRelationships(ProcedureDefinition procDef) {

        //At this point, we no longer need reference to the JSON Model... we're just reorganizing the Entity.
        List<ProcedureDefinitionImage> updatedImages = new ArrayList<>(procDef.getImages().size());
        for(ProcedureDefinitionImage image : procDef.getImages()) {
            //Wipe out previous annotations... we don't need 'em.
            image.setAnnotations(new ArrayList<>());

            try {
                //Add the new annotations.
                image.getAnnotations()
                        .addAll(procDef.getLockIsolationPoints()
                                .stream()
                                //Just because some Isolation Points have images, doesn't mean ALL Isolation Points do.
                                //Also, "Notes" Isolation Points don't have Annotations...
                                .filter(isolationPoint -> isolationPoint.getAnnotation() != null)
                                .map(IsolationPoint::getAnnotation)
                                .filter(annotation -> annotation.getImage().getMobileGUID().equals(image.getMobileGUID()))
                                .collect(Collectors.toList()));
            } catch (NullPointerException npe) {
                //This is actually OK.  It just means that there were no new Annotations to add.  If we don't catch
                //this error and continue on, the sky will fall, kittens will perish and zombies will rise.
                log.warn("There was a Null Pointer Exception when processing Annotations on ProcDef with ID " +
                         procDef.getId() + "... trying to carry on.");
            }

            //Add the image to the updated image list.
            updatedImages.add(image);
        }

        procDef.setImages(updatedImages);

        return procDef;
    }

    private ProcedureDefinitionImage createNewImage(ApiProcedureDefinitionImage image,
                                                    ProcedureDefinition procDef) throws ImageProcessingException {
        ProcedureDefinitionImage convertedImage = new ProcedureDefinitionImage();
        convertedImage.setMobileGUID(image.getSid());

        //Currently we don't handle anything to do with whether an image is active or not... skip that field.

        convertedImage.setFileName(image.getFileName());

        convertedImage.setTenant(getCurrentTenant());

        convertedImage.setProcedureDefinition(procDef);

        if(image.getData() == null) {
            throw new ImageProcessingException("There was no image present in what appears to be a new image with ID " + image.getSid());
        }

        convertedImage = s3Service.uploadTempProcedureDefImage(convertedImage, "image/jpeg", image.getData());

        if (convertedImage == null) {
            throw new ImageProcessingException("The image with SID '" + image.getSid() + "' failed to upload to S3!!!");
        }

        //Now we need to save the image for the application of the Annotations at a later stage.
        persistenceService.save(convertedImage);

        //We set the annotations later, since it's easier to manage their existence elsewhere and add them back to
        //the image after the fact...

        return convertedImage;
    }

    private ImageAnnotation createNewImageAnnotation(ApiImageAnnotation apiAnnotation, IsolationPointSourceType sourceType, String isolationPointIdentifier) {

        ImageAnnotation entityAnnotation = new ImageAnnotation();
		entityAnnotation.setType(resolveImageAnnotationTypeType(apiAnnotation.getAnnotationType(), sourceType));

        // Since these cannot be set by the mobile yet, we need to default them
        if (apiAnnotation.getX() == 0 && apiAnnotation.getY() == 0) {
            entityAnnotation.setX(0.5);
            entityAnnotation.setY(0.5);
        } else {
            entityAnnotation.setX(apiAnnotation.getX());
            entityAnnotation.setY(apiAnnotation.getY());
        }

        if (apiAnnotation.getX_tail() == 0 && apiAnnotation.getX_tail() == 0) {
            // Creates a horizontal line pointing to the center of the image
            entityAnnotation.setX_tail(0.1);
            entityAnnotation.setY_tail(0.5);
        } else {
            entityAnnotation.setX_tail(apiAnnotation.getX_tail());
            entityAnnotation.setY_tail(apiAnnotation.getY_tail());
        }

        entityAnnotation.setText(apiAnnotation.getText() != null ? apiAnnotation.getText() : isolationPointIdentifier);
        //I don't think we actually need to set this either.
//        originalAnnotation.setId(imageAnnotation.getSid());

        entityAnnotation.setTenant(getCurrentTenant());

        //We have to set the image later...
//        entityAnnotation.setImage(image);

        return entityAnnotation;
    }

    /*
        Image Processing End...
     */


    /*
        Isolation Point Processing Beginning...
     */

    public ProcedureDefinition convertToEntityIsolationPoints(ProcedureDefinition procDef,
                                                              ApiProcedureDefinitionV2 apiProcDef, boolean isNew)
                                                                        throws ImageProcessingException {

        //We're working with isolation points, so lets create a list of them now to access easily.
        List<ApiIsolationPoint> isolationPoints = apiProcDef.getIsolationPoints();

        //We'll need this map to attach the correct image to the correct annotation in the Isolation Points.
        Map<String, ProcedureDefinitionImage> imageMap =
                procDef.getImages()
                        .stream()
                        .collect(Collectors.toMap(ProcedureDefinitionImage::getMobileGUID, image -> image));

        //But having the existing images is only half of the battle.  We now need to find any images that don't
        //exist in the DB and add them as well.  This is done based on whether the Isolation Point is new or not.
        //We'll avoid doing work where possible.

        //As with Image Processing, there are two paths: New and Existing... if it's new, we can forgo a lot of the
        //processing that needs to take place for an existing Procedure Definition.  We're just converting from one
        //model to another.
        if(isNew) {
            //First, we're going to want to write all of our images and update the image map and Entity.
            for(ApiProcedureDefinitionImage image : apiProcDef.getImages()) {
                ProcedureDefinitionImage entityImage = createNewImage(image, procDef);
                imageMap.put(entityImage.getMobileGUID(), entityImage);
                procDef.addImage(entityImage);
            }

            //Next, we're going to add the isolation points.  These might
            isolationPoints.stream()
                           .map(isolationPoint ->
                                   createNewIsolationPoint(isolationPoint,
                                           imageMap.get(isolationPoint.getAnnotation() == null ? "nothing":isolationPoint.getAnnotation().getImageId())))
                           .forEach(procDef::copyIsolationPointForApi);
        } else {
            //First thing is first... we need to process these images and add any that aren't already in the DB.
            //To do that, we need to reference the map of existing images.  If we don't get an image back, that means we
            //need to add that image... so...
            List<ApiProcedureDefinitionImage> newImages =
                    apiProcDef.getImages()
                              .stream()
                              .filter(image -> imageMap.get(image.getSid()) == null)
                              .collect(Collectors.toList());

            for(ApiProcedureDefinitionImage image : newImages) {
                ProcedureDefinitionImage entityImage = createNewImage(image, procDef);
                imageMap.put(entityImage.getMobileGUID(), entityImage);
                procDef.addImage(entityImage);
            }

            //Our second step is to identify which Isolation Points (if any) no longer belong in the DB.  We do this by
            //compiling a list of the IPs still present in the provided JSON.
            List<Long> inboundIPs = isolationPoints.stream().map(ApiIsolationPoint::getSid).collect(Collectors.toList());

            //Before we go any further, we need another map containing the existing isolation points, indexed by their ID.
            Map<Long, IsolationPoint> entityIPMap = procDef.getLockIsolationPoints()
                    .stream()
                    .collect(Collectors.toMap(IsolationPoint::getId, isolationPoint -> isolationPoint));

            //Next, we compare the isolation points in the existing Procedure Definition with those that were passed in.
            //If they are no longer present in the provided JSON, we delete them from the database. We must also remove
            //the IsolationPoint from the DB, otherwise Hibernate gets grumpy.
            List<IsolationPoint> isolationPointList = procDef.getLockIsolationPoints();
            isolationPointList
                   .stream()
                   .filter(isolationPoint -> !inboundIPs.contains(isolationPoint.getId()))
                   .forEach(isolationPoint -> {
                       procDef.removeIsolationPointOnly(isolationPoint);
                       persistenceService.remove(isolationPoint);
                   });

            //Within this code, there are also two potentials for processing: We might have a new isolation point or we
            //might have an existing one that needs to be updated.  To simplify this processing, we split the provided
            //Isolation Points into two lists: those with IDs and those without... those without are simple conversions,
            //but those with will take a little more thinking to process.
            Map<Boolean, List<ApiIsolationPoint>> existingAndNotExistingIPs =
                    isolationPoints.stream().collect(Collectors.partitioningBy(isolationPoint -> isolationPoint.getSid() != null));

            //These ones are new... this is very easy.  We'll drop them directly into the isolation points on the procDef.
            existingAndNotExistingIPs.get(Boolean.FALSE)
                                     .stream()
                                     .map(isolationPoint ->
                                             createNewIsolationPoint(isolationPoint,
                                                                     imageMap.get(isolationPoint.getAnnotation() == null ? "nothing":isolationPoint.getAnnotation().getImageId())))
                                     .forEach(procDef::copyIsolationPointForApi);

            //These ones already existed... this is a little harder...  We'll definitely need that map of the existing
            //Isolation Points to make it easiest to pull the one we want to update.
            existingAndNotExistingIPs.get(Boolean.TRUE)
                                     .stream()
                                     .map(isolationPoint ->
                                             updateExistingIsolationPoint(isolationPoint,
                                                                          entityIPMap.get(isolationPoint.getSid()),
                                                                          imageMap.get(isolationPoint.getAnnotation() == null ? "nothing":isolationPoint.getAnnotation().getImageId())))
                                     .forEach(procDef::copyIsolationPointForApi);
        }

        return procDef;
    }

    private IsolationPoint updateExistingIsolationPoint(ApiIsolationPoint apiIsolationPoint,
                                                        IsolationPoint entityIsolationPoint,
                                                        ProcedureDefinitionImage image) {

        entityIsolationPoint.setTenant(getCurrentTenant());

        entityIsolationPoint.setCheck(apiIsolationPoint.getCheck());

        entityIsolationPoint.setSourceType(resolveSourceType(apiIsolationPoint.getSource(), apiIsolationPoint.getSourceText()));
        entityIsolationPoint.setSourceText(apiIsolationPoint.getSourceText());
        entityIsolationPoint.setIdentifier(apiIsolationPoint.getIdentifier());
        entityIsolationPoint.setElectronicIdentifier(apiIsolationPoint.getElectronicIdentifier());
        entityIsolationPoint.setLocation(apiIsolationPoint.getLocation());
        entityIsolationPoint.setMethod(apiIsolationPoint.getMethod());
        entityIsolationPoint.setFwdIdx(apiIsolationPoint.getFwdIdx());
        entityIsolationPoint.setRevIdx(apiIsolationPoint.getRevIdx());

        entityIsolationPoint.setDeviceDefinition(convertDefinition(apiIsolationPoint.getDeviceDefinition()));
        entityIsolationPoint.setLockDefinition(convertDefinition(apiIsolationPoint.getLockDefinition()));

        if(apiIsolationPoint.getAnnotation() == null) {
            entityIsolationPoint.setAnnotation(null);
        } else {
            ImageAnnotation imageAnnotation = createNewImageAnnotation(apiIsolationPoint.getAnnotation(), entityIsolationPoint.getSourceType(), entityIsolationPoint.getIdentifier());
            imageAnnotation.setImage(image);
            //Even here, we probably want to save to make sure it's updated... the DB may get angry if we try
            //to save changes in two places.
            persistenceService.save(imageAnnotation);
            entityIsolationPoint.setAnnotation(imageAnnotation);
        }

        return entityIsolationPoint;
    }

    private IsolationPoint createNewIsolationPoint(ApiIsolationPoint apiIsolationPoint, ProcedureDefinitionImage image) {

        IsolationPoint isoPoint = new IsolationPoint();

        isoPoint.setTenant(getCurrentTenant());

        isoPoint.setCheck(apiIsolationPoint.getCheck());
		isoPoint.setSourceType(resolveSourceType(apiIsolationPoint.getSource(), apiIsolationPoint.getSourceText()));
		isoPoint.setSourceText(apiIsolationPoint.getSourceText());
        isoPoint.setIdentifier(apiIsolationPoint.getIdentifier());
        isoPoint.setElectronicIdentifier(apiIsolationPoint.getElectronicIdentifier());
        isoPoint.setLocation(apiIsolationPoint.getLocation());
        isoPoint.setMethod(apiIsolationPoint.getMethod());
        isoPoint.setFwdIdx(apiIsolationPoint.getFwdIdx());
        isoPoint.setRevIdx(apiIsolationPoint.getRevIdx());

        isoPoint.setDeviceDefinition(convertDefinition(apiIsolationPoint.getDeviceDefinition()));
        isoPoint.setLockDefinition(convertDefinition(apiIsolationPoint.getLockDefinition()));

        if(apiIsolationPoint.getAnnotation() != null) {
            ImageAnnotation imageAnnotation = createNewImageAnnotation(apiIsolationPoint.getAnnotation(), isoPoint.getSourceType(), isoPoint.getIdentifier());
            imageAnnotation.setImage(image);
            //This is new... it definitely needs to be written to the DB before we do other things.  We want to save
            //these while we're processing the Isolation Points... otherwise we'd have to try to match them back to the
            //Isolation Points later WITHOUT having the ID from the DB to help us.
            persistenceService.save(imageAnnotation);
            isoPoint.setAnnotation(imageAnnotation);
        }

        return isoPoint;
    }

	/*
	WEB-5345: The following is a temporary hack to allow saving of Valve and Hydraulic source types
	from mobile version 1.6.0.

	The mobile bug is that the source field for Valve or Hydraulic source types is null.  In this case
		we will infer the source type from the sourceText which is a required, limited list and is
		unique for these two types.
	 */
	private IsolationPointSourceType resolveSourceType(String apiSourceType, String apiSourceText) {
		if (apiSourceType != null) {
			return IsolationPointSourceType.valueOf(apiSourceType);
		} else {
			if (apiSourceText.equals("Valve")) {
				return IsolationPointSourceType.V;
			} else if (apiSourceText.equals("Hydraulic")
					|| apiSourceText.equals("Hydraulic 600 PSI")
					|| apiSourceText.equals("Hydraulic 900 PSI")
					|| apiSourceText.equals("Hydraulic 1200 PSI")) {
				return IsolationPointSourceType.H;
			} else {
				throw new NullPointerException("source is null and sourceText is unknown: " + apiSourceText);
			}
		}
	}

	private ImageAnnotationType resolveImageAnnotationTypeType(String apiAnnotationType, IsolationPointSourceType sourceType) {
		if (apiAnnotationType != null) {
			return ImageAnnotationType.valueOf(apiAnnotationType);
		} else {
			// Names of the IsolationPointSourceType and ImageAnnotationType should always match
			return ImageAnnotationType.valueOf(sourceType.name());
		}
	}
	// WEB-5345: END

    /*
        Isolation Processing End...
     */

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

    private List<ApiProcedureDefinitionImage> convertImages(ProcedureDefinition definition) {
        List<ApiProcedureDefinitionImage> convertedImages = new ArrayList<ApiProcedureDefinitionImage>();

        List<IsolationPoint> isolationPoints = definition.getUnlockIsolationPoints();

        for(IsolationPoint point:isolationPoints) {
            if(point.getSourceType() != IsolationPointSourceType.N) {
                ApiProcedureDefinitionImage convertedImage = new ApiProcedureDefinitionImage();
                EditableImage image = point.getAnnotation().getImage();

                convertedImage.setSid(image.getMobileGUID());
                convertedImage.setModified(image.getModified());
                convertedImage.setActive(true);
                convertedImage.setFileName(Long.toString(point.getID()));
                convertedImage.setAnnotations(convertAnnotations(image.getAnnotations()));

                byte[] imageData;
                try {
                    imageData = s3Service.downloadProcedureDefinitionImageSvg(definition, image.getFileName(), point);

                    if (imageData == null) {
                        if (definition.getAnnotationType().equals(AnnotationType.CALL_OUT_STYLE)) {
                            svgGenerationService.generateAndUploadAnnotatedSvgs(definition);
                        } else {
                            svgGenerationService.generateAndUploadArrowStyleAnnotatedSvgs(definition);
                        }

                        imageData = s3Service.downloadProcedureDefinitionImageSvg(definition, image.getFileName(), point);

                        if (imageData == null) {
                            throw new Exception("Image does not exist...");
                        }
                    }

                    convertedImage.setData(imageData);
                } catch (IOException ioe) {
                    log.error("IOException downloading procedure def image: " + image.getId(), ioe);
                } catch (Exception e) {
                    log.error("There was a problem while Generating SVGs for ProcDef with ID " + definition.getId(), e);
                    e.printStackTrace();
                }

                convertedImages.add(convertedImage);
            }
        }
        return convertedImages;
    }

    private List<ApiImageAnnotation> convertAnnotations(List<ImageAnnotation> annotations) {
        List<ApiImageAnnotation> convertedAnnotations = new ArrayList<>();
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
        convertedAnnotation.setX_tail(annotation.getX_tail());
        convertedAnnotation.setY_tail(annotation.getY_tail());
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

        apiProcedureDef.setImages(convertImages(procedureDef));

        apiProcedureDef.setRejectedReason(procedureDef.getRejectedReason());

        apiProcedureDef.setEquipmentBuilding(procedureDef.getBuilding());
        apiProcedureDef.setApplicationProcess(procedureDef.getApplicationProcess());
        apiProcedureDef.setRemovalProcess(procedureDef.getRemovalProcess());
        apiProcedureDef.setTestingAndVerification(procedureDef.getTestingAndVerification());

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

