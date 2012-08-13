package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.MassCloseEventTask;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ConfirmClosePanel extends AbstractMassUpdatePanel{

    @SpringBean
    private MassUpdateManager massUpdateManager;

    private MassUpdateEventModel massUpdateEventModel;

    public ConfirmClosePanel(String id, final IModel<EventReportCriteria> searchCriteria, AbstractMassUpdatePanel previousPanel, final MassUpdateEventModel massUpdateEventModel) {
        super(id, searchCriteria);
        this.previousPanel = previousPanel;
        this.massUpdateEventModel = massUpdateEventModel;

       add(new Label("massCloseMessage", new FIDLabelModel("message.mass_close_confirm_details",
               searchCriteria.getObject().getSelection().getNumSelectedIds(),
               new FIDLabelModel("label.open_events").getObject())));

        Form<Void> confirmCloseForm = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                List<Long> eventScheduleIds = searchCriteria.getObject().getSelection().getSelectedIds();
                MassCloseEventTask task = new MassCloseEventTask(massUpdateManager, eventScheduleIds, massUpdateEventModel.getEvent(), getCurrentUser());
                TaskExecutor.getInstance().execute(task);
                setResponsePage(new ReportPage(searchCriteria.getObject()));
                info(new FIDLabelModel("message.massupdating", new FIDLabelModel("label.open_events").getObject()).getObject());
            }
        };

        confirmCloseForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});

        add(confirmCloseForm);
    }

    @Override
	public boolean isConfirmPanel() {
		return true;
	}

}
