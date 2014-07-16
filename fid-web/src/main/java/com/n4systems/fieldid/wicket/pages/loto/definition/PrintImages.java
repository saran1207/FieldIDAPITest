package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintImages extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;
    protected @SpringBean JsonRenderer renderer;

    private static final String INIT_JS = "fieldIdWidgets.annotate('%s');";
    private List<String> imageMarkupIds = new ArrayList<String>();

    public PrintImages(String id, final IModel<ProcedureDefinition> model) {
        super(id,new PropertyModel(model,"images"));

        add(new ListView<ProcedureDefinitionImage>("imagesu", ProxyModel.of(model, on(ProcedureDefinition.class).getImages())) {
            @Override
            protected void populateItem(ListItem<ProcedureDefinitionImage> item) {
                item.getDefaultModelObject();
                ExternalImage image = new ExternalImage("image", getImageUrl(item));

                image.setOutputMarkupId(true);
                imageMarkupIds.add(image.getMarkupId());
                item.add(image);
            }
        });
    }

    protected String getImageUrl(ListItem<ProcedureDefinitionImage> item) {
        return s3Service.getProcedureDefinitionImageMediumURL(item.getModelObject()).toString();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderJavaScriptReference("//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");
        response.renderJavaScriptReference("javascript/print/annotate-images.js");

        response.renderCSSReference("style/legacy/component/annotated-image.css");

        List<ProcedureDefinitionImage> images = (List<ProcedureDefinitionImage>) getDefaultModel().getObject();

        String jsonStr = "";
        List<AnnotatedImage> annotatedImages = new ArrayList<AnnotatedImage>();

        Iterator<String> imageIdIterator = imageMarkupIds.iterator();
        for(ProcedureDefinitionImage image: images) {
            annotatedImages.add(new AnnotatedImage(imageIdIterator.next(), convertAnnotations(image.getAnnotations())));
        }

        if (null != annotatedImages && annotatedImages.size() > 0) {
            jsonStr = renderer.render(new ImageList(annotatedImages));
            response.renderOnLoadJavaScript("annotateImages(" + jsonStr + ");");
        }
    }

    private List<Annotation> convertAnnotations(List<ImageAnnotation> annotations) {
        List<Annotation> convertedAnnotations =  new ArrayList<Annotation>();
        for (ImageAnnotation annotation : annotations) {
            convertedAnnotations.add(new Annotation(annotation));
        }
        return convertedAnnotations;
    }

    private void addTestImages(List<ProcedureDefinitionImage> images) {
        ProcedureDefinitionImage imge = null;

        for(int i=0; i<=25; i++) {
            imge = new ProcedureDefinitionImage();
           // imge.setId(images.get(0).getId());
            imge.setProcedureDefinition(images.get(0).getProcedureDefinition());
            imge.setFileName(images.get(0).getFileName());
           // imge.setAnnotations(images.get(0).getAnnotations());
            imge.setTenant(images.get(0).getTenant());
            images.add(imge);
        }

    }


    class AnnotatedImage {

     String id;
     List<Annotation> annotations = Lists.newArrayList();

        AnnotatedImage() {}

        AnnotatedImage(String id, List<Annotation> annotations) {
            this.id = id;
            this.annotations = annotations;
        }

    }

    class ImageList {

        public List<AnnotatedImage> images = Lists.newArrayList();

        ImageList() {}

        ImageList(List<AnnotatedImage> images) {
            this.images = images;
        }

    }

    class Options {

        ImageList options = null;

        Options(ImageList options) {
            this.options = options;
        }

    }

    class Annotation {
        Double x;
        Double y;
        String text;
        String cssStyle;

        Annotation(ImageAnnotation imageAnnotation) {
            x = imageAnnotation.getX();
            y = imageAnnotation.getY();
            text = imageAnnotation.getText();
            cssStyle = imageAnnotation.getType().getCssClass();
        }
    }

}
