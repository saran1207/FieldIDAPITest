package com.n4systems.fieldid.wicket.components.massupdate.event;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventstatus.EventStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventStatus;
import com.n4systems.model.Status;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.Arrays;
import java.util.Date;

public class EditDetailsPanel extends AbstractMassUpdatePanel {
	
	public EditDetailsPanel(String id, IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id, eventSearchCriteria);
		this.previousPanel = previousPanel;
						
		add(new Label("editDetailsMessage", new FIDLabelModel("message.mass_edit_details",
                eventSearchCriteria.getObject().getSelection().getNumSelectedIds())));

        MassUpdateEventForm form;
		add(form = new MassUpdateEventForm("form", new MassUpdateEventModel()) {
			@Override
			protected void onSubmit() {
				
				MassUpdateEventModel model = (MassUpdateEventModel) this.getDefaultModelObject();
				model.convertDatePerformedToUTC(FieldIDSession.get().getSessionUser().getTimeZone());
				
				if(model.getSelect().containsValue(true)) {
					String errorMessage = checkRequiredFields(model);
					if(errorMessage.isEmpty()) {
						onNext((MassUpdateEventModel) this.getDefaultModelObject());						
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
		
		if(model.getSelect().get("owner") && model.getEvent().getOwner() == null) {
			return new FIDLabelModel("error.ownerrequiredmassupdate").getObject();
		}

		if(model.getSelect().get("performedBy") && model.getEvent().getPerformedBy() == null) {
			return new FIDLabelModel("errors.required",new FIDLabelModel("label.performed_by").getObject()).getObject();
		}
		
		if(model.getSelect().get("datePerformed") && model.getEvent().getDate() == null) {
			return new FIDLabelModel("errors.required",new FIDLabelModel("label.date_performed").getObject()).getObject();
		}
		
		if(model.getSelect().get("result") && model.getEvent().getStatus() == null) {
			return new FIDLabelModel("errors.required",new FIDLabelModel("label.result").getObject()).getObject();
		}
		
		return errorMessage;
	}
	
	protected void onNext(MassUpdateEventModel massUpdateEventModel) {};

	@SuppressWarnings("serial")
	class MassUpdateEventForm extends Form<MassUpdateEventModel> {

		public MassUpdateEventForm(String id, MassUpdateEventModel massUpdateEventModel) {
			super(id, new CompoundPropertyModel<MassUpdateEventModel>(massUpdateEventModel));

			CheckBox assignedUserCheck = new CheckBox("assignedUserCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[assignedUser]"));
			GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", 
					new PropertyModel<User>(massUpdateEventModel, "assignedTo"), new GroupedUsersForTenantModel());
			groupedUserPicker.setNullValid(true);
			groupedUserPicker.add(createCheckOnChangeEvent(assignedUserCheck));
			WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedUserContainer");
			add(assignedUserContainer);
			assignedUserContainer.add(assignedUserCheck);
			assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
			assignedUserContainer.add(groupedUserPicker);
			
			final CheckBox ownerCheck = new CheckBox("ownerCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[owner]"));
			ownerCheck.setOutputMarkupId(true);
			OrgPicker ownerPicker = new OrgPicker("owner", new PropertyModel<BaseOrg>(massUpdateEventModel, "event.owner")) {
				@Override
				protected void onPickerClosed(AjaxRequestTarget target) {
					IModel<Boolean> model = (IModel<Boolean>) ownerCheck.getDefaultModel();
					clearAllCheckboxes();
					model.setObject(true);
					target.add(ownerCheck);				
				}
			};
			add(ownerCheck);
			add(ownerPicker);

			final CheckBox locationCheck = new CheckBox("locationCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[location]"));
			locationCheck.setOutputMarkupId(true);
			LocationPicker location = new LocationPicker("location", new PropertyModel<Location>(massUpdateEventModel, "event.advancedLocation")){
				@SuppressWarnings("unchecked")
				@Override
				protected void onLocationPicked(AjaxRequestTarget target) {
					IModel<Boolean> model = (IModel<Boolean>) locationCheck.getDefaultModel();
					clearAllCheckboxes();
					model.setObject(true);
					target.add(locationCheck);
				}
			}.withRelativePosition();
			location.getFreeformLocationField().add(createCheckOnChangeEvent(locationCheck));			
			add(locationCheck);
			add(location);
            
            final CheckBox performedByCheck = new CheckBox("performedByCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[performedBy]"));
            performedByCheck.setOutputMarkupId(true);
            FormComponent<User> performedBy = new DropDownChoice<User>("performedBy",new PropertyModel<User>(massUpdateEventModel, "event.performedBy"), new UsersForTenantModel(), new ListableChoiceRenderer<User>()).setNullValid(true);
            performedBy.add(createCheckOnChangeEvent(performedByCheck));
            add(performedByCheck);
            add(performedBy);

            CheckBox datePerformedCheck = new CheckBox("datePerformedCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[datePerformed]"));
            DateTimePicker datePerformed = new DateTimePicker("datePerformed", new PropertyModel<Date>(massUpdateEventModel, "event.date"), true);
            datePerformed.getDateTextField().add(createCheckOnChangeEvent(datePerformedCheck));
            add(datePerformedCheck);
            add(datePerformed);

