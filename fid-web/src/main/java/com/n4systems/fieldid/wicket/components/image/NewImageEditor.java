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

    private EditableImageList<ProcedureDefinitionImage> images;
    private SvgImageDisplayPanel editor; //This becomes an editor by bolting some JavaScript to it.  It's otherwise just a display panel.
    private Form tinyForm;
    private FileUploadField fileUploadField;

    private List<FileUpload> fileUploads = new ArrayList<>();
    private IModel<IsolationPoint> model;
    private IModel<List<ProcedureDefinitionImage>> imageModel;
    private ProcedureDefinitionImage currentImage;

    private ArrowAnnotatingBehaviour ajaxBehavior;

    public NewImageEditor(String id, IModel<IsolationPoint> model, IModel<List<ProcedureDefinitionImage>> imageModel) {
        super(id, model);
        this.model = model;
        this.imageModel = imageModel;                                           //TODO Not sure if this is right either, we may need the real deal here...
        this.currentImage = (model.getObject().getAnnotation() == null ? null : (ProcedureDefinitionImage) model.getObject().getAnnotation().getImage());
    }

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
                if(currentAnnotation == null && id == null) {
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
                //FIXME Figure out how to handle this: Return Annotation when one exists, but return something that won't shit out a NPE when an annotation doesn't exist...
                return model.getObject().getAnnotation();
            }
        });

        if(currentImage == null) {
            editor = new SvgImageDisplayPanel("imageEditor");
        } else {
            editor = new SvgImageDisplayPanel("imageEditor", currentImage);
        }

        add(editor);
        editor.setOutputMarkupId(true);
        add(images = createImageList("imageGallery"));
        images.setOutputMarkupId(true);
        tinyForm = new Form("uploadForm");
        tinyForm.add(fileUploadField = new FileUploadField("fileUpload", new PropertyModel<>(this, "fileUploads")));
        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedImage = fileUploadField.getFileUpload();
                //Steps to handle image upload...
                //1) Take image and ram it up into S3
                handleUpload(uploadedImage.getBytes(), uploadedImage.getContentType(), uploadedImage.getClientFileName());

                //2) Determine Index... presumably the image list has been updated...
                //TODO Make sure we don't have to do any extra stuff to add the file to the list... it may not be automatic.
                switchToLastCarouselImage(target);

                doSubmit(target);
                target.add(images);
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

    private void switchToLastCarouselImage(AjaxRequestTarget target) {
        currentImage = imageModel.getObject().get(imageModel.getObject().size()-1);
        swapEditorPanel(target, new SvgImageDisplayPanel("imageEditor", currentImage));
    }

    private void swapEditorPanel(AjaxRequestTarget target, SvgImageDisplayPanel panel) {
        panel.setOutputMarkupId(true);
        editor.replaceWith(panel);
        target.add(panel);
        editor = panel;
        target.appendJavaScript(getEditorJS());
    }

    private void handleUpload(byte[] bytes, String contentType, String clientFileName) {
        ProcedureDefinitionImage image = createImage(clientFileName);
        uploadImage(image, contentType, bytes);
    }

    private void uploadImage(ProcedureDefinitionImage image, String contentType, byte[] imageData) {
        s3Service.uploadTempProcedureDefImage(image, contentType, imageData);
    }


    /*
        Overrides...
     */

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //TODO Add JS references and such...?
    }


    /*
        Protected methods...
     */

    protected EditableImageList<ProcedureDefinitionImage> createImageList(String id) {
        return new EditableImageList<ProcedureDefinitionImage>(id, imageModel) {
            @Override
            protected void createImage(ListItem<ProcedureDefinitionImage> item) {
                item.add(new ContextImage("image", s3Service.getProcedureDefinitionImageThumbnailURL(item.getModelObject()).toString()));
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        //We want to set this image to be equal to whatever is in the editor.
                        currentImage = item.getModelObject();
                        swapEditorPanel(target, new SvgImageDisplayPanel("imageEditor", item.getModelObject()));
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

    //TODO It's a LOT of functionality... we may end up wanting to break this apart into separate methods for image handling...
    protected abstract void doSubmit(AjaxRequestTarget target);

    protected abstract void doDone(AjaxRequestTarget target);

    //This needs to be implemented as an abstract method, because we don't know enough about the ProcDef here...
    protected abstract ProcedureDefinitionImage createImage(String clientFileName);
}
