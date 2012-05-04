package com.n4systems.fieldid.wicket.pages.saveditems.send;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.sendsearch.SendSearchService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.components.timezone.FrequencyDropDownChoice;
import com.n4systems.fieldid.wicket.components.timezone.HourOfDaySelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.StringAppendModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunLastSearchPage;
import com.n4systems.fieldid.wicket.pages.saveditems.ManageSavedItemsPage;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.common.ReportFormat;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class SendSavedItemPage extends FieldIDFrontEndPage {

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private AsyncService asyncService;

    @SpringBean
    private SendSearchService sendSearchService;

    private SavedItem savedItem;
    private IModel<? extends SearchCriteria> criteria;
    private Page returnToPage;

    private static final int SEND_NOW_STATE = 1;
    private static final int SCHEDULE_STATE = 2;
    
    private int currentState;
    private boolean scheduleStateAvailable = false;

    private AjaxSubmitLink submitLink;
    private Long savedReportId;

    public SendSavedItemPage(IModel<? extends SearchCriteria> criteria, Page returnToPage) {
        this.criteria = criteria;
        savedReportId = criteria.getObject().getSavedReportId();
        this.returnToPage = returnToPage;
        if (criteria.getObject().getSavedReportId() != null) {
            initializeSavedItem(criteria.getObject().getSavedReportId());
        } else {
            currentState = SEND_NOW_STATE;
            scheduleStateAvailable = false;
        }
    }

    public SendSavedItemPage(PageParameters params) {
        super(params);

        long savedItemId = params.get("id").toLong();
        initializeSavedItem(savedItemId);
    }

    private void initializeSavedItem(long savedItemId) {
        savedItem = persistenceService.find(SavedItem.class, savedItemId);
        criteria = new Model<SearchCriteria>(savedItem.getSearchCriteria());
        currentState = SCHEDULE_STATE;
        scheduleStateAvailable = true;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        SendSavedItemSchedule sendSavedItemSchedule = new SendSavedItemSchedule();
        prepopulateSubject(sendSavedItemSchedule);
        add(new SendSavedItemForm("form", new Model<SendSavedItemSchedule>(sendSavedItemSchedule)));
    }

    private void prepopulateSubject(SendSavedItemSchedule sendSavedItemSchedule) {
        if (criteria.getObject() instanceof AssetSearchCriteria) {
            sendSavedItemSchedule.setSubject("FieldID: Search");
        } else if (criteria.getObject() instanceof EventReportCriteria) {
            sendSavedItemSchedule.setSubject("Field ID: Report");
        }
    }

    class SendSavedItemForm extends Form<SendSavedItemSchedule> {

        public SendSavedItemForm(String id, final IModel<SendSavedItemSchedule> model) {
            super(id, model);
            
            // We want to see a blank field for an extra email address to send to if there aren't any yet
            if (model.getObject().getEmailAddresses().isEmpty()) {
                model.getObject().getEmailAddresses().add("");
            }
            
            final FIDFeedbackPanel feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
            add(feedbackPanel);
            
            final WebMarkupContainer scheduleFieldsContainer = new WebMarkupContainer("scheduleFieldsContainer");
            scheduleFieldsContainer.setOutputMarkupPlaceholderTag(true);

            scheduleFieldsContainer.add(new FrequencyDropDownChoice("frequency", new PropertyModel<SimpleFrequency>(model, "frequency")));
            scheduleFieldsContainer.add(new HourOfDaySelect("hourToSend", new PropertyModel<Integer>(model, "hourToSend")));
            scheduleFieldsContainer.setVisible(currentState == SCHEDULE_STATE);
            
            add(scheduleFieldsContainer);

            MattBar bar = new MattBar("bar") {
                @Override
                protected void onEnterState(AjaxRequestTarget target, Object state) {
                    currentState = (Integer)state;
                    scheduleFieldsContainer.setVisible(state.equals(SCHEDULE_STATE));
                    target.add(scheduleFieldsContainer, submitLink);
                }
            };
            bar.setVisible(scheduleStateAvailable);

            bar.addLink(new FIDLabelModel("label.send_now"), SEND_NOW_STATE);
            bar.addLink(new FIDLabelModel("label.schedule_future_runs"), SCHEDULE_STATE);
            bar.setCurrentState(currentState);
            add(bar);

            final WebMarkupContainer emailAddressesContainer = new WebMarkupContainer("emailAddressesContainer");
            emailAddressesContainer.setOutputMarkupPlaceholderTag(true);
            add(emailAddressesContainer);

            emailAddressesContainer.add(new ListView<String>("emailAddresses", new PropertyModel<List<String>>(model, "emailAddresses")) {
                @Override
                protected void populateItem(final ListItem<String> item) {
                    TextField<String> addressField = new TextField<String>("address", item.getModel());
                    addressField.add(new UpdateComponentOnChange() {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            target.add(feedbackPanel);
                        }
                    });
                    ValidationBehavior.addValidationBehaviorToComponent(addressField);
                    addressField.add(new StringValidator.MaximumLengthValidator(255));
                    item.add(addressField);
                    item.add(new AjaxLink("deleteLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            model.getObject().getEmailAddresses().remove(item.getIndex());
                            target.add(emailAddressesContainer);
                        }
                    });
                }
            });

            emailAddressesContainer.add(new CheckBox("sendToOwner", new PropertyModel<Boolean>(model, "sendToOwner")));

            emailAddressesContainer.add(new AjaxLink("addEmailAddressLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    model.getObject().getEmailAddresses().add("");
                    target.add(emailAddressesContainer);
                }
            });
            
            TextField<String> subjectTextField = new RequiredTextField<String>("subject", new PropertyModel<String>(model, "subject"));
            subjectTextField.add(new StringValidator.MaximumLengthValidator(255));
            add(subjectTextField);
            
            TextArea<String> messageTextArea;
            add(messageTextArea = new TextArea<String>("message", new PropertyModel<String>(model, "message")));
            messageTextArea.add(new StringValidator.MaximumLengthValidator(1024));
            
            DropDownChoice<ReportFormat> formatSelect = new DropDownChoice<ReportFormat>("format", new PropertyModel<ReportFormat>(model,"reportFormat"), Arrays.asList(ReportFormat.values()), new ListableLabelChoiceRenderer<ReportFormat>());
            formatSelect.setNullValid(false);
            add(formatSelect);
            
            add(submitLink = new AjaxSubmitLink("scheduleNowLink") {
                { setOutputMarkupId(true); }
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    final SendSavedItemSchedule sendItemSchedule = model.getObject();
                    sendItemSchedule.clearBlankEmailAddresses();
                    if (validateEmailAddresses(sendItemSchedule)) {
                        sendItemSchedule.setUser(getCurrentUser());
                        sendItemSchedule.setTenant(getCurrentUser().getTenant());
                        sendItemSchedule.setSavedItem(savedItem);

                        if (currentState == SCHEDULE_STATE) {
                            persistenceService.save(sendItemSchedule);
                            FieldIDSession.get().info("Successfully saved schedule");
                            getRequestCycle().setResponsePage(ManageSavedItemsPage.class);
                        } else {
                            AsyncService.AsyncTask<Object> task = asyncService.createTask(new Callable<Object>() {
                                @Override
                                public Object call() throws Exception {
                                    if (savedReportId != null) {
                                        sendSearchService.sendSavedItem(savedReportId, sendItemSchedule);
                                    } else {
                                        sendSearchService.sendSearch(criteria.getObject(), sendItemSchedule);
                                    }
                                    
                                    return null;
                                }
                            });
                            asyncService.run(task);
                            FieldIDSession.get().info("Email generation in progress");
                            getRequestCycle().setResponsePage(RunLastSearchPage.class);
                        }
                    } else {
                        target.add(feedbackPanel);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });

            add(new Link("cancelLink") {
                @Override
                public void onClick() {
                    if (returnToPage != null) {
                        setResponsePage(returnToPage);
                    } else {
                        setResponsePage(ManageSavedItemsPage.class);
                    }
                }
            });

            submitLink.add(new FlatLabel("submitLabel", createCurrentSubmitLabelModel()));
        }

        // Would like to validate email addresses in a standard way -- but this has an issue
        // If the user enters an invalid email address, then clicks the add button,
        // the input with the bad email address will clear (actually they will all clear unless they're updated on change)
        // This solves the issue.
        private boolean validateEmailAddresses(SendSavedItemSchedule sendItemSchedule) {
            EmailAddressValidator emailAddressValidator = EmailAddressValidator.getInstance();
            boolean goodAddresses = true;
            
            if (sendItemSchedule.getEmailAddresses().size() == 0 && !sendItemSchedule.isSendToOwner()) {
                error(new FIDLabelModel("message.at_least_one_email_address_required").getObject());
                return false;
            }
            
            for (String address : sendItemSchedule.getEmailAddresses()) {
                if (!emailAddressValidator.getPattern().matcher(address).matches()) {
                    goodAddresses = false;
                    error("Invalid email address: " + address);
                }
            }
            return goodAddresses;
        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/saved_item/send_saved_item.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, createTitleModel());
    }
    
    public IModel<String> createCurrentSubmitLabelModel() {
        return new IModel<String>() {
            @Override
            public String getObject() {
                if (currentState == SCHEDULE_STATE) {
                    return new FIDLabelModel("label.schedule_now").getObject();
                }
                return new FIDLabelModel("label.send_now").getObject();
            }

            @Override
            public void setObject(String object) {
            }
            @Override
            public void detach() {
            }
        };
    }

    public IModel<String> createTitleModel() {
        return new IModel<String>() {
            @Override
            public String getObject() {
                if (savedItem == null) {
                    return new FIDLabelModel("label.email").getObject();
                } else {
                    return new StringAppendModel(" / ", new FIDLabelModel("label.email"), new PropertyModel<String>(savedItem, "name")).getObject();
                }
            }

            @Override
            public void setObject(String object) {
            }
            @Override
            public void detach() {
            }
        };
    }

}
