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
        // CAVEAT : be careful here.  if the call to getDefaultModelObject() throws an exception, it
        //  will then subsequently call toString() when re-throwing in the catch block.
        // toString() then calls isVisible() which sends you one step closer to stack overflow.
        // suggested? do your own model getting and handle exceptions yourself.
        if (getDefaultModelObject() == null) {
            return false;
        }
        return super.isVisible();
    }

}
