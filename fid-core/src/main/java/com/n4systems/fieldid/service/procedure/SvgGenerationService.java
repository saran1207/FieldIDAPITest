package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.*;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.stream.Collectors;

public class SvgGenerationService extends FieldIdPersistenceService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    private static final Logger logger = Logger.getLogger(SvgGenerationService.class);

    public static final Integer DEFAULT_JASPER_HEIGHT = 140;
    public static final Integer DEFAULT_JASPER_WIDTH = 140;
    private static final double DEFAULT_IMAGE_SIZE = 200.0;
    private static final double DEFAULT_STROKE_WIDTH = 5.0;
    private static final double DEFAULT_OUTPUT_QUALITY = 1.0;

    public void generateAndUploadAnnotatedSvgs(ProcedureDefinition definition) throws Exception {
        for(ProcedureDefinitionImage image: definition.getImages()) {
            byte[] allAnnotations = exportToSvg(image);
            //DO NOT forget the ".svg" portion.  It will cause a zombie apocalypse.
            uploadSvg(definition, allAnnotations, image.getFileName() + ".svg");
        }

        definition.getUnlockIsolationPoints()
                  .stream()
                  //We need to make sure to only do this for Isolation Points that have annotations... otherwise we
                  //get a NullPointerException
                  .filter(isolationPoint -> isolationPoint.getAnnotation() != null)
                  .forEach(isolationPoint -> {
                      try {
                          byte[] singleAnnotation = exportToSvg(isolationPoint.getAnnotation());
                          uploadSvg(definition, singleAnnotation, isolationPoint.getAnnotation().getImage().getFileName() + "_" + isolationPoint.getAnnotation().getId() + ".svg");
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  });
    }

    private void uploadSvg(ProcedureDefinition procedureDefinition, byte[] fileContents, String fileName) {
        s3Service.uploadProcedureDefinitionSvg(procedureDefinition, fileContents, fileName);
    }

    private byte[] exportToSvg(ProcedureDefinitionImage image) throws Exception {
        return exportToSvg(new DOMSource(buildSvg(image)));
    }

    private byte[] exportToSvg(ImageAnnotation annotation) throws Exception {
        return exportToSvg(new DOMSource(buildSvg(annotation)));
    }

    private byte[] exportToSvg(DOMSource source) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //This can be empty.  We're writing to it and we can access its internal bytes later... I think.
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        //We now use the ByteArrayOutputStream to instantiate the StreamResult to work with the DOMSource.
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

        //Well, we're now done with all that hairy business.  We pipe out the byte array to keep this all happening
        //in memory instead of on disk.
        return output.toByteArray();
    }

    private Document buildSvg(ImageAnnotation annotation) throws Exception{
        Document doc = getDocument();

        byte [] bytes = s3Service.downloadProcedureDefinitionImage((ProcedureDefinitionImage) annotation.getImage());

        //convert image to be scaled down to jasper size
        bytes = imageService.scaleImage(bytes, DEFAULT_JASPER_WIDTH, DEFAULT_JASPER_HEIGHT, DEFAULT_OUTPUT_QUALITY);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        //This part only matters if the annotation is supposed to be rendered.  If it's not supposed to be rendered,
        //we don't add the Annotation definition or element.
        if(annotation.isRenderAnnotation()) {

            Element defs = doc.createElement("defs");

            svg.appendChild(defs);

            defs.appendChild(createAnnotationDefinition(doc, annotation, width, height));
        }


            Element imageElement = createImageElement(doc, bytes, height, width);

            svg.appendChild(imageElement);

        //Again... no render annotation?  No do this step.
        if(annotation.isRenderAnnotation()) {
            svg.appendChild(createAnnotation(doc, annotation, height, width));
        }

        return doc;
    }

    private Document buildSvg(ProcedureDefinitionImage image) throws Exception {

        Document doc = getDocument();

        //Not sure why we're fetching the image from S3, when we are trying to build the SVG now.
        byte [] bytes = s3Service.downloadProcedureDefinitionImage(image);

        //convert image to be scaled down to jasper size
        bytes = imageService.scaleImage(bytes, DEFAULT_JASPER_WIDTH, DEFAULT_JASPER_HEIGHT, DEFAULT_OUTPUT_QUALITY);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        Element defs = doc.createElement("defs");

        svg.appendChild(defs);

        //Ensure we only add annotations that are supposed to render.... presumably we could have some that are NOT
        //supposed to render mixed in.
        for(ImageAnnotation annotation: image.getAnnotations().stream().filter(ImageAnnotation::isRenderAnnotation).collect(Collectors.toList())) {
            defs.appendChild(createAnnotationDefinition(doc, annotation, width, height));
        }

        Element imageElement = createImageElement(doc, bytes, height, width);

        svg.appendChild(imageElement);

        //Again, only annotations that are supposed to render.
        for(ImageAnnotation annotation: image.getAnnotations().stream().filter(ImageAnnotation::isRenderAnnotation).collect(Collectors.toList())) {
            svg.appendChild(createAnnotation(doc, annotation, height, width));
        }

        return doc;
    }

    private Document getDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        DOMImplementation domImpl = doc.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("svg","-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
        doc.appendChild(doctype);
        return doc;
    }

    private Element createSvgElement(Document doc, Integer width, Integer height) {
        Element svg = doc.createElement("svg");
        svg.setAttribute("viewBox", "0 0 " + width + " " + height);
        svg.setAttribute("height", DEFAULT_JASPER_HEIGHT.toString());
        svg.setAttribute("width", DEFAULT_JASPER_WIDTH.toString());
        svg.setAttribute("version", "1.1");
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        svg.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        return svg;
    }

    private Element createImageElement(Document doc, byte[] bytes) {
        Element imageElement = doc.createElement("image");
        imageElement.setAttribute("x", "0");
        imageElement.setAttribute("y", "0");
        imageElement.setAttribute("height", "100%");
        imageElement.setAttribute("width", "100%");
        imageElement.setAttribute("xlink:href", "data:image/png;base64," + DatatypeConverter.printBase64Binary(bytes));
        return imageElement;
    }

    private Element createImageElement(Document doc, byte[] bytes, int height, int width) {
        Element imageElement = doc.createElement("image");
        imageElement.setAttribute("x", "0");
        imageElement.setAttribute("y", "0");
        imageElement.setAttribute("height", height+"");
        imageElement.setAttribute("width", width+"");
        imageElement.setAttribute("xlink:href", "data:image/png;base64," + DatatypeConverter.printBase64Binary(bytes));
        return imageElement;
    }

    private Node createAnnotation(Document doc, ImageAnnotation annotation, Integer height, Integer width) {
        Element annotationElement = doc.createElement("use");
        annotationElement.setAttribute("xlink:href", "#" + annotation.getType().getCssClass() + "_" + annotation.getText());
        annotationElement.setAttribute("x", Math.round(width * annotation.getX()) + "");
        annotationElement.setAttribute("y", Math.round(height * annotation.getY()) + "");
        return annotationElement;
    }

    private Node createAnnotationDefinition(Document doc, ImageAnnotation annotation, Integer width, Integer height) {

        Boolean isReversed =  Math.round(width * annotation.getX()) < width/2;
        Boolean isWide = width > height;

        Element group = doc.createElement("g");
        group.setAttribute("id", annotation.getType().getCssClass() + "_" + annotation.getText());

        if(isWide) {
            Element path = doc.createElement("path");
            path.setAttribute("id", "arrow");
            path.setAttribute("d", "M 0 0 l -12 4 l 0 -3 l -40 0 l 0 -3 l 40 0 l 0 -4 z");
            path.setAttribute("fill", annotation.getType().getBackgroundColor());
            path.setAttribute("stroke", annotation.getType().getBorderColor());
            path.setAttribute("stroke-width", "1");
            if (isReversed) {
                path.setAttribute("transform", "scale(-1, 1)");
            }
            group.appendChild(path);

            Element rect = doc.createElement("rect");

            rect.setAttribute("x", "-63");
            rect.setAttribute("y", "-5");
            rect.setAttribute("rx", "5");
            rect.setAttribute("ry", "5");

            int lenght = annotation.getText().length();
            if (lenght <= 4) {
                rect.setAttribute("width", "25%");
            } else if ((lenght > 4) && (lenght <= 6)) {
                rect.setAttribute("width", "35%");
            } else {
                rect.setAttribute("width", "40%");
            }

            rect.setAttribute("height", "10%");
            rect.setAttribute("fill", annotation.getType().getBackgroundColor());
            rect.setAttribute("stroke", annotation.getType().getBorderColor());
            rect.setAttribute("stroke-width", "1");

            if (isReversed) {
                rect.setAttribute("transform", "scale(-1, 1)");
            }

            group.appendChild(rect);

            Element text = doc.createElement("text");
            if (isReversed) {
                if (lenght <= 4) {
                    text.setAttribute("x", "30");
                } else if ((lenght > 4) && (lenght <= 6)) {
                    text.setAttribute("x", "22");
                } else {
                    text.setAttribute("x", "9");
                }
            } else {
                text.setAttribute("x", "-61");
            }
            text.setAttribute("y", "2");
            text.setAttribute("fill", annotation.getType().getFontColor());

            text.setAttribute("font-size", "7");

            text.setTextContent(annotation.getText());

            group.appendChild(text);
        } else {
            Element path = doc.createElement("path");
            path.setAttribute("id", "arrow");
            path.setAttribute("d", "M 0 0 l -12 4 l 0 -3 l -40 0 l 0 -3 l 40 0 l 0 -4 z");
            path.setAttribute("fill", annotation.getType().getBackgroundColor());
            path.setAttribute("stroke", annotation.getType().getBorderColor());
            path.setAttribute("stroke-width", "1");
            if (isReversed) {
                path.setAttribute("transform", "scale(-1, 1)");
            }

            group.appendChild(path);
            Element rect = doc.createElement("rect");

            rect.setAttribute("x", "-75");
            rect.setAttribute("y", "-8");
            rect.setAttribute("rx", "5");
            rect.setAttribute("ry", "5");

            int lenght = annotation.getText().length();
            if (lenght <= 4) {
                rect.setAttribute("width", "25%");
            } else if ((lenght > 4) && (lenght <= 6)) {
                rect.setAttribute("width", "35%");
            } else {
                rect.setAttribute("width", "47%");
            }

            rect.setAttribute("height", "10%");
            rect.setAttribute("fill", annotation.getType().getBackgroundColor());
            rect.setAttribute("stroke", annotation.getType().getBorderColor());
            rect.setAttribute("stroke-width", "1");

            if (isReversed) {
                rect.setAttribute("transform", "scale(-1, 1)");
            }

            group.appendChild(rect);
            Element text = doc.createElement("text");


            if (isReversed) {
                if (lenght <= 4) {
                    text.setAttribute("x", "50");
                } else if ((lenght > 4) && (lenght <= 6)) {
                    text.setAttribute("x", "39");
                } else {
                    text.setAttribute("x", "29");
                }
            } else {
                text.setAttribute("x", "-74");
            }
            text.setAttribute("y", "2");
            text.setAttribute("fill", annotation.getType().getFontColor());

            text.setAttribute("font-size", "7");

            text.setTextContent(annotation.getText());

            group.appendChild(text);
        }

        return group;
    }


    /******************************** Arrow Style Annotation SVG ***********************/


    public void generateAndUploadArrowStyleAnnotatedSvgs(ProcedureDefinition definition) throws Exception {
        definition.getUnlockIsolationPoints()
                .stream()
                        //We need to make sure to only do this for Isolation Points that have annotations... otherwise we
                        //get a NullPointerException
                .filter(isolationPoint -> isolationPoint.getAnnotation() != null)
                .forEach(isolationPoint -> {
                    try {
                        ImageAnnotation annotation = isolationPoint.getAnnotation();
                        Document document = generateArrowStyleAnnotatedImage(annotation);

                        if(document != null) {
                            byte[] singleAnnotation = exportToSvg(new DOMSource(generateArrowStyleAnnotatedImage(annotation)));
                            uploadSvg(definition, singleAnnotation, annotation.getImage().getFileName() + "_" + annotation.getId() + ".svg");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public Document generateArrowStyleAnnotatedImage(ImageAnnotation annotation) throws Exception{
        Document doc = getDocument();

        byte [] bytes = s3Service.downloadProcedureDefinitionImage((ProcedureDefinitionImage) annotation.getImage());

        if(bytes != null) {

            //convert image to be scaled down to jasper size
            bytes = imageService.scaleImage(bytes, DEFAULT_JASPER_WIDTH, DEFAULT_JASPER_HEIGHT, DEFAULT_OUTPUT_QUALITY);

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

            Integer width = bufferedImage.getWidth();
            Integer height = bufferedImage.getHeight();

            Element svg = createSvgElement(doc, width, height);

            if (annotation.isRenderAnnotation()) {
                svg.appendChild(createArrowMarkerDefinition(doc));
            }

            svg.appendChild(createImageElement(doc, bytes));

            if (annotation.isRenderAnnotation()) {
                svg.appendChild(createArrowAnnotation(doc, annotation, height, width));
            }

            doc.appendChild(svg);

            //printDocument(doc, System.out);
            return doc;
        }

        return null;
    }

    private Element createArrowMarkerDefinition(Document doc) {
        Element defs = doc.createElement("defs");

        Element marker = doc.createElement("marker");
        marker.setAttribute("id", "arrow");
        marker.setAttribute("markerUnits", "strokeWidth");
        marker.setAttribute("orient", "auto");
        marker.setAttribute("markerWidth", "4.6");
        marker.setAttribute("markerHeight", "4.6");
        marker.setAttribute("refX", "0.9199999999999999");
        marker.setAttribute("refY", "2.3");

        Element polygon = doc.createElement("polygon");
        polygon.setAttribute("id", "arrow-poly");
        polygon.setAttribute("points", "0,2.3 4.6,0 3.4499999999999997,2.3 4.6,4.6");
        polygon.setAttribute("fill", "#ff0000");

        marker.appendChild(polygon);
        defs.appendChild(marker);

        return defs;
    }

    private Element createArrowAnnotation(Document doc, ImageAnnotation annotation, Integer height, Integer width) {
        Element group = doc.createElement("g");
        group.setAttribute("id", "annotations");

        Element line = doc.createElement("line");
        line.setAttribute("x1", String.valueOf(Math.round(width * annotation.getX())));
        line.setAttribute("y1", String.valueOf(Math.round(height * annotation.getY())));
        line.setAttribute("x2", String.valueOf(Math.round(width * annotation.getX_tail())));
        line.setAttribute("y2", String.valueOf(Math.round(height * annotation.getY_tail())));
        line.setAttribute("stroke", "#ff0000");

        Double strokeWidth = width / DEFAULT_IMAGE_SIZE * DEFAULT_STROKE_WIDTH;

        line.setAttribute("stroke-width", strokeWidth.toString());
        line.setAttribute("marker-start", "url(#arrow)");

        group.appendChild(line);

        return group;
    }

    //For Testing
    @Deprecated
    private static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }

}
