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
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.concurrent.Callable;

public class ConfirmEditPanel extends AbstractMassUpdatePanel {

	private static final Logger logger = Logger.getLogger(ConfirmEditPanel.class);
	
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
				final int eventCount = eventScheduleIds.size();
				NewRelic.addCustomParameter("Event mass update count", eventCount);
                final User modifiedBy = getCurrentUser();
                final String currentPlatform = ThreadLocalInteractionContext.getInstance().getCurrentPlatform();
                final PlatformType platformType = ThreadLocalInteractionContext.getInstance().getCurrentPlatformType();

				final Token newRelicToken = NewRelic.getAgent().getTransaction().getToken();
                AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {

					@Trace(async = true)
                    @Override
                    public Void call() throws Exception {

						newRelicToken.link(); // link this async thread to the main NewRelic transaction for reporting
						long startTime = System.nanoTime();

                        ThreadLocalInteractionContext.getInstance().setCurrentUser(modifiedBy);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatform(currentPlatform);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(platformType);

                        try {
							logger.info("Beginning Event mass update for " + eventCount + " events");
                            massUpdateEventService.updateEvents(eventScheduleIds, massUpdateEventModel.getEvent(), massUpdateEventModel.getSelect(), modifiedBy.getId());
							massUpdateEventService.sendSuccessEmailResponse(eventScheduleIds, modifiedBy);
							long endTime = System.nanoTime();
							logger.info("Event mass update finished for " + eventCount + " assets and took " + ((endTime-startTime) / 1000000) + " ms");
                        } catch (Exception e) {
							logger.error(e.getMessage(), e);
                            massUpdateEventService.sendFailureEmailResponse(eventScheduleIds, modifiedBy);
                        } finally {
							ThreadLocalInteractionContext.getInstance().clear();
							newRelicToken.expire();
						}
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
