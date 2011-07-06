package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

// A Label that always renders only its body.
// It sets itself invisible if its model is null, so it can be used more easily in enclosures
public class FlatLabel extends Label {

    public FlatLabel(String id) {
        super(id);
        setRenderBodyOnly(true);
    }

    public FlatLabel(String id, String label) {
        super(id, label);
        setRenderBodyOnly(true);
    }

    public FlatLabel(String id, IModel<?> model) {
        super(id, model);
        setRenderBodyOnly(true);
    }

    @Override
    public boolean isVisible() {
        if (getDefaultModelObject() == null) {
            return false;
        }
        return super.isVisible();
    }

}
