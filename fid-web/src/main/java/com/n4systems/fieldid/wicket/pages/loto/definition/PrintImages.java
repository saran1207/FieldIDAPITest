package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.image.ImageList;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
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
import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintImages extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;
    protected @SpringBean JsonRenderer renderer;

    public PrintImages(String id, final IModel<ProcedureDefinition> model) {
        super(id,new PropertyModel(model,"images"));
        // if no images...add one....
//        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
//        model.getObject().getImages().add(image);
        //

//        image.addImageAnnotation(new ImageAnnotation(.3434,.1235,"hello", ImageAnnotationType.getDefault()));

        // add images for testing

        //addTestImages(images);

//        PropertyModel<List<ProcedureDefinitionImage>> pmod = ProxyModel.of(model, on(ProcedureDefinition.class).getImages());
//
//        List<ProcedureDefinitionImage> pimgs = ( List<ProcedureDefinitionImage>)pmod.getObject();
//        addTestImages(pimgs);

        add(new ListView<ProcedureDefinitionImage>("images", ProxyModel.of(model, on(ProcedureDefinition.class).getImages())) {
            @Override
            protected void populateItem(ListItem<ProcedureDefinitionImage> item) {
                item.getDefaultModelObject();
                item.add(new ExternalImage("image", getImageUrl(item)));


            }
        });

        /*
        add(new ListView<ProcedureDefinitionImage>("images", pimgs) {
            @Override
            protected void populateItem(ListItem<ProcedureDefinitionImage> item) {
                item.getDefaultModelObject();
                item.add(new ExternalImage("image", getImageUrl(item)));


            }
        });
          */
    }

    protected String getImageUrl(ListItem<ProcedureDefinitionImage> item) {
        String path = item.getModelObject().getS3Path();
        String imageUrl = s3Service.generateResourceUrl(path).toString();
        return imageUrl;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");

       List<ProcedureDefinitionImage> images = (List<ProcedureDefinitionImage>) getDefaultModel().getObject();

        String jsonStr = "";
        List<AnnotatedImage> anns = new ArrayList<AnnotatedImage>();

        for(ProcedureDefinitionImage image: images) {
            anns.add(new AnnotatedImage(image.getId(), image.getAnnotations()));
        }

        if (null != anns && anns.size() > 0) {
            jsonStr = renderer.render(anns);
            // response.renderOnLoadJavaScript("alert('" + jsonStr + "')");
        }
    }

    private void addTestImages(List<ProcedureDefinitionImage> images) {
        ProcedureDefinitionImage imge = null;

        for(int i=0; i<=15; i++) {
            imge = new ProcedureDefinitionImage();
            imge.setId(new Long(12+i));
            imge.setProcedureDefinition(images.get(0).getProcedureDefinition());
            imge.setFileName(images.get(0).getFileName());
            images.add(imge);
        }

    }


    class AnnotatedImage {

     Long wid;
     List<ImageAnnotation> annotations = Lists.newArrayList();

        AnnotatedImage() {}

        AnnotatedImage(Long wid, List<ImageAnnotation> annotations) {
            this.wid = wid;
            this.annotations = annotations;
        }

    }

}
