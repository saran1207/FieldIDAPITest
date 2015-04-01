package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.AnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the new Image Editor.  This new design allows us to work directly with SVGs and avoid much of the complexity
 * of the old editor.
 *
 * Created by Jordan Heath on 14-12-05.
 */
public abstract class ArrowStyleEditorAndGalleryPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    private ImageList<ProcedureDefinitionImage> images;
    private Component editor;
    private FileUploadField fileUploadField;
    private Form tinyForm;
    private Component placeholder;
    private WebMarkupContainer editorAndGalleryContainer;

    private List<FileUpload> fileUploads = new ArrayList<>();
    private IModel<IsolationPoint> model;
    private AnnotationType annotationType;
    private ProcedureDefinitionImage currentImage;

//    private ArrowStyleAnnotatingBehaviour ajaxBehavior;

    private final static Logger logger = Logger.getLogger(ArrowStyleEditorAndGalleryPanel.class);

    public ArrowStyleEditorAndGalleryPanel(String id, IModel<IsolationPoint> model, AnnotationType annotationType) {
        super(id, model);
        this.model = model;
        this.currentImage = (model.getObject().getAnnotation() == null ? null : (ProcedureDefinitionImage) model.getObject().getAnnotation().getImage());
        this.annotationType = annotationType;
    }


    /*
        Overrides...
     */

    @Override
    public void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);

        //We ended up needing this extra WebMarkupContainer to allow us to do a partial refresh of the page.  This ended
        //up being necessary so that the FileUploadField didn't get refreshed when swapping between the placeholder
        //image and the Editor panel when adding the first image to a ProcedureDefinition.
        add(editorAndGalleryContainer = new WebMarkupContainer("editorAndGalleryContainer"));
        editorAndGalleryContainer.setOutputMarkupId(true);
        editorAndGalleryContainer.add(placeholder = new ContextImage("placeholderImage", "images/loto/upload-lightbox-blank-slate.png"));
        placeholder.setOutputMarkupId(true);
        if(currentImage == null) {
            //Creating the panel without an image leaves a placeholder where the image would otherwise be.
            editor = createEditorPanel().setVisible(false);

            //...but the placeholder is junk... so instead we hide the editor and build a ContextImage to hold as placeholder.
            placeholder.setVisible(true);
        } else {
            //When have an annotation, we pass that in to be able to display the associated image (if there is one)
            //or the placeholder if there is not an image.
            editor = createEditorPanel(model.getObject().getAnnotation());

            //We have an image and annotation, so hide the placeholder.
            placeholder.setVisible(false);
        }



        editorAndGalleryContainer.add(editor);
        editor.setOutputMarkupId(true);
        editorAndGalleryContainer.add(images = createImageList("imageGallery"));
        images.setOutputMarkupId(true);
        tinyForm = new Form("uploadForm");
        tinyForm.add(fileUploadField = new FileUploadField("fileUpload", new PropertyModel<>(this, "fileUploads")));
        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedImage = fileUploadField.getFileUpload();
                if (uploadedImage != null) {
                    //Steps to handle image upload...
                    //1) Take image and ram it up into S3
                    handleUpload(uploadedImage.getBytes(), uploadedImage.getContentType(), uploadedImage.getClientFileName());

                    //2) Now that the image is in S3 and our image model, refresh the image gallery/list thingy
                    refreshGalleryPanel(target, createImageList("imageGallery"));

                    //3) The gallery has now been refreshed, so we can switch to the uploaded image... the last in the
                    //   carousel.
                    switchToLastCarouselImage(target);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                //Do nothing, look pretty...
            }
        });
        add(tinyForm);

        add(new Label("instructions", new FIDLabelModel("label.annotate_instructions")));

        add(new AjaxLink<Void>("done") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doDone(target);
            }
        });
    }


    /*
        Private methods...
     */

    private void switchToLastCarouselImage(AjaxRequestTarget target) {
        currentImage = displayableImages().get(displayableImages().size() - 1);

        //Oh, we should probably set this as the image for the annotation, no?
        if(model.getObject().getAnnotation() == null) {
            model.getObject().setAnnotation(new ImageAnnotation());
        }
        model.getObject().getAnnotation().setImage(currentImage);

        //If you're switching the image, then your annotation should no longer be shown... right?
        swapEditorPanel(target, createEditorPanel(currentImage));
    }

    private void swapEditorPanel(AjaxRequestTarget target, Component panel) {
        if(!editor.isVisible()) {
            editor.setVisible(true);
            placeholder.setVisible(false);
            target.add(placeholder, editorAndGalleryContainer);
        }

        //Replace the panel...
        editor.replaceWith(panel);

        //...then switch the reference.
        editor = panel;
        target.add(panel, editor);
    }

    private void refreshGalleryPanel(AjaxRequestTarget target, ImageList<ProcedureDefinitionImage> panel) {
        panel.setOutputMarkupId(true);
        images.replaceWith(panel);
        target.add(panel);
        images = panel;
    }

    private void handleUpload(byte[] bytes, String contentType, String clientFileName) {
        ProcedureDefinitionImage image = createImage(clientFileName);
        uploadImage(image, contentType, bytes);
    }

    private ProcedureDefinitionImage uploadImage(ProcedureDefinitionImage image, String contentType, byte[] imageData) {
        return s3Service.uploadTempProcedureDefImage(image, contentType, imageData);
    }

    private Component createEditorPanel() {
        if(annotationType.equals(AnnotationType.ARROW_STYLE)) {
            return new ArrowStyleAnnotationEditor("imageEditor"){
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
        } else
            return new CallOutStyleAnnotationEditor("imageEditor"){
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
    }

    private Component createEditorPanel(ImageAnnotation annotation) {
        if(annotationType.equals(AnnotationType.ARROW_STYLE))
            return new ArrowStyleAnnotationEditor("imageEditor", annotation){
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
        else
            return new CallOutStyleAnnotationEditor("imageEditor", annotation){
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
    }

    private Component createEditorPanel(ProcedureDefinitionImage image) {
        if(annotationType.equals(AnnotationType.ARROW_STYLE))
            return new ArrowStyleAnnotationEditor("imageEditor", image) {
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
        else
            return new CallOutStyleAnnotationEditor("imageEditor", image){
                @Override
                protected ProcedureDefinitionImage retrieveCurrentImage() {
                    return currentImage;
                }

                @Override
                protected IsolationPoint retrieveIsolationPoint() {
                    return model.getObject();
                }
            };
    }


    /*
        Protected methods...
     */

    protected ImageList<ProcedureDefinitionImage> createImageList(String id) {
        return new ImageList<ProcedureDefinitionImage>(id, new ListModel<>(displayableImages())) {
            @Override
            protected void createImage(ListItem<ProcedureDefinitionImage> item) {
                if(annotationType.equals(AnnotationType.ARROW_STYLE)) {
                    item.add(new ArrowStyleAnnotatedSvg("image", item.getModelObject()).withNoAnnotations());
                } else {
                    item.add(new CallOutStyleAnnotatedSvg("image", Model.of(item.getDefaultModel())).withNoAnnotations().withScale(2.0));
                }
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        //We want to set this image to be equal to whatever is in the editor.
                        currentImage = item.getModelObject();
                        swapEditorPanel(target, createEditorPanel(item.getModelObject()));
                    }
                });
            }
        };
    }


    /*
        Abstracts...
     */

    protected abstract void doDone(AjaxRequestTarget target);

    //This needs to be implemented as an abstract method, because we don't know enough about the ProcDef here...
    protected abstract ProcedureDefinitionImage createImage(String clientFileName);

    protected abstract List<ProcedureDefinitionImage> displayableImages();
}
