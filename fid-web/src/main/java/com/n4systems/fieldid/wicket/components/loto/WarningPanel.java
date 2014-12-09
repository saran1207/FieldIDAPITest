package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
    private List<WarningTemplate> choices = new ArrayList<>();

    private WarningTemplate selectWarnings = new WarningTemplate("Select Warning(s)", "");

    public WarningPanel(String id, final IModel<String> model) {
        super(id);
        this.model = model;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        choices.add(selectWarnings);

        choices.addAll(warningTemplateService.getAllTemplatesForTenant());

        onChangeBehaviours.add(this);

        if(choices.size() > 1) {
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
        select.setVisible(choices.size() > 1);
        getSelect().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                fireOnChange(target);
            }
        });
//        getSelect().setNullValid(true);
        //Not sure if this trick is necessary at initial load when the component is set to Null being valid.
        getSelect().setDefaultModelObject(selectWarnings);
        select.setOutputMarkupId(true);

        add(select);
    }

    @Override
    public void onEvent(AjaxRequestTarget target) {
        WarningTemplate warning = (WarningTemplate) select.getDefaultModelObject();
        if(warning != null && !warning.getName().equalsIgnoreCase(selectWarnings.getName())) {
            addWarning(warning);
            //While not explicitly required, it would be ideal to have the ability to select the same option twice in a
            //row, mainly because it feels like clumsy functionality not to be able to... as such, we force the
            //DropDownChoice component to set its default model as null.  This forces a blank line into the current
            //selection, making the component look like it did before the
            getSelect().setDefaultModelObject(selectWarnings);

            //We need to do this in case there is an error on the page, very strange...
            text.clearInput();

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
