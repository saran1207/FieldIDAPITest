package com.n4systems.fieldid.wicket.components.massupdate.event;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.event.MassUpdateEventService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.model.PlatformType;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.concurrent.Callable;

public class ConfirmEditPanel extends AbstractMassUpdatePanel {
	
    @SpringBean
    private AsyncService asyncService;

    @SpringBean
    private MassUpdateEventService massUpdateEventService;
	
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
				final List<Long> eventScheduleIds = eventSearchCriteria.getObject().getSelection().getSelectedIds();
                final User modifiedBy = getCurrentUser();
                final String currentPlatform = ThreadLocalInteractionContext.getInstance().getCurrentPlatform();
                final PlatformType platformType = ThreadLocalInteractionContext.getInstance().getCurrentPlatformType();

                AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ThreadLocalInteractionContext.getInstance().setCurrentUser(modifiedBy);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatform(currentPlatform);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(platformType);

                        try {
                            massUpdateEventService.updateEvents(eventScheduleIds, massUpdateEventModel.getEvent(), massUpdateEventModel.getSelect(), modifiedBy.getId());
                        } catch (Exception e) {
                            massUpdateEventService.sendFailureEmailResponse(eventScheduleIds, modifiedBy);
                        }
                            massUpdateEventService.sendSuccessEmailResponse(eventScheduleIds, modifiedBy);

                        ThreadLocalInteractionContext.getInstance().clear();
                        return null;
                    }
                });
                asyncService.run(task);
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

	@Override
	public boolean isConfirmPanel() {
		return true;
	}
	
}
