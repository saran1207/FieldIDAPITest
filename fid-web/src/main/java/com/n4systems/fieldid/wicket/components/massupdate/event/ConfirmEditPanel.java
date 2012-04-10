package com.n4systems.fieldid.wicket.components.massupdate.event;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.MassUpdateEventsTask;

public class ConfirmEditPanel extends AbstractMassUpdatePanel {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private MassUpdateManager massUpdateManager;
	
	private MassUpdateEventModel massUpdateEventModel;

	public ConfirmEditPanel(String id, final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel, final MassUpdateEventModel massUpdateEventModel) {
		super(id, eventSearchCriteria);
		this.previousPanel = previousPanel;
		this.massUpdateEventModel = massUpdateEventModel;
		
		add(new Label("massEditMessage", new FIDLabelModel("message.mass_edit_confirm_details", 
				                                           eventSearchCriteria.getObject().getSelection().getNumSelectedIds(),
				                                           new FIDLabelModel("label.events.lc").getObject())));
		
		Form<Void> confirmEditForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				List<Long> eventScheduleIds = eventSearchCriteria.getObject().getSelection().getSelectedIds();
				MassUpdateEventsTask task = new MassUpdateEventsTask(massUpdateManager, eventScheduleIds, massUpdateEventModel.getEvent(), massUpdateEventModel.getSelect(), getCurrentUser());
				TaskExecutor.getInstance().execute(task);
				setResponsePage(new ReportPage(eventSearchCriteria.getObject()));
				info(new FIDLabelModel("message.massupdating", new FIDLabelModel("label.events").getObject()).getObject());
			}
		};
		
		confirmEditForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
		
		add(confirmEditForm);
	}
	
	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}
	
}
