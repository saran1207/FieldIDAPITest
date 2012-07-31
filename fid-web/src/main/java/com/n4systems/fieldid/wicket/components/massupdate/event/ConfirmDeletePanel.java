package com.n4systems.fieldid.wicket.components.massupdate.event;

import com.n4systems.ejb.EventManager;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.Event;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmDeletePanel extends AbstractMassUpdatePanel {
	
	@SpringBean private UserService userService;

    @SpringBean private PersistenceService persistenceService;

    @SpringBean private EventService eventService;
    
	@SpringBean	private EventManager eventManager;
	
	private String confirmation;
	private Button submitButton;

	public ConfirmDeletePanel(String id, final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id);
		this.previousPanel = previousPanel;
		
		Form<Void> confirmDeleteForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {		
                
                List<Long> eventIds =   eventSearchCriteria.getObject().getSelection().getSelectedIds();
                for (Long id: eventIds) {
                    Event event = persistenceService.find(Event.class, id);
                    eventManager.retireEvent(event, getCurrentUser().getId());
                }
                eventSearchCriteria.getObject().getSelection().clear();
                setResponsePage(new ReportPage((EventReportCriteria)eventSearchCriteria.getObject()));
                info(new FIDLabelModel("message.event_massdelete_successful", eventIds.size()).getObject());
			}
		};

		TextField<String> input;
		
		confirmDeleteForm.add(input = new RequiredTextField<String>("confirmationField", new PropertyModel<String>(this, "confirmation")));
		
		input.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Matcher matcher = Pattern.compile("delete", Pattern.CASE_INSENSITIVE).matcher(ConfirmDeletePanel.this.confirmation);
				if(matcher.matches()) {
					submitButton.setEnabled(true);
					target.add(submitButton);
				}else {
					submitButton.setEnabled(false);
					target.add(submitButton);
				}
			}
		});
		
		confirmDeleteForm.add(submitButton = new Button("submitButton"));
		submitButton.setEnabled(false);
		submitButton.setOutputMarkupId(true);
		confirmDeleteForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
		
		add(confirmDeleteForm);
		
		add(new FIDFeedbackPanel("feedbackPanel"));
	}
	
	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}
	
	@Override
	public boolean isConfirmPanel() {
		return true;
	}

}
