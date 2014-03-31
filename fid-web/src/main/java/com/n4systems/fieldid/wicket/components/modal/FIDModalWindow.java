package com.n4systems.fieldid.wicket.components.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;


public class FIDModalWindow extends ModalWindow {

    public FIDModalWindow(String id) {
        super(id);
    }

    public FIDModalWindow(String id, IModel<?> model) {
        super(id, model);
    }

    public FIDModalWindow(String id, IModel<?> model, int width, int height) {
    	this(id,model);
        setResizable(false);
    	setInitialWidth(width);    	
    	setInitialHeight(height);
	}

    public FIDModalWindow withInitialSize(int width, int height) {
        setInitialHeight(height);
        setInitialWidth(width);
        return this;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setCssClassName(CSS_CLASS_GRAY);
        setMaskType(MaskType.SEMI_TRANSPARENT);
        setResizable(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/modal/fid_modal.css");
    }

}
