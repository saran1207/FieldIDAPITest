package com.n4systems.fieldid.wicket.components.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class FIDModalWindow extends ModalWindow {

    public FIDModalWindow(String id) {
        super(id);
        initializeModalWindow();
    }

    public FIDModalWindow(String id, IModel<?> model) {
        super(id, model);
        initializeModalWindow();
    }

    private void initializeModalWindow() {
        setCssClassName(CSS_CLASS_GRAY);
        setMaskType(MaskType.SEMI_TRANSPARENT);
        setResizable(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/modal/fid_modal.css");
    }
}
