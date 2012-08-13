package com.n4systems.fieldid.wicket.components.massupdate.event;

import com.n4systems.fieldid.service.massupdate.MassUpdateService;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.EventRemovalSummary;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class DeleteDetailsPanel extends AbstractMassUpdatePanel {
	
	@SpringBean 
	private MassUpdateService massUpdateService;

	public DeleteDetailsPanel(String id, IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id);

		this.previousPanel = previousPanel;

        EventRemovalSummary removalSummary = massUpdateService.calculateEventRemovalSummary(eventSearchCriteria.getObject().getSelection().getSelectedIds());
		
		add(new Label("deleteDetailsMessage", new FIDLabelModel("message.mass_delete_details", 
				                                                eventSearchCriteria.getObject().getSelection().getNumSelectedIds(),
                                                                new FIDLabelModel("label.events.lc").getObject())));
        Integer eventsToDelete = removalSummary.getMasterEventsToDelete() + removalSummary.getStandardEventsToDelete();
		add(new Label("eventsToDelete", eventsToDelete.toString()));
		add(new Label("schedulesToDelete", removalSummary.getEventSchedulesToDelete().toString()));

		
		Form<Void> deleteDetailsForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				onNext();
			}
		};
		
		add(deleteDetailsForm);
		
		deleteDetailsForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
	}

	protected void onNext() {};
	
	@Override
	public boolean isDetailsPanel() {
		return true;
	}

}
