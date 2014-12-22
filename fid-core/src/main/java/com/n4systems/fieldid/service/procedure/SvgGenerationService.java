package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.FileUtils;
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
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SvgGenerationService extends FieldIdPersistenceService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    public static Integer DEFAULT_JASPER_HEIGHT = 140;
    public static Integer DEFAULT_JASPER_WIDTH = 140;

    public void generateAndUploadAnnotatedSvgs(ProcedureDefinition definition) throws Exception {
        for(ProcedureDefinitionImage image: definition.getImages()) {
            File allAnnotations = exportToSvg(image);
            uploadSvg(definition, allAnnotations);
        }

        definition.getUnlockIsolationPoints()
                  .stream()
                  //We need to make sure to only do this for Isolation Points that have annotations... otherwise we
                  //get a NullPointerException
                  .filter(isolationPoint -> isolationPoint.getAnnotation() != null)
                  .forEach(isolationPoint -> {
                      try {
                          File singleAnnotation = exportToSvg(isolationPoint.getAnnotation());
                          uploadSvg(definition, singleAnnotation);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  });
    }

    private void uploadSvg(ProcedureDefinition procedureDefinition, File svgFile) {
        s3Service.uploadProcedureDefinitionSvg(procedureDefinition, svgFile);
    }

    private File exportToSvg(ProcedureDefinitionImage image) throws Exception {
        return exportToSvg(new DOMSource(buildSvg(image)), image.getFileName());
    }

    private File exportToSvg(ImageAnnotation annotation) throws Exception {
        return exportToSvg(new DOMSource(buildSvg(annotation)), annotation.getImage().getFileName() + "_" + annotation.getId());
    }

    private File exportToSvg(DOMSource source, String filename) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        File file = PathHandler.getTempFile(filename + ".svg");
        file.createNewFile();
        StreamResult result = new StreamResult(file.getPath());
        transformer.transform(source, result);
        return file;
    }

    private Document buildSvg(ImageAnnotation annotation) throws Exception{
        Document doc = getDocument();

        File imageFile = PathHandler.getTempFile();
        byte [] bytes = s3Service.downloadProcedureDefinitionImage((ProcedureDefinitionImage) annotation.getImage());

        //convert image to be scaled down to jasper size
        bytes = imageService.scaleImage(bytes, DEFAULT_JASPER_WIDTH, DEFAULT_JASPER_HEIGHT);

        FileUtils.writeByteArrayToFile(imageFile, bytes);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        Element defs = doc.createElement("defs");

        svg.appendChild(defs);

        defs.appendChild(createAnnotationDefinition(doc, annotation, width, height));

        Element imageElement = createImageElement(doc, bytes, height, width);

        svg.appendChild(imageElement);

        svg.appendChild(createAnnotation(doc, annotation, height, width));

        return doc;
    }

    private Document buildSvg(ProcedureDefinitionImage image) throws Exception {

        Document doc = getDocument();

        File imageFile = PathHandler.getTempFile();
        byte [] bytes = s3Service.downloadProcedureDefinitionImage(image);

        //convert image to be scaled down to jasper size
        bytes = imageService.scaleImage(bytes, DEFAULT_JASPER_WIDTH, DEFAULT_JASPER_HEIGHT);

        FileUtils.writeByteArrayToFile(imageFile, bytes);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        Element defs = doc.createElement("defs");

        svg.appendChild(defs);

        for(ImageAnnotation annotation: image.getAnnotations()) {
            defs.appendChild(createAnnotationDefinition(doc, annotation, width, height));
        }

        Element imageElement = createImageElement(doc, bytes, height, width);

        svg.appendChild(imageElement);

        for(ImageAnnotation annotation: image.getAnnotations()) {
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
                        File singleAnnotation = exportToSvg(new DOMSource(generateArrowStyleAnnotatedImage(annotation)), annotation.getImage().getFileName() + "_" + annotation.getId());
                        uploadSvg(definition, singleAnnotation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public Document generateArrowStyleAnnotatedImage(ImageAnnotation annotation) throws Exception{
        Document doc = getDocument();

        File imageFile = PathHandler.getTempFile();
        byte [] bytes = s3Service.downloadProcedureDefinitionImage((ProcedureDefinitionImage) annotation.getImage());
        FileUtils.writeByteArrayToFile(imageFile, bytes);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        svg.appendChild(createArrowMarkerDefinition(doc));

        svg.appendChild(createImageElement(doc, bytes));

        svg.appendChild(createArrowAnnotation(doc, annotation, height, width));

        doc.appendChild(svg);

        //printDocument(doc, System.out);
        return doc;
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
        line.setAttribute("stroke-width", "25");
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
