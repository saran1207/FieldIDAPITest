package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.ArrowStyleAnnotationJsonRenderer;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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

    private final static String IMAGE_EDITOR_ENABLE_JS = "arrowAnnotationEditor.init(%s, %s)";

    @SpringBean
    private ArrowStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private AtomicLongService atomicLongService;

    private ImageList<ProcedureDefinitionImage> images;
    private ArrowStyleAnnotatedSvg editor; //This becomes an editor by bolting some JavaScript to it.  It's otherwise just a display panel.
    private FileUploadField fileUploadField;
    private Form tinyForm;
    private WebMarkupContainer editorAndGalleryContainer;

    private List<FileUpload> fileUploads = new ArrayList<>();
    private IModel<IsolationPoint> model;
    private ProcedureDefinitionImage currentImage;

    private ArrowStyleAnnotatingBehaviour ajaxBehavior;

    private final static Logger logger = Logger.getLogger(ArrowStyleEditorAndGalleryPanel.class);

    public ArrowStyleEditorAndGalleryPanel(String id, IModel<IsolationPoint> model) {
        super(id, model);
        this.model = model;
        this.currentImage = (model.getObject().getAnnotation() == null ? null : (ProcedureDefinitionImage) model.getObject().getAnnotation().getImage());
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
        if(currentImage == null) {
            //Creating the panel without an image leaves a placeholder where the image would otherwise be.
            editor = new ArrowStyleAnnotatedSvg("imageEditor");

            //...but the placeholder is junk... so instead we hide the editor and build a ContextImage to hold as placeholder.
        } else {
            //When have an annotation, we pass that in to be able to display the associated image (if there is one)
            //or the placeholder if there is not an image.
            editor = new ArrowStyleAnnotationEditor("imageEditor", model.getObject().getAnnotation()){
                @Override
                protected String createEditorInitJS() {
                    return String.format(EDITOR_JS_CALL,
                                         this.getMarkupId(),
                                         getEditorJSON());
                }
            }.withNoAnnotations();
        }

        editor.add(ajaxBehavior = createAnnotatingBehaviour());

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

    private ArrowStyleAnnotatingBehaviour createAnnotatingBehaviour() {
        return new ArrowStyleAnnotatingBehaviour() {
            @Override
            protected void processAnnotation(ImageAnnotation annotation) {
                annotation.setText(model.getObject().getIdentifier());
                model.getObject().setAnnotation(annotation);
            }

            @Override
            protected ProcedureDefinitionImage getEditedImage() {
                return currentImage;
            }

            @Override
            protected ImageAnnotation getImageAnnotation(Double x, Double y, Double x2, Double y2) {
                ImageAnnotation currentAnnotation = model.getObject().getAnnotation();
                if(currentAnnotation == null) {
                    //Must be a new annotation... so make a new one.
                    currentAnnotation = new ImageAnnotation(x, y, x2, y2, "", ImageAnnotationType.fromIsolationPointSourceType(model.getObject().getSourceType()));
                    currentAnnotation.setTempId(atomicLongService.getNext());
                    currentAnnotation.setTenant(FieldIDSession.get().getTenant());
                }
                currentAnnotation.setY(y);
                currentAnnotation.setX(x);
                currentAnnotation.setX_tail(x2);
                currentAnnotation.setY_tail(y2);
                currentAnnotation.setType(ImageAnnotationType.fromIsolationPointSourceType(model.getObject().getSourceType()));
                currentAnnotation.setText("");

                return currentAnnotation;
            }

            @Override
            protected ImageAnnotation getAnnotation() {
                return model.getObject().getAnnotation();
            }
        };
    }

    private void switchToLastCarouselImage(AjaxRequestTarget target) {
        currentImage = displayableImages().get(displayableImages().size() - 1);

        //Oh, we should probably set this as the image for the annotation, no?
        if(model.getObject().getAnnotation() == null) {
            model.getObject().setAnnotation(new ImageAnnotation());
        }
        model.getObject().getAnnotation().setImage(currentImage);

        //If you're switching the image, then your annotation should no longer be shown... right?
        swapEditorPanel(target, new ArrowStyleAnnotationEditor("imageEditor", currentImage) {
            @Override
            protected String createEditorInitJS() {
                return String.format(EDITOR_JS_CALL,
                                     this.getMarkupId(),
                                     getEditorJSON());
            }
        }.withNoAnnotations());
    }

    private void swapEditorPanel(AjaxRequestTarget target, ArrowStyleAnnotatedSvg panel) {
        panel.setOutputMarkupId(true);

        //Since we're going to move the ajax behaviour, we need to remove it...
        editor.remove(ajaxBehavior);

        //...then destroy it...
        ajaxBehavior = null;

        //...then replace the panel...
        editor.replaceWith(panel);

        //...then finally add a newly created ajax behaviour.
        panel.add(ajaxBehavior = createAnnotatingBehaviour());
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

    private String getEditorJSON() {
        return jsonRenderer.render(model.getObject().getAnnotation() == null ?
                ajaxBehavior.getEmptyEditorParams() :
                ajaxBehavior.getEditorParams());
    }


    /*
        Protected methods...
     */

    protected ImageList<ProcedureDefinitionImage> createImageList(String id) {
        return new ImageList<ProcedureDefinitionImage>(id, new ListModel<>(displayableImages())) {
            @Override
            protected void createImage(ListItem<ProcedureDefinitionImage> item) {
                item.add(new ArrowStyleAnnotatedSvg("image", item.getModelObject()).withNoAnnotations());
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        //We want to set this image to be equal to whatever is in the editor.
                        currentImage = item.getModelObject();
                        swapEditorPanel(target, new ArrowStyleAnnotationEditor("imageEditor", item.getModelObject()){
                            @Override
                            protected String createEditorInitJS() {
                                return String.format(EDITOR_JS_CALL,
                                                     this.getMarkupId(),
                                                     getEditorJSON());
                            }
                        }.withNoAnnotations());
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
