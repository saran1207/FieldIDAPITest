package com.n4systems.fieldid.wicket.components.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

import com.n4systems.model.orgs.BaseOrg;


public class FIDModalWindow extends ModalWindow {

    public FIDModalWindow(String id) {
        super(id);
        initializeModalWindow();
    }

    public FIDModalWindow(String id, IModel<?> model) {
        super(id, model);
        initializeModalWindow();
    }

    public FIDModalWindow(String id, IModel<BaseOrg> model, int width, int height) {
    	this(id,model);
    	setInitialWidth(width);    	
    	setInitialHeight(height);
	}

	private void initializeModalWindow() {
        setCssClassName(CSS_CLASS_GRAY);
        setMaskType(MaskType.SEMI_TRANSPARENT);
        setResizable(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/modal/fid_modal.css");
    }
}
