package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.dashboard.UnusedWidgetsModel;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AddWidgetPanel extends Panel {

    private IModel<DashboardLayout> currentLayoutModel;

    public AddWidgetPanel(String id, IModel<DashboardLayout> currentLayoutModel) {
        super(id);
        this.currentLayoutModel = currentLayoutModel;
        setOutputMarkupId(true);

        add(new AddWidgetForm("addWidgetForm"));
    }

    class AddWidgetForm extends Form {

        private WidgetType selectedType;

        public AddWidgetForm(String id) {
            super(id);

            final UnusedWidgetsModel unusedWidgetsModel = new UnusedWidgetsModel(currentLayoutModel);
            add(new DropDownChoice<WidgetType>("widgetTypeSelect", new PropertyModel<WidgetType>(this, "selectedType"), unusedWidgetsModel, new ListableChoiceRenderer<WidgetType>()));
            add(new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (selectedType != null) {
                        onWidgetTypeSelected(target, selectedType);
                        unusedWidgetsModel.detach();
                        target.addComponent(AddWidgetPanel.this);
                        selectedType = null;
                    }
                }
            });
        }
    }

    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) { }

}
