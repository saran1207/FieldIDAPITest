package com.n4systems.fieldid.wicket.pages.escalationrules;

import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssignmentEscalationRule;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;

/**
 * Created by rrana on 2015-08-26.
 */
public class EditRulePage extends FieldIDTemplatePage {

    @SpringBean
    AssignmentEscalationRuleService assignmentEscalationRuleService;

    private EventReportCriteria reportCriteria;
    private AssignmentEscalationRule rule;
    protected FIDFeedbackPanel feedbackPanel;

    private List<String> emailList;

    private Long oldDateRange;
    private String originalName;

    public EditRulePage(PageParameters params) {
        Long id = params.get("id").toLong();
        rule = assignmentEscalationRuleService.getRuleById(id);
        oldDateRange = rule.getOverdueQuantity();
        originalName = rule.getRuleName();
        add(new SaveRuleForm("saveRuleForm", null));
    }

    class SaveRuleForm extends Form {

        private RequiredTextField<String> nameText;
        private FidDropDownChoice<Long> overdueBy;

        public SaveRuleForm(String id, Long savedItemId) {
            super(id);

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            //Rule Name
            add(nameText = new RequiredTextField<String>("name", new PropertyModel<String>(rule, "ruleName")));

            //Overdue By
            overdueBy = new FidDropDownChoice<Long>("localDateRange", new PropertyModel<Long>(rule, "overdueQuantity"), RulesDateRange.getLongValues(), new CalculationChoiceRenderer());
            overdueBy.add(new UpdateComponentOnChange());
            overdueBy.setNullValid(false);
            overdueBy.setRequired(true);
            add(overdueBy);

            //Escalate To
            ExaminersModel usersModel = new ExaminersModel();

            VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
            AssignedUserOrGroupSelect assignedUserOrGroupSelect;

            add(assignedUserOrGroupSelect = new AssignedUserOrGroupSelect("escalate",
                    ProxyModel.of(rule, on(AssignmentEscalationRule.class).getEscalateUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)));
            assignedUserOrGroupSelect.setRequired(true);

            //Reassign To

            AssignedUserOrGroupSelect reassignTo;
            add(reassignTo = new AssignedUserOrGroupSelect("reassign",
                    ProxyModel.of(rule, on(AssignmentEscalationRule.class).getReassignUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)));


            add(new CheckBox("notifyAssignee", new PropertyModel<Boolean>(rule, "notifyAssignee")));
            //---------
            final WebMarkupContainer emailAddressesContainer = new WebMarkupContainer("emailAddressesContainer");
            emailAddressesContainer.setOutputMarkupPlaceholderTag(true);
            add(emailAddressesContainer);

            emailList = rule.getAdditionalEmailsList();
            if(emailList == null) {
                emailList = new ArrayList<>();
            }

            emailAddressesContainer.add(new ListView<String>("emailAddresses", new PropertyModel<List<String>>(EditRulePage.this, "emailList")) {
                @Override
                protected void populateItem(final ListItem<String> item) {
                    EmailTextField addressField = new EmailTextField("address", item.getModel());
                    addressField.add(new UpdateComponentOnChange() {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            target.add(feedbackPanel);
                        }
                    });
                    addressField.setRequired(true);
                    item.add(addressField);
                    item.add(new AjaxLink("deleteLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            emailList.remove(item.getIndex());
                            target.add(emailAddressesContainer);
                        }
                    });
                }
            });

            emailAddressesContainer.add(new AjaxLink("addEmailAddressLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    emailList.add("");
                    target.add(emailAddressesContainer);
                }
            });

            TextField<String> subjectTextField = new RequiredTextField<String>("subject", new PropertyModel<String>(rule, "subjectText"));
            subjectTextField.add(new StringValidator.MaximumLengthValidator(255));
            add(subjectTextField);

            TextArea<String> messageTextArea;
            add(messageTextArea = new TextArea<String>("message", new PropertyModel<String>(rule, "customMessageText")));
            messageTextArea.add(new StringValidator.MaximumLengthValidator(1024));

            add(new Link("cancelLink") {
                @Override public void onClick() {
                    setResponsePage(new ManageEscalationRules());
                }
            });
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            if(validateFields()) {
                assignmentEscalationRuleService.updateRule(rule, oldDateRange);
                setResponsePage(new ManageEscalationRules());
            }
        }

    }

    private boolean validateFields() {
        boolean valid = true;
        //Make sure Name is unique
        if(!originalName.equals(rule.getRuleName()) && assignmentEscalationRuleService.isNameUnique(rule.getRuleName())) {
            //make them type a new name
            error("A rule with this name already exists.  Please enter a unique name.");
            valid = false;
        }

        //tie the emailList object into the hibernate object.
        rule.setAdditionalEmails(null);
        for (String email : emailList) {
            rule.addAdditionalEmail(email);
        }

        return valid;
    }

    static class CalculationChoiceRenderer implements IChoiceRenderer<Long> {
        @Override
        public Object getDisplayValue(Long object) {
            return new FIDLabelModel(RulesDateRange.getLabelFor(object)).getObject();
        }

        @Override
        public String getIdValue(Long object, int index) {
            return RulesDateRange.getLabelFor(object);
        }
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_rule"));
    }

}
