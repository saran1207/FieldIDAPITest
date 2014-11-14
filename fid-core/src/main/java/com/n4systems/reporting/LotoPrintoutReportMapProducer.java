package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.reporting.data.ImagePrintoutContainer;
import com.n4systems.reporting.data.IsolationPointPrintoutContainer;
import com.n4systems.util.DateTimeDefinition;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is the report map producer for the new LOTO Printouts.
 *
 * Created by Jordan Heath on 2014-10-23.
 */
public class LotoPrintoutReportMapProducer extends ReportMapProducer {

    private final ProcedureDefinition procDef;
    private final String reportTitle;

    private SvgGenerationService svgGenerationService;

    private static final Logger logger = Logger.getLogger(ReportMapProducer.class);

    public LotoPrintoutReportMapProducer(String reportTitle, ProcedureDefinition procDef, DateTimeDefinition dateTimeDefinition, S3Service s3Service, SvgGenerationService svgGenerationService) {
        super(dateTimeDefinition, s3Service);
        this.procDef = procDef;
        this.reportTitle = reportTitle;
        this.svgGenerationService = svgGenerationService;
    }

    /**
     * This method produces the necessary map values for Jasper Reports to generate a LOTO Printout (either Longform or
     * Shortform, this method supplies values for both).
     */
    @Override
    protected void addParameters() {
        add("reportTitle", reportTitle);
        add("procedureCode", procDef.getProcedureCode());
        add("equipmentDescription", procDef.getEquipmentDescription());
        add("equipmentNumber", procDef.getEquipmentNumber());
        add("equipmentLocation", procDef.getEquipmentLocation());
        add("building", procDef.getBuilding());
        add("warnings", procDef.getWarnings());
        add("applicationProcess", procDef.getApplicationProcess());
        add("removalProcess", procDef.getRemovalProcess());
        add("testingAndVerification", procDef.getTestingAndVerification());
        add("currentDate", formatDate(new Date(System.currentTimeMillis()), false));
        add("userPosition", procDef.getApprovedBy() != null ? procDef.getApprovedBy().getPosition() : procDef.getRejectedBy() != null ? procDef.getRejectedBy().getPosition() : "");

        //If it's null, that SHOULD mean that there was no previous ProcDef.  At least not one that was published...
        if(procDef.getRevisionNumber().intValue() > 1) {
            add("revisedBy", procDef.getCreatedBy().getDisplayName());
            add("revisedByDate", formatDate(procDef.getCreated(), false));
        } else {
            add("revisedBy", "");
            add("revisedByDate", "");
        }

        add("reviewedBy", procDef.getApprovedBy() != null ? procDef.getApprovedBy().getDisplayName() : procDef.getRejectedBy() != null ? procDef.getRejectedBy().getDisplayName() : "");
        add("developedBy", procDef.getDevelopedBy() != null ? procDef.getDevelopedBy().getDisplayName() : "");
        add("reviewedByDate", procDef.getPublishedState().equals(PublishedState.PUBLISHED) ? formatDate(procDef.getModified(), false) : "");
        add("developedByDate", procDef.getCreated() != null ? formatDate(procDef.getCreated(), false) : "");
        add("revisionNumber", procDef.getRevisionNumber().toString());
        add("inDraft", procDef.getPublishedState().equals(PublishedState.DRAFT));

        try {
            add("fieldIdLogo", s3Service.downloadLotoLogo());
        } catch (IOException e) {
            logger.error("There was an exception while attempting to download the static footer Logo for a LOTO Printout!!", e);
        }

        add("logoImage", getCustomerLogo(procDef.getOwner().getPrimaryOrg()));

        //If it's not long, it's short... or invalid... but we'll pretend that being invalid is impossible.
        List<IsolationPointPrintoutContainer> isolationPoints = convertToIPContainerCollection(procDef.getLockIsolationPoints());
        add("isolationPoints", isolationPoints);

        //Now, we have to do the images...  these are special images that hold all annotations associated with the
        //single image.
        List<ImagePrintoutContainer> allImages = convertToImageContainerCollection(procDef.getImages());

        add("allImages", allImages);
    }

    /**
     * This method converts a <b>List</b> of <b>ProcedureDefinitionImage</b> JPA entities into a <b>List</b> of
     * <b>ImageShortPrintoutContainer</b> POJOs for use by Jasper.
     *
     * @param images - A <b>List</b> populated with <b>ProcedureDefinitionImage</b> JPA entities.
     * @return A <b>List</b> populated with <b>ImageShortPrintoutContainer</b> POJOs for use by Jasper.
     */
    private List<ImagePrintoutContainer> convertToImageContainerCollection(List<ProcedureDefinitionImage> images) {
        return images.stream()
                     .map(this::convertToImageContainer)
                     .filter(container -> container.getImage() != null)
                     .collect(Collectors.toList());
    }

