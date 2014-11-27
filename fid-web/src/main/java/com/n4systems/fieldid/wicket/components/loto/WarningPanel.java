package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
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
 * This is the WarningTemplate Selection Panel.  This panel will render a DropDown menu that allows the user to select
 * from a set of preset Warning Templates... provided any are configured for that Tenant.  In the event there are no
 * Warning Templates, this DropDown menu will not render.
 *
 * Created by Jordan Heath on 14-11-25.
 */
public class WarningPanel extends Panel implements IEventBehavior {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    private WarningTemplate warning;

    private List<IEventBehavior> onChangeBehaviours = new ArrayList<>();

    private IModel<String> model;

    private LabelledDropDown<WarningTemplate> select;
    private LabelledTextArea<String> text;
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

        if(choices.size() > 0) {
            text = new LabelledTextArea<>("warningsText", " ", model);
            //In order to prettify this component, we want to bring these two closer together.  BUT we don't want to do
            //this when there are no WarningTemplates.  This is a quick and easy minor adjustment.
            text.add(new AttributeAppender("style", Model.of("margin-top: -12px"), "; "));
        } else {
            text = new LabelledTextArea<>("warningsText", "label.warnings", model);
        }
        getText().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(text);
            }
        });
        text.setOutputMarkupId(true);

        add(text);

        //In this particular case, you actually can't use the diamond operator. Surprise!!
        select = new LabelledDropDown<WarningTemplate>("warningsSelect", "label.warnings", Model.of(warning)) {
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
        select.setOutputMarkupId(true);

        add(select);
    }

    @Override
    public void onEvent(AjaxRequestTarget target) {
        WarningTemplate warning = (WarningTemplate) select.getDefaultModelObject();
        if(warning != null) {
            //Here, we add the warning, then set a blank "Choose One" selection (it gets ripped away when a selection
            //is made otherwise)
            addWarning(warning);
            getSelect().setDefaultModelObject(Model.of(new WarningTemplate("Choose One", "")));
            //Don't forget to add both components as targets for update.
            target.add(text);
            target.add(select);
        }
    }

    public FidDropDownChoice getSelect() {
        return (FidDropDownChoice)select.get("label").get("input");
    }

    public TextArea getText() {
        return (TextArea)text.get("label").get("input");
    }

    private void addWarning(WarningTemplate warning) {
        String value = model.getObject();
        if(value == null) {
            value = "";
        } else {
            value = value.endsWith("\n") ? value : value +"\n";
        }
        model.setObject(value + " • " + warning.getWarning() + "\n");
    }

    private void fireOnChange(AjaxRequestTarget target) {
        for(IEventBehavior behaviour : onChangeBehaviours) {
            behaviour.onEvent(target);
        }
    }
}
