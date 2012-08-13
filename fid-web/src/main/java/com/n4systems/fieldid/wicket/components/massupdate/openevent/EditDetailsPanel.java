package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.Date;

public class EditDetailsPanel extends AbstractMassUpdatePanel {
	
	public EditDetailsPanel(String id, IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id, eventSearchCriteria);
		this.previousPanel = previousPanel;
						
		add(new Label("editDetailsMessage", new FIDLabelModel("message.mass_edit_details",
                eventSearchCriteria.getObject().getSelection().getNumSelectedIds())));

        MassUpdateOpenEventsForm form;
		add(form = new MassUpdateOpenEventsForm("form", new MassUpdateEventModel()) {
			@Override
			protected void onSubmit() {
				
				MassUpdateEventModel model = (MassUpdateEventModel) this.getDefaultModelObject();
				model.convertDatePerformedToUTC(FieldIDSession.get().getSessionUser().getTimeZone());
				
				if(model.getSelect().containsValue(true)) {
					String errorMessage = checkRequiredFields(model);
					if(errorMessage.isEmpty()) {
						onNext(model);
					}
					else {
						error(new FIDLabelModel(errorMessage).getObject());
					}
				} else {
					error(new FIDLabelModel("error.mass_edit_details").getObject());
				}
			}
			
			@Override
			protected void onError() {
				clearAllCheckboxes();
			}
		});
				
		form.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
		
		add(new FIDFeedbackPanel("feedbackPanel"));
	}

	private String checkRequiredFields(MassUpdateEventModel model) {
		String errorMessage = "";
		
		if(model.getSelect().get("nextEventDate") && model.getEvent().getNextDate() == null) {
            return new FIDLabelModel("errors.required",new FIDLabelModel("label.nexteventdate").getObject()).getObject();
		}

		return errorMessage;
	}
	
	protected void onNext(MassUpdateEventModel massUpdateEventModel) {};

	@SuppressWarnings("serial")
	class MassUpdateOpenEventsForm extends Form<MassUpdateEventModel> {

		public MassUpdateOpenEventsForm(String id, MassUpdateEventModel massUpdateEventModel) {
			super(id, new CompoundPropertyModel<MassUpdateEventModel>(massUpdateEventModel));

            CheckBox nextEventDateCheck = new CheckBox("nextEventDateCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[nextEventDate]"));
            DateTimePicker nextEventDate = new DateTimePicker("nextEventDate", new PropertyModel<Date>(massUpdateEventModel, "event.nextDate"), true).withNoAllDayCheckbox();
            nextEventDate.getDateTextField().add(createCheckOnChangeEvent(nextEventDateCheck));
            add(nextEventDateCheck);
            add(nextEventDate);
		}

		
		protected void clearAllCheckboxes() {
			visitChildren(CheckBox.class, new IVisitor<CheckBox,Void>() {
				@Override
				public void component(CheckBox object, IVisit<Void> visit) {
					object.clearInput();
				}
			});
		}
		
	}
	
	private void updateCheckbox(final CheckBox checkBox, AjaxRequestTarget target) {
		IModel<Boolean> model = (IModel<Boolean>) checkBox.getDefaultModel();
		model.setObject(true);
		target.add(checkBox);
	}
	
	private Behavior createCheckOnChangeEvent(final CheckBox checkBox) {
		checkBox.setOutputMarkupId(true);
		return new AjaxEventBehavior("onchange") {
			@Override protected void onEvent(AjaxRequestTarget target) {
				updateCheckbox(checkBox, target);
			}

		};
		
	}
	
	@Override
	public boolean isDetailsPanel() {
		return true;
	}

}
