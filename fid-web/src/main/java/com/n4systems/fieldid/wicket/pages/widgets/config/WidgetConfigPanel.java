package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

@SuppressWarnings("serial")
public class WidgetConfigPanel<T extends WidgetConfiguration> extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    private ConfigForm configForm;

    public WidgetConfigPanel(String id, IModel<T> configModel) {
        super(id);
        add(configForm = new ConfigForm("configForm", configModel));
    }

    class ConfigForm extends Form {

        FIDFeedbackPanel feedbackPanel;

        public ConfigForm(String id, final IModel<T> model) {
            super(id);
            setOutputMarkupId(true);

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            TextField<String> nameField;
            add(nameField = new TextField<String>("name", new PropertyModel<String>(model, "name")));

            nameField.add(new StringValidator.MaximumLengthValidator(255));

            add(new AjaxButton("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    persistenceService.update(getWidgetConfigurationToSave(model));
                    target.addComponent(WidgetConfigPanel.this.getParent().getParent());
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
                }
            });
            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                   target.addComponent(WidgetConfigPanel.this.getParent());
                }
            });
        }

    }

    protected void addConfigElement(Component component) {
    	if (component==null) { 
    		return;
    	}
        configForm.add(component);
    }

    protected T getWidgetConfigurationToSave(final IModel<T> model) {
    	return model.getObject();
    }
    
}
