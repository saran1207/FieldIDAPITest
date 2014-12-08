package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.ArrowStyleAnnotationJsonRenderer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
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
public abstract class NewImageEditor extends Panel {

    private final static String IMAGE_EDITOR_ENABLE_JS = "fictionalJS.callAllTheThings(%s, %s)";

    @SpringBean
    private ArrowStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private AtomicLongService atomicLongService;

    private ImageList<ProcedureDefinitionImage> images;
    private ArrowStyleAnnotatedSvg editor; //This becomes an editor by bolting some JavaScript to it.  It's otherwise just a display panel.
    private FileUploadField fileUploadField;

    private List<FileUpload> fileUploads = new ArrayList<>();
    private IModel<IsolationPoint> model;
    private ProcedureDefinitionImage currentImage;

    private ArrowAnnotatingBehaviour ajaxBehavior;

    public NewImageEditor(String id, IModel<IsolationPoint> model) {
        super(id, model);
        this.model = model;                                                      //Not sure if this is right either, we may need the real deal here...
        this.currentImage = (model.getObject().getAnnotation() == null ? null : (ProcedureDefinitionImage) model.getObject().getAnnotation().getImage());
    }


    /*
        Overrides...
     */

    @Override
    public void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
        add(ajaxBehavior = new ArrowAnnotatingBehaviour() {
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
            protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2) {
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
        });

        if(currentImage == null) {
            //Creating the panel without an image leaves a placeholder where the image would otherwise be.
            editor = new ArrowStyleAnnotatedSvg("imageEditor");
        } else {
            //When have an annotation, we pass that in to be able to display the associated image (if there is one)
            //or the placeholder if there is not an image.
            editor = new ArrowStyleAnnotatedSvg("imageEditor", model.getObject().getAnnotation());
        }

        add(editor);
        editor.setOutputMarkupId(true);
        add(images = createImageList("imageGallery"));
        images.setOutputMarkupId(true);
        Form tinyForm = new Form("uploadForm");
        tinyForm.add(fileUploadField = new FileUploadField("fileUpload", new PropertyModel<>(this, "fileUploads")));
        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedImage = fileUploadField.getFileUpload();
                //Steps to handle image upload...
                //1) Take image and ram it up into S3
                handleUpload(uploadedImage.getBytes(), uploadedImage.getContentType(), uploadedImage.getClientFileName());

                //2) Now that the image is in S3 and our image model, refresh the image gallery/list thingy
                refreshGalleryPanel(target, createImageList("imageGallery"));

                //3) Determine Index... presumably the image list has been updated...
                //Not sure if extra work needs to be done to add the images to the list, or we just need to implement a
                //different Model class to carry the value properly.
                switchToLastCarouselImage(target);

                target.add(images);
                target.add(editor);
                target.add(NewImageEditor.this);

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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //TODO Add JS references and such...
    }


    /*
        Private methods...
     */

    private void switchToLastCarouselImage(AjaxRequestTarget target) {
        currentImage = displayableImages().get(displayableImages().size() - 1);

        //If you're switching the image, then your annotation should no longer be shown... right?
        swapEditorPanel(target, new ArrowStyleAnnotatedSvg("imageEditor", currentImage).withNoAnnotations());
    }

    private void swapEditorPanel(AjaxRequestTarget target, ArrowStyleAnnotatedSvg panel) {
        panel.setOutputMarkupId(true);
        editor.replaceWith(panel);
        target.add(panel);
        editor = panel;
        target.appendJavaScript(getEditorJS());
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


    /*
        Protected methods...
     */

    protected ImageList<ProcedureDefinitionImage> createImageList(String id) {
        return new ImageList<ProcedureDefinitionImage>(id, new ListModel<>(displayableImages())) {
            @Override
            protected void createImage(ListItem<ProcedureDefinitionImage> item) {
                item.add(new ContextImage("image", s3Service.getProcedureDefinitionImageThumbnailURL(item.getModelObject()).toString()));
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        //We want to set this image to be equal to whatever is in the editor.
                        currentImage = item.getModelObject();
                        swapEditorPanel(target, new ArrowStyleAnnotatedSvg("imageEditor", item.getModelObject()));
                    }
                });
            }
        };
    }

    protected String getEditorJS() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,editor.getMarkupId(),jsonRenderer.render(ajaxBehavior.getArrowAnnotatedImageOptions()));
    }


    /*
        Abstracts...
     */

    protected abstract void doDone(AjaxRequestTarget target);

    //This needs to be implemented as an abstract method, because we don't know enough about the ProcDef here...
    protected abstract ProcedureDefinitionImage createImage(String clientFileName);

    protected abstract List<ProcedureDefinitionImage> displayableImages();
}
