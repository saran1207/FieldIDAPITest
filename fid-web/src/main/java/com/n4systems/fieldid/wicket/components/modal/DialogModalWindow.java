package com.n4systems.fieldid.wicket.components.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class DialogModalWindow extends ModalWindow {

    public DialogModalWindow(String id) {
        super(id);
    }

    public DialogModalWindow(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setCssClassName("dialog-modal");
        setMaskType(MaskType.SEMI_TRANSPARENT);
        setResizable(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScript("Wicket.Window.unloadConfirmation=false;", "DISABLE_UNLOAD_CONFIRM");
        response.renderCSSReference("style/modal/fid_modal.css");
        response.renderCSSReference("style/modal/dialog_modal.css");
    }

}
