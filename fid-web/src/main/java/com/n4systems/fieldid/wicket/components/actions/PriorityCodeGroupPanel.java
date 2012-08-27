package com.n4systems.fieldid.wicket.components.actions;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PriorityCodeGroupPanel extends Panel {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    private AddActionTypeForm addActionTypeForm;
    private AjaxButton openFormButton;

    public PriorityCodeGroupPanel(String id) {
        super(id);


        ListView<PriorityCode> actionTypes = new ListView<PriorityCode>("actionType", createActionTypesModel()) {
            @Override
            protected void populateItem(ListItem<PriorityCode> item) {
                item.add(new Label("name", new PropertyModel<PriorityCode>(item.getModel(), "name")));
            }
        };
        add(actionTypes);

        addActionTypeForm = new AddActionTypeForm("form", Model.of(new PriorityCode()));
        add(addActionTypeForm);
        Form openForm = new Form("openForm");
        openForm.add(openFormButton = new AjaxButton("openFormButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                addActionTypeForm.setFormVisible(target, true);
                target.appendJavaScript("translateWithin($('#" + addActionTypeForm.formContainer.getMarkupId() + "'), $('#" + openFormButton.getMarkupId() + "'), $('#pageContent'), " + -40 + ", " + -960 + ");");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(openForm);
    }

    class AddActionTypeForm extends Form<PriorityCode> {

        FIDFeedbackPanel feedbackPanel;
        String name;
        public WebMarkupContainer formContainer;

        public AddActionTypeForm(String id, IModel<PriorityCode> model) {
            super(id, model);
            add(formContainer = new WebMarkupContainer("formContainer"));
            formContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            formContainer.add(new RequiredTextField("name", new PropertyModel(this, "name")));
            
            formContainer.add(new AjaxSubmitLink("addActionTypeButton"){

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PriorityCode newPriorityCode = getModelObject();
                    newPriorityCode.setName(name);
                    newPriorityCode.setTenant(FieldIDSession.get().getTenant());

                    priorityCodeService.create(newPriorityCode);
                    setModelObject(new PriorityCode());
                    name = null;
                    target.add(PriorityCodeGroupPanel.this);
                    setFormVisible(target, false);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            
            formContainer.add(new AjaxLink<Void>("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    name = null;
                    setFormVisible(target, false);
                }
            });

            formContainer.setVisible(false);
            formContainer.setOutputMarkupPlaceholderTag(true);

        }

        public void setFormVisible(AjaxRequestTarget target, boolean visible) {
            formContainer.setVisible(visible);
            target.add(formContainer);
        }
    }



   private LoadableDetachableModel<List<PriorityCode>> createActionTypesModel() {
       return new LoadableDetachableModel<List<PriorityCode>>() {
           @Override
           protected List<PriorityCode> load() {
               return priorityCodeService.getActivePriorityCodes();
           }
       };
   }
}
