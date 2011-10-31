package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.util.AjaxCallback;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WidgetConfigPanel<T extends WidgetConfiguration> extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    private AjaxCallback<Boolean> saveCallback;
    private ConfigForm configForm;

    public WidgetConfigPanel(String id, IModel<T> configModel, AjaxCallback<Boolean> saveCallback) {
        super(id);
        setOutputMarkupId(true);
        this.saveCallback = saveCallback;

        add(configForm = new ConfigForm("configForm", configModel));
    }

    class ConfigForm extends Form {

        public ConfigForm(String id, final IModel<T> model) {
            super(id);
            setOutputMarkupId(true);

            add(new TextField<String>("name", new PropertyModel<String>(model, "name")));
            add(new AjaxButton("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    persistenceService.update(model.getObject());
                    saveCallback.call(target, true);
                }
            });
            add(new AjaxButton("cancelButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    saveCallback.call(target, false);
                }
            });
        }

    }

    protected void addConfigElement(Component component) {
        configForm.add(component);
    }

}