			final CheckBox eventBookCheck = new CheckBox("eventBookCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[eventBook]"));
            eventBookCheck.setOutputMarkupId(true);
            FormComponent<EventBook> eventBooks = new DropDownChoice<EventBook>("eventBook", new PropertyModel<EventBook>(massUpdateEventModel, "event.book"), 
            		new EventBooksForTenantModel().addNullOption(true).setOpenBooksOnly(true), 
            		new ListableChoiceRenderer<EventBook>()).setNullValid(true);
            eventBooks.add(createCheckOnChangeEvent(eventBookCheck));
            add(eventBookCheck);
            add(eventBooks);

            massUpdateEventModel.getEvent().setStatus(null);
            final CheckBox resultCheck = new CheckBox("resultCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[result]"));
            resultCheck.setOutputMarkupId(true);
            FormComponent<Status> results = new DropDownChoice<Status>("result", new PropertyModel<Status>(massUpdateEventModel, "event.status"), Arrays.asList(Status.values()), new StatusChoiceRenderer()).setNullValid(true);
            results.add(createCheckOnChangeEvent(resultCheck));
            add(resultCheck);
            add(results);

			final CheckBox commentCheck = new CheckBox("commentCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[comments]"));
			commentCheck.setOutputMarkupId(true);
			Comment comment = new Comment("comment", new PropertyModel<String>(massUpdateEventModel,"event.comments"));
			comment.addChangeBehavior(new IEventBehavior() {
				@Override public void onEvent(AjaxRequestTarget target) {
					updateCheckbox(commentCheck, target);
				} 				
			});
			add(commentCheck);
			add(comment);

            CheckBox assetStatusCheck = new CheckBox("assetStatusCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[assetStatus]"));
            FormComponent<AssetStatus> assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(massUpdateEventModel, "event.assetStatus"),
                    new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()).setNullValid(true);
            assetStatus.add(createCheckOnChangeEvent(assetStatusCheck));
            add(assetStatusCheck);
            add(assetStatus);
            
            CheckBox eventStatusCheck = new CheckBox("eventStatusCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[eventStatus]"));
            FormComponent<EventStatus> eventStatus = new DropDownChoice<EventStatus>("eventStatus", new PropertyModel<EventStatus>(massUpdateEventModel, "event.eventStatus"),
                    new EventStatusesForTenantModel(), new ListableChoiceRenderer<EventStatus>()).setNullValid(true);
            eventStatus.add(createCheckOnChangeEvent(eventStatusCheck));
            add(eventStatusCheck);
            add(eventStatus);

            CheckBox printableCheck = new CheckBox("printableCheck", new PropertyModel<Boolean>(massUpdateEventModel, "select[printable]"));
            FormComponent<Boolean> printable = new CheckBox("printable", new PropertyModel<Boolean> (massUpdateEventModel, "event.printable"));
            printable.add(createCheckOnChangeEvent(printableCheck));
            add(printableCheck);
            add(printable);

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
