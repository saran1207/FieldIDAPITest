package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 * Created by jheath on 14-11-25.
 */
public class WarningPanel extends Panel implements IEventBehavior {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    private WarningTemplate warning;

    private List<IEventBehavior> onChangeBehaviours = new ArrayList<>();

    private IModel<String> model;

    private LabelledDropDown<WarningTemplate> select;
    private TextArea<String> text;
    private List<WarningTemplate> choices;

    public WarningPanel(String id, final IModel<String> model) {
        super(id);
        this.model = model;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        choices = warningTemplateService.getAllTemplatesForTenant();

        onChangeBehaviours.add(this);

        WarningsModel warningsModel = new WarningsModel();

        text = new TextArea<>("warningsText", model);
        text.setOutputMarkupId(true);
//        select = new DropDownChoice<>("warningsSelect", new PropertyModel<>(this, "warning"), warningsModel);

        select = new LabelledDropDown<WarningTemplate>("warningsSelect", "label.warning", Model.of(warning)) {
            @Override
            public List<WarningTemplate> getChoices() {
                return choices;
            }
        };
        select.setVisible(choices.size() > 0);
        getSelect().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                fireOnChange(target);
            }
        });

//        getSelect().setNullValid(true);

//        select.setLabel(new FIDLabelModel("label.warnings"));
//        FormComponentLabel label = new FormComponentLabel("label",select);
//        Label labelSpan = new Label("labelSpan", new FIDLabelModel("label.warnings"));
//        label.add(labelSpan.setRenderBodyOnly(true));
//        label.add(select);
//        add(label);

        select.setOutputMarkupId(true);
//        label.add(new AttributeModifier("for", Model.of(select.getMarkupId())));

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
    protected void onModelChanging() {
        super.onModelChanging();
    }

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
    }

    @Override
    protected void internalOnModelChanged() {
        super.internalOnModelChanged();
    }

    @Override
    public void onEvent(AjaxRequestTarget target) {
        //Hahaha.  this'll probably explode in a ball of fire...
        WarningTemplate warning = (WarningTemplate) select.getDefaultModelObject();
        if(warning != null) {
            addWarning(warning);
//            text.setDefaultModel(model);
//            getSelect().render();
            target.add(text);
            target.add(select);
        }
    }

    public WarningPanel addMaxLengthValidation(int maxLength) {
        text.add(new StringValidator.MaximumLengthValidator(maxLength));
        return this;
    }

    public FidDropDownChoice getSelect() {
        return (FidDropDownChoice)select.get("label").get("input");
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
