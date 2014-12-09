package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * An Extension of the SVG Display Panel with some JS bolted to the front of it...  You can maybe use this to edit
 * annotations and stuff.
 *
 * This is really just a wrapper for this panel that invokes the JS.
 *
 * Created by Jordan Heath on 14-12-09.
 */
public class ArrowStyleAnnotationEditor extends ArrowStyleAnnotatedSvg {
    protected String EDITOR_JS_CALL = "$('#%s').arrowAnnotationEditor(%s);";

    public ArrowStyleAnnotationEditor(String id, ImageAnnotation theAnnotation) {
        super(id, theAnnotation);
    }

    public ArrowStyleAnnotationEditor(String id) {
        super(id);
    }

    public ArrowStyleAnnotationEditor(String id, ProcedureDefinitionImage theImage) {
        super(id, theImage);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jquery-1.7.2.js");
        response.renderJavaScriptReference("javascript/arrowAnnotationEditor.js");
        response.renderOnDomReadyJavaScript(createEditorInitJS());
    }

    protected String createEditorInitJS() {
        return null;
    }
}
