package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeArchivedListPanel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeListPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PriorityCodePage extends FieldIDFrontEndPage {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    private AddActionTypeForm addActionTypeForm;
    private AjaxSubmitLink openFormButton;

    private AjaxLink viewAll;
    private AjaxLink viewArchived;
    private PriorityCodeListPanel priorityCodeListPanel;
    private PriorityCodeArchivedListPanel priorityCodeArchivedListPanel;

    public PriorityCodePage() {

        add(viewAll = new AjaxLink<Void>("viewAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                viewAll.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
                viewArchived.add(new AttributeModifier("class", "mattButtonRight"));
                priorityCodeListPanel.setVisible(true);
                priorityCodeArchivedListPanel.setVisible(false);
                target.add(viewAll);
                target.add(viewArchived);
                target.add(priorityCodeListPanel);
                target.add(priorityCodeArchivedListPanel);
            }
        });
        viewAll.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));

        add(viewArchived = new AjaxLink<Void>("viewArchived") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                viewArchived.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
                viewAll.add(new AttributeModifier("class", "mattButtonLeft"));
                priorityCodeListPanel.setVisible(false);
                priorityCodeArchivedListPanel.setVisible(true);
                target.add(viewAll);
                target.add(viewArchived);
                target.add(priorityCodeListPanel);
                target.add(priorityCodeArchivedListPanel);
            }
        });

        addActionTypeForm = new AddActionTypeForm("form", Model.of(new PriorityCode()));
        add(addActionTypeForm);
        Form openForm = new Form("openForm");
        openForm.add(openFormButton = new AjaxSubmitLink("openFormButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                addActionTypeForm.setFormVisible(target, true);
                target.appendJavaScript("translateWithin($('#" + addActionTypeForm.formContainer.getMarkupId() + "'), $('#" + openFormButton.getMarkupId() + "'), $('#pageContent'), " + 40 + ", " + -1063 + ");");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(openForm);


        add(priorityCodeListPanel = new PriorityCodeListPanel("priorityCodeList"));
        priorityCodeListPanel.setOutputMarkupPlaceholderTag(true);

        add(priorityCodeArchivedListPanel = new PriorityCodeArchivedListPanel("priorityCodeArchivedList"));
        priorityCodeArchivedListPanel.setOutputMarkupPlaceholderTag(true);
        priorityCodeArchivedListPanel.setVisible(false);
    }

    class AddActionTypeForm extends Form<PriorityCode> {

        FIDFeedbackPanel feedbackPanel;
        String name;
        public WebMarkupContainer formContainer;

        public AddActionTypeForm(String id, IModel<PriorityCode> model) {
            super(id, model);
            add(formContainer = new WebMarkupContainer("formContainer"));
            formContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            RequiredTextField nameField;
            formContainer.add(nameField = new RequiredTextField("name", new PropertyModel(this, "name")));
            nameField.add(new PriorityCodeUniqueNameValidator());

            formContainer.add(new AjaxSubmitLink("addActionTypeButton"){

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PriorityCode newPriorityCode = getModelObject();
                    newPriorityCode.setName(name);
                    newPriorityCode.setTenant(FieldIDSession.get().getTenant());

                    priorityCodeService.create(newPriorityCode);
                    setModelObject(new PriorityCode());
                    name = null;
                    target.add(priorityCodeListPanel);
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


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");

        response.renderCSSReference("style/newCss/setup/prioritycodes.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.priority_codes"));
    }
}
