package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.reporting.data.ImageShortPrintoutContainer;
import com.n4systems.reporting.data.IsolationPointLongPrintoutContainer;
import com.n4systems.reporting.data.IsolationPointShortPrintoutContainer;
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

    public enum PrintoutType {
        LONG(), SHORT();

        public String getName() {
            return name();
        }
    }

    private final ProcedureDefinition procDef;
    private final String reportTitle;
    private final PrintoutType printoutType;

    private SvgGenerationService svgGenerationService;

    private static final Logger logger = Logger.getLogger(ReportMapProducer.class);

    public LotoPrintoutReportMapProducer(String reportTitle, ProcedureDefinition procDef, PrintoutType printoutType, DateTimeDefinition dateTimeDefinition, S3Service s3Service, SvgGenerationService svgGenerationService) {
        super(dateTimeDefinition, s3Service);
        this.procDef = procDef;
        this.reportTitle = reportTitle;
        this.printoutType = printoutType;
        this.svgGenerationService = svgGenerationService;
    }

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

        //If it's null, that SHOULD mean that there was no previous ProcDef.  At least not one that was published...
        //Not 100% confident that this will work.  At least not in all cases.
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

        if(printoutType.equals(PrintoutType.LONG)) {
            //Long form has this field that doesn't exist in shortForm... but for the most part, it's not useful.
            add("userPosition", procDef.getApprovedBy() != null ? procDef.getApprovedBy().getPosition() : procDef.getRejectedBy() != null ? procDef.getRejectedBy().getPosition() : "");

            List<IsolationPointLongPrintoutContainer> isolationPoints = convertToLongIPContainerCollection(procDef.getLockIsolationPoints());
            //Not really too sure we need to send in both... I'm pretty sure we can just send a list right in and
            //build the JRBeanCollectionDataSource inside the actual report.
            add("isolationPoints", isolationPoints);
        } else {
            //If it's not long, it's short... or invalid... but we'll pretend that being invalid is impossible.
            List<IsolationPointShortPrintoutContainer> isolationPoints = convertToShortIPContainerCollection(procDef.getLockIsolationPoints());
            add("isolationPoints", isolationPoints);

            //FIXME: Pull proper image data here... we're using static garbage for now...

            //Now, we have to do the images...  these are special images that hold all annotations associated with the
            //single image.
            List<ImageShortPrintoutContainer> allImages = convertToShortImageContainerCollection(procDef.getImages());



//            ImageShortPrintoutContainer imageContainer = new ImageShortPrintoutContainer();
//            try {
//                imageContainer.setImage(new FileInputStream(new File("/Users/jheath/Pictures/skeptical-smiley-face-g9WKOp-200x200.jpg")));
//                allImages.add(imageContainer);
//                imageContainer = new ImageShortPrintoutContainer();
//                imageContainer.setImage(new FileInputStream(new File("/Users/jheath/Pictures/skeptical-smiley-face-g9WKOp-200x200.jpg")));
//                allImages.add(imageContainer);
//                imageContainer = new ImageShortPrintoutContainer();
//                imageContainer.setImage(new FileInputStream(new File("/Users/jheath/Pictures/skeptical-smiley-face-g9WKOp-200x200.jpg")));
//                allImages.add(imageContainer);
//                imageContainer = new ImageShortPrintoutContainer();
//                imageContainer.setImage(new FileInputStream(new File("/Users/jheath/Pictures/skeptical-smiley-face-g9WKOp-200x200.jpg")));
//                allImages.add(imageContainer);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            add("allImages", allImages);
        }
    }

    private List<ImageShortPrintoutContainer> convertToShortImageContainerCollection(List<ProcedureDefinitionImage> images) {
        return images.stream()
                     .map(this::convertToShortImageContainer)
                     .filter(container -> container.getImage() != null)
                     .collect(Collectors.toList());
    }

    private ImageShortPrintoutContainer convertToShortImageContainer(ProcedureDefinitionImage image) {
        ImageShortPrintoutContainer returnMe = new ImageShortPrintoutContainer();

        try {
            byte[] imageData = s3Service.downloadProcedureDefinitionImageSvg(image);

            if(imageData == null) {
                //This might just mean that we haven't generated the SVGs yet... so we'll do that now.
                try {
                    svgGenerationService.generateAndUploadAnnotatedSvgs(procDef);

                    imageData = s3Service.downloadProcedureDefinitionImageSvg(image);

                    if(imageData == null) throw new Exception("Image didn't exist...");

                    returnMe.setImage(new ByteArrayInputStream(imageData));
                } catch (IOException ioe) {
                    logger.warn("There was an IOException while trying to download the ProcedureDefinitionImage with ID " + image.getId(), ioe);
                } catch (Exception e) {
                    logger.error("There was a problem while Generating SVGs for ProcDef with ID " + procDef.getId(), e);
                    e.printStackTrace();
                }
            } else {
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
     * This method just uses streams and the convertToContainer method to re-map the collection into a series of POJOs
     * better suited for being fed to Jasper.  This ensures Jasper doesn't have to deal with converting content or
     * digging deep into the complex relationships of the ProcedureDefinition model.
     *
     * @param isolationPoints - A <b>List</b> populated with all <b>IsolationPoint</b>s from the definition.
     * @return A <b>List</b> populated with <b>IsolationPointPrintoutContainer</b>s, representing the provided Isolation Points.
     */
    private List<IsolationPointLongPrintoutContainer> convertToLongIPContainerCollection(List<IsolationPoint> isolationPoints) {
        return isolationPoints.stream()
                              //Convert it to the container objects to ensure Jasper has an easy time digging into the
                              //collection.
                              .map(this::convertToLongContainer)
                              .collect(Collectors.toList());
    }

    private List<IsolationPointShortPrintoutContainer> convertToShortIPContainerCollection(List<IsolationPoint> isolationPoints) {
        return isolationPoints.stream()
                              .map(this::convertToShortContainer)
                              .collect(Collectors.toList());
    }

    private IsolationPointShortPrintoutContainer convertToShortContainer(IsolationPoint isolationPoint) {
        IsolationPointShortPrintoutContainer container = new IsolationPointShortPrintoutContainer();

        container.setCheck(isolationPoint.getCheck());
        container.setDevice(isolationPoint.getDeviceDefinition().getAssetType() == null ? isolationPoint.getDeviceDefinition().getFreeformDescription() : isolationPoint.getDeviceDefinition().getAssetType().getDisplayName());
        //Is this right?
        container.setEnergySource(isolationPoint.getSourceType() == null ? isolationPoint.getSourceText() : isolationPoint.getSourceType().name());
        container.setLockoutMethod(isolationPoint.getMethod());
        container.setSourceId(isolationPoint.getIdentifier());
        container.setSourceLocation(isolationPoint.getLocation());

        return container;
    }

    private IsolationPointLongPrintoutContainer convertToLongContainer(IsolationPoint isolationPoint) {

        IsolationPointLongPrintoutContainer container = new IsolationPointLongPrintoutContainer();

        //NOTE: This might not be right.  Check the output, because I think this might be the all-caps value...
        container.setEnergySource(isolationPoint.getSourceType().toString());

        container.setDevice(isolationPoint.getDeviceDefinition().getAssetType() != null ? isolationPoint.getDeviceDefinition().getAssetType().getDisplayName() : isolationPoint.getDeviceDefinition().getFreeformDescription());

        container.setLockoutMethod(isolationPoint.getMethod());

        //TODO: Remove this line once you're properly handling images.
//        try {
//            container.setImage(new FileInputStream(new File("/Users/jheath/Pictures/skeptical-smiley-face-g9WKOp-200x200.jpg")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


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
