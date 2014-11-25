package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jheath on 14-11-25.
 */
public class Warning extends Panel implements IEventBehavior {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    private WarningTemplate warning;

    private List<IEventBehavior> onChangeBehaviours = new ArrayList<>();

    private IModel<String> model;

    private DropDownChoice<WarningTemplate> select;
    private TextArea<String> text;

    public Warning(String id, final IModel<String> model) {
        super(id);
        this.model = model;
        onChangeBehaviours.add(this);

        WarningsModel warningsModel = new WarningsModel();

        text = new TextArea<>("warningsText", model);
        text.setOutputMarkupId(true);
        select = new DropDownChoice<>("warningsSelect", new PropertyModel<>(this, "warning"), warningsModel);
        select.setNullValid(true);
        select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                fireOnChange(target);
            }
        });

        text.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(text);
            }
        });

        add(text);
        add(select);
    }

    @Override
    public void onEvent(AjaxRequestTarget target) {
        WarningTemplate warning = select.getModel().getObject();
        if(warning != null) {
            addWarning(warning);
            text.setDefaultModel(model);
            target.add(text);
        }
    }

    public Warning addMaxLengthValidation(int maxLength) {
        text.add(new StringValidator.MaximumLengthValidator(maxLength));
        return this;
    }

    public DropDownChoice<WarningTemplate> getSelect() {
        return select;
    }

    private void addWarning(WarningTemplate warning) {
        String value = model.getObject();
        if(value == null) {
            value = "";
        } else {
            //TODO This is where you need to add those bullets.
            value = value.endsWith("\n") ? value : value +"\n";
        }
        model.setObject(value + warning.getWarning() + "\n");
    }

    private void fireOnChange(AjaxRequestTarget target) {
        for(IEventBehavior behaviour : onChangeBehaviours) {
            behaviour.onEvent(target);
        }
    }

    class WarningsModel extends LoadableDetachableModel<List<WarningTemplate>> {
        @Override
        protected List<WarningTemplate> load() {
            return warningTemplateService.getAllTemplatesForTenant();
        }
    }
}
