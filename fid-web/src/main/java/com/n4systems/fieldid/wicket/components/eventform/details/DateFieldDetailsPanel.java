package com.n4systems.fieldid.wicket.components.eventform.details;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.model.DateFieldCriteria;

public class DateFieldDetailsPanel extends Panel {

    public DateFieldDetailsPanel(String id, final IModel<DateFieldCriteria> model) {
        super(id, model);
        
        add(new AjaxCheckBox("includeTimeCheckbox", new PropertyModel<Boolean>(model, "includeTime")){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
                // We don't need a callback, we just need the underlying model updated on change
                // This could be an ajaxformcomponentupdatingbehavior
			}});
    }

}
