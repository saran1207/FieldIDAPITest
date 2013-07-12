package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeArchivedListPanel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeListPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.PriorityCodeAutoScheduleType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;

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
                target.add(viewAll, viewArchived, priorityCodeListPanel, priorityCodeArchivedListPanel);
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
                target.add(viewAll, viewArchived, priorityCodeListPanel, priorityCodeArchivedListPanel);
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

    class AddPriorityCodeForm extends Form<PriorityCode> {

        FIDFeedbackPanel feedbackPanel;
        String name;
        PriorityCodeAutoScheduleType autoScheduleType;
        Integer autoScheduleCustomDays;
        public WebMarkupContainer formContainer;

        public AddPriorityCodeForm(String id, IModel<PriorityCode> model) {
            super(id, model);
            add(formContainer = new WebMarkupContainer("formContainer"));
            formContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            RequiredTextField<String> nameField;
            formContainer.add(nameField = new RequiredTextField<String>("name", new PropertyModel<String>(this, "name")));
            nameField.add(new PriorityCodeUniqueNameValidator());

            final TextField<Integer> customDaysField = new TextField<Integer>("autoScheduleCustomDays", new PropertyModel<Integer>(this, "autoScheduleCustomDays"));
            final WebMarkupContainer customDaysContainer = new WebMarkupContainer("customDaysContainer");
            customDaysContainer.setOutputMarkupPlaceholderTag(true).setVisible(autoScheduleType == PriorityCodeAutoScheduleType.CUSTOM);
            customDaysContainer.add(customDaysField);
            formContainer.add(customDaysContainer);

            formContainer.add(new DropDownChoice<PriorityCodeAutoScheduleType>("autoSchedule", new PropertyModel<PriorityCodeAutoScheduleType>(this, "autoScheduleType"), Arrays.asList(PriorityCodeAutoScheduleType.values()), new ListableLabelChoiceRenderer<PriorityCodeAutoScheduleType>()) {
                {
                    setNullValid(true);
                    add(new UpdateComponentOnChange() {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            autoScheduleCustomDays = null;
                            customDaysContainer.setVisible(PriorityCodeAutoScheduleType.CUSTOM.equals(autoScheduleType));
                            target.add(customDaysContainer);
                        }
                    });
                }
            });

            formContainer.add(new AjaxSubmitLink("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (autoScheduleType == PriorityCodeAutoScheduleType.CUSTOM && (autoScheduleCustomDays == null || autoScheduleCustomDays < 1 || autoScheduleCustomDays > 1000)) {
                        error(getString("message.invalid_custom_days"));
                        target.add(feedbackPanel);
                        return;
                    }
                    PriorityCode newPriorityCode = getModelObject();
                    newPriorityCode.setName(name);
                    newPriorityCode.setTenant(FieldIDSession.get().getTenant());
                    newPriorityCode.setAutoSchedule(autoScheduleType);
                    newPriorityCode.setAutoScheduleCustomDays(autoScheduleCustomDays);

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
        response.renderCSSReference("style/newCss/priority/priority_codes.css");

        response.renderCSSReference("style/newCss/setup/prettyItemList.css");

        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new PriorityCodeTitleLabel(labelId);
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.priority_codes"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
}
