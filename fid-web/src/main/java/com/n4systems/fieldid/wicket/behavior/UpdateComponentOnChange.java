package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class UpdateComponentOnChange extends AjaxFormComponentUpdatingBehavior {

    public UpdateComponentOnChange() {
        super("onchange");
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
    }

}
