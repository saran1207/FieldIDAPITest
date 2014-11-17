package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.File;

public class SvgGenerationService extends FieldIdPersistenceService {

    @Autowired
    private S3Service s3Service;

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
        FileUtils.writeByteArrayToFile(imageFile, bytes);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        Element defs = doc.createElement("defs");

        svg.appendChild(defs);

        defs.appendChild(createAnnotationDefinition(doc, annotation, width));

        Element imageElement = createImageElement(doc, bytes, width, height);

        svg.appendChild(imageElement);

        svg.appendChild(createAnnotation(doc, annotation, height, width));

        return doc;
    }

    private Document buildSvg(ProcedureDefinitionImage image) throws Exception {

        Document doc = getDocument();

        File imageFile = PathHandler.getTempFile();
        byte [] bytes = s3Service.downloadProcedureDefinitionImage(image);
        FileUtils.writeByteArrayToFile(imageFile, bytes);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();

        Element svg = createSvgElement(doc, width, height);

        doc.appendChild(svg);

        Element defs = doc.createElement("defs");

        svg.appendChild(defs);

        for(ImageAnnotation annotation: image.getAnnotations()) {
            defs.appendChild(createAnnotationDefinition(doc, annotation, width));
        }

        Element imageElement = createImageElement(doc, bytes, width, height);

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
        svg.setAttribute("version", "1.1");
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        svg.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        return svg;
    }

    private Element createImageElement(Document doc, byte[] bytes, Integer width, Integer height) {
        Element imageElement = doc.createElement("image");
        imageElement.setAttribute("x", "0");
        imageElement.setAttribute("y", "0");
        imageElement.setAttribute("height", height.toString());
        imageElement.setAttribute("width", width.toString());
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

    private Node createAnnotationDefinition(Document doc, ImageAnnotation annotation, Integer width) {

        Boolean isReversed =  Math.round(width * annotation.getX()) < width/2;

        Element group = doc.createElement("g");
        group.setAttribute("id", annotation.getType().getCssClass() + "_" + annotation.getText());

        Element path = doc.createElement("path");
        path.setAttribute("id", "arrow");
        path.setAttribute("d", "M 0 0 l -12 12 l 0 -8 l -75 0 l 0 -8 l 75 0 l 0 -8 z");
        path.setAttribute("fill", annotation.getType().getBackgroundColor());
        path.setAttribute("stroke", annotation.getType().getBorderColor());
        path.setAttribute("stroke-width", "1");
        if(isReversed) {
            path.setAttribute("transform", "scale(-1, 1)");
        }

        group.appendChild(path);

        Element rect = doc.createElement("rect");
        rect.setAttribute("x", "-155");
        rect.setAttribute("y", "-13");
        rect.setAttribute("rx", "5");
        rect.setAttribute("ry", "5");
        rect.setAttribute("width", "60");
        rect.setAttribute("height", "24");
        rect.setAttribute("fill", annotation.getType().getBackgroundColor());
        rect.setAttribute("stroke", annotation.getType().getBorderColor());
        rect.setAttribute("stroke-width", "2");
        if(isReversed) {
            rect.setAttribute("transform", "scale(-1, 1)");
        }

        group.appendChild(rect);

        Element text = doc.createElement("text");
        if(isReversed) {
            text.setAttribute("x", "100");
        } else {
            text.setAttribute("x", "-150");
        }
        text.setAttribute("y", "6");
        text.setAttribute("fill", annotation.getType().getFontColor());
        text.setAttribute("font-size", "16");
        text.setTextContent(annotation.getText());

        group.appendChild(text);

        return group;
    }


}
