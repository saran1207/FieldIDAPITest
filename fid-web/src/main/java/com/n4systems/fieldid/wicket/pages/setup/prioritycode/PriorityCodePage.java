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
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PriorityCodePage extends FieldIDFrontEndPage {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    private AddPriorityCodeForm addPriorityCodeForm;
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
                target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");

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
                target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
            }
        });

        addPriorityCodeForm = new AddPriorityCodeForm("form", Model.of(new PriorityCode()));
        add(addPriorityCodeForm);
        Form openForm = new Form("openForm");
        openForm.add(openFormButton = new AjaxSubmitLink("openFormButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                addPriorityCodeForm.setFormVisible(target, true);
                target.appendJavaScript("translateWithin($('#" + addPriorityCodeForm.formContainer.getMarkupId() + "'), $('#" + openFormButton.getMarkupId() + "'), $('#pageContent'), " + 40 + ", " + -1085 + ");");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(openForm);

        ContextImage tooltip;
        
        add(tooltip = new ContextImage("tooltip", "images/tooltip-icon.png"));
        tooltip.add(new AttributeAppender("title", new FIDLabelModel("msg.priority_codes")));

        add(priorityCodeListPanel = new PriorityCodeListPanel("priorityCodeList"));
        priorityCodeListPanel.setOutputMarkupPlaceholderTag(true);

        add(priorityCodeArchivedListPanel = new PriorityCodeArchivedListPanel("priorityCodeArchivedList"));
        priorityCodeArchivedListPanel.setOutputMarkupPlaceholderTag(true);
        priorityCodeArchivedListPanel.setVisible(false);
    }

    class AddPriorityCodeForm extends Form<PriorityCode> {

        FIDFeedbackPanel feedbackPanel;
        String name;
        public WebMarkupContainer formContainer;

        public AddPriorityCodeForm(String id, IModel<PriorityCode> model) {
            super(id, model);
            add(formContainer = new WebMarkupContainer("formContainer"));
            formContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            RequiredTextField nameField;
            formContainer.add(nameField = new RequiredTextField("name", new PropertyModel(this, "name")));
            nameField.add(new PriorityCodeUniqueNameValidator());

            formContainer.add(new AjaxSubmitLink("saveButton"){

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
                    target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
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

        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.priority_codes"));
    }
}