    /**
     * This method converts <b>ProcedureDefinitionImage</b> JPA Entities into <b>ImageShortPrintoutContainer</b> POJOs
     * for use by Jasper.  This eliminates much of the extra - and unneeded - data attached to these entities and
     * provides Jasper with a cleaner set of data to work with.
     *
     * This "conversion" is done by way of downloading the associated SVG image from S3.  Most of the leg work is done
     * by <b>S3Service.downloadProcedureDefinitionImageSvg(...)</b> to retrieve the images and
     * <b>S3Service.generateAndUploadAnnotatedSvgs(...)</b> to generate images when they do not exist in S3.
     *
     * @param image - A <b>ProcedureDefinitionImage</b> JPA Entity to be converted for Jasper.
     * @return An <b>ImageShortPrintoutContainer</b> POJO for use by Jasper.
     */
    private ImagePrintoutContainer convertToImageContainer(ProcedureDefinitionImage image) {
        ImagePrintoutContainer returnMe = new ImagePrintoutContainer();

        try {
            byte[] imageData = s3Service.downloadProcedureDefinitionImageSvg(image);

            if(imageData == null) {
                //This might just mean that we haven't generated the SVGs yet... so we'll do that now.
                try {
                    svgGenerationService.generateAndUploadAnnotatedSvgs(procDef);

                    //Now we try to pull the SVG down again...
                    imageData = s3Service.downloadProcedureDefinitionImageSvg(image);

                    //If it's still null, then we need to throw an exception of some sort.
                    if(imageData == null) throw new Exception("Image didn't exist...");

                    returnMe.setImage(new ByteArrayInputStream(imageData));
                } catch (IOException ioe) {
                    logger.warn("There was an IOException while trying to download the ProcedureDefinitionImage with ID " + image.getId(), ioe);
                } catch (Exception e) {
                    logger.error("There was a problem while Generating SVGs for ProcDef with ID " + procDef.getId(), e);
                    e.printStackTrace();
                }
            } else {
                //If we succeeded the first time, then we just add the image.
                returnMe.setImage(new ByteArrayInputStream(imageData));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("There was an error downloading an SVG image for the LOTO Shortform Printout for Image with ID " + image.getId() +  "!!", e);
            returnMe.setImage(null);
        }

        return returnMe;
    }

    /**
     * This method uses Streams and the convertToIPContainer method to re-map the collection into a series of
     * POJOs for inclusion in the report.  Like above, this ensures we don't send unneeded information to Jasper.
     *
     * @param isolationPoints - A <b>List</b> populated with all <b>IsolationPoint</b>s from the definition.
     * @return A <b>List</b> populated with <b>IsolationPointPrintoutContainer</b>s, representing the provided Isolation Points.
     */
    private List<IsolationPointPrintoutContainer> convertToIPContainerCollection(List<IsolationPoint> isolationPoints) {
        return isolationPoints.stream()
                              .map(this::convertToIPContainer)
                              .collect(Collectors.toList());
    }

    /**
     * This method converts a single <b>IsolationPoint</b> into a <b>IsolationPointPrintoutContainer</b> for use by
     * Jasper in generating the report.
     *
     * @param isolationPoint - An <b>IsolationPoint</b> JPA Entity that needs to be converted.
     * @return The resulting <b>IsolationPointPrintoutContainer</b> POJO for use by Jasper.
     */
    private IsolationPointPrintoutContainer convertToIPContainer(IsolationPoint isolationPoint) {
        IsolationPointPrintoutContainer container = new IsolationPointPrintoutContainer();

        container.setCheck(isolationPoint.getCheck());
        container.setDevice(isolationPoint.getDeviceDefinition().getAssetType() == null ? isolationPoint.getDeviceDefinition().getFreeformDescription() : isolationPoint.getDeviceDefinition().getAssetType().getDisplayName());
        container.setLockoutMethod(isolationPoint.getMethod());
        container.setSourceId(isolationPoint.getIdentifier());
        container.setSourceLocation(isolationPoint.getLocation());
        container.setEnergyType(isolationPoint.getSourceType().name());
        container.setEnergySource(isolationPoint.getSourceText());

        //Find the relevant image.  We do a bit of cheating, but should receive back an Optional that will either
        //contain the image or null.
        Optional<ProcedureDefinitionImage> optionalImage =
                procDef.getImages()
                        .stream()
                        //The trick here is to filter by a value that should result in either 0 or 1 elements.
                        .filter(image -> image.getId().equals(isolationPoint.getAnnotation().getImage().getId()))
                        .findAny();

        //Zero elements, you say?
        //Well, yeah... images are optional.  This is how we handle that:
        try {
            ProcedureDefinitionImage theImage = optionalImage.get();
            if(theImage == null) throw new NoSuchElementException("The image was empty... that's still bad news.");
            byte[] imageData = s3Service.downloadProcedureDefinitionImageSvg(theImage, isolationPoint);
            if(imageData == null) {
                //It's possible this image was only null because the SVG doesn't exist yet... so we're going to try
                //to fix that... then we're going to try again.  If it's still bad the second time, we're going to throw
                //an exception.
                try {
                    svgGenerationService.generateAndUploadAnnotatedSvgs(procDef);

                    imageData = s3Service.downloadProcedureDefinitionImageSvg(theImage, isolationPoint);

                    if(imageData == null) throw new Exception("Image doesn't exist...");

                    container.setImage(new ByteArrayInputStream(imageData));
                } catch (IOException ioe) {
                    logger.warn("There was an IOException while trying to download the image associated with the Isolation Point with ID " + isolationPoint.getId(), ioe);
                } catch (Exception e) {
                    logger.error("There was a problem while Generating SVGs for ProcDef with ID " + procDef.getId(), e);
                    e.printStackTrace();
                }
            } else {
                container.setImage(new ByteArrayInputStream(imageData));
            }
        } catch(NoSuchElementException nsee) {
            //Aside from a console warning, we don't do anything else... this console warning may even be too much...
            //but I'd rather have some record of this.
            logger.warn("No image found for Isolation Point with ID: " + isolationPoint.getId());
        } catch(IOException ioe) {
            //Again, we'll just warn and then continue without the image.  This can all be changed if images are super
            //important.
            logger.warn("There was an IOException while trying to download the image associated with the Isolation Point with ID " + isolationPoint.getId(), ioe);
        }

        //Now we're left with null if there's no image, or the image data if there IS image data.

        return container;
    }
}
