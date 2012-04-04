package com.n4systems.fieldid.wicket.components.massupdate.asset;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.viewhelpers.handlers.PublishedState;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.IEventBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.PublishedStateChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;

public class EditDetailsPanel extends AbstractMassUpdatePanel {
	
	public EditDetailsPanel(String id, IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id, assetSearchCriteria);
		this.previousPanel = previousPanel;
						
		add(new Label("editDetailsMessage", new FIDLabelModel("message.mass_edit_details", 
						assetSearchCriteria.getObject().getSelection().getNumSelectedIds())));
		
		MassUpdateAssetForm form;
		add(form = new MassUpdateAssetForm("form", new MassUpdateAssetModel(FieldIDSession.get().getTenant().getId())) {
			@Override
			protected void onSubmit() {
				
				MassUpdateAssetModel model = (MassUpdateAssetModel) this.getDefaultModelObject();
				
				if(model.getSelect().containsValue(true)) {
					String errorMessage = checkRequiredFields(model);
					if(errorMessage.isEmpty()) {
						onNext((MassUpdateAssetModel) this.getDefaultModelObject());						
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

	private String checkRequiredFields(MassUpdateAssetModel model) {
		String errorMessage = "";
		
		if(model.getSelect().get("identified") && model.getAsset().getIdentified() == null) {
			return "error.identifiedrequired";
		}
		
		if(model.getSelect().get("owner") && model.getAsset().getOwner() == null) {
			return "error.ownerrequiredmassupdate";
		}
				
		if(Boolean.TRUE.equals(model.getSelect().get("nonIntegrationOrderNumber")) && model.getAsset().getNonIntergrationOrderNumber() == null) {
			model.getAsset().setNonIntergrationOrderNumber("");
		}
		
		return errorMessage;
	}
	
	protected void onNext(MassUpdateAssetModel massUpdateAssetModel) {};

	@SuppressWarnings("serial")
	class MassUpdateAssetForm extends Form<MassUpdateAssetModel> {

		public MassUpdateAssetForm(String id, MassUpdateAssetModel massUpdateAssetModel) {
			super(id, new CompoundPropertyModel<MassUpdateAssetModel>(massUpdateAssetModel));

			CheckBox assignedUserCheck = new CheckBox("assignedUserCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[assignedUser]"));
			GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", 
					new PropertyModel<User>(massUpdateAssetModel,	"asset.assignedUser"), new GroupedUsersForTenantModel());
			groupedUserPicker.setNullValid(true);
			groupedUserPicker.add(createCheckOnChangeEvent(assignedUserCheck));
			WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedUserContainer");
			add(assignedUserContainer);
			assignedUserContainer.add(assignedUserCheck);
			assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
			assignedUserContainer.add(groupedUserPicker);
			
			final CheckBox ownerCheck = new CheckBox("ownerCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[owner]"));
			ownerCheck.setOutputMarkupId(true);
			OrgPicker ownerPicker = new OrgPicker("owner", new PropertyModel<BaseOrg>(massUpdateAssetModel, "asset.owner")) {
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
			
			CheckBox assetStatusCheck = new CheckBox("assetStatusCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[assetStatus]"));
			FormComponent<AssetStatus> assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(massUpdateAssetModel, "asset.assetStatus"), 
					new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()).setNullValid(true);
			assetStatus.add(createCheckOnChangeEvent(assetStatusCheck));
			add(assetStatusCheck);
			add(assetStatus);
			
			CheckBox purchaseOrderCheck = new CheckBox("purchaseOrderCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[purchaseOrder]"));
			TextField<String> purchaseOrder = new TextField<String>("purchaseOrder", new PropertyModel<String>(massUpdateAssetModel, "asset.purchaseOrder"));
			purchaseOrder.add(createCheckOnChangeEvent(purchaseOrderCheck));
			add(purchaseOrderCheck);
			add(purchaseOrder);
			
			CheckBox nonIntegrationOrderNumberCheck = new CheckBox("nonIntegrationOrderNumberCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[nonIntegrationOrderNumber]"));
			TextField<String> nonIntergrationOrderNumber = new TextField<String>("nonIntergrationOrderNumber", new PropertyModel<String>(massUpdateAssetModel, "asset.nonIntergrationOrderNumber"));
			nonIntergrationOrderNumber.add(createCheckOnChangeEvent(nonIntegrationOrderNumberCheck));
			WebMarkupContainer nonIntergrationOrderNumberContainer = new WebMarkupContainer("nonIntergrationOrderNumberContainer");
			nonIntergrationOrderNumberContainer.setVisible(!FieldIDSession.get().getSecurityGuard().isIntegrationEnabled());
			nonIntergrationOrderNumberContainer.add(nonIntegrationOrderNumberCheck);
			nonIntergrationOrderNumberContainer.add(nonIntergrationOrderNumber);
			add(nonIntergrationOrderNumberContainer);

			final CheckBox locationCheck = new CheckBox("locationCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[location]"));
			locationCheck.setOutputMarkupId(true);
			LocationPicker location = new LocationPicker("location", new PropertyModel<Location>(massUpdateAssetModel, "asset.advancedLocation")){
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
			
			CheckBox identifiedCheck = new CheckBox("identifiedCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[identified]"));
			DateTimePicker identified = new DateTimePicker("identified", new PropertyModel<Date>(massUpdateAssetModel, "asset.identified"));
			identified.getDateTextField().add(createCheckOnChangeEvent(identifiedCheck));			
			add(identifiedCheck);
			add(identified);

			List<PublishedState> publishedStates = Arrays.asList(PublishedState.values());
			CheckBox publishedCheck = new CheckBox("publishedCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[published]"));
			FormComponent<PublishedState> published = new DropDownChoice<PublishedState>("published", new PropertyModel<PublishedState>(massUpdateAssetModel, "publishedState"),
					publishedStates, new PublishedStateChoiceRenderer());
			published.add(createCheckOnChangeEvent(publishedCheck));
			add(publishedCheck);
			add(published);
			
			final CheckBox commentCheck = new CheckBox("commentCheck", new PropertyModel<Boolean>(massUpdateAssetModel, "select[comments]"));
			commentCheck.setOutputMarkupId(true);
			Comment comment = new Comment("comment", new PropertyModel<String>(massUpdateAssetModel,"asset.comments"));
			comment.addChangeBehavior(new IEventBehavior() {
				@Override public void onEvent(AjaxRequestTarget target) {
					updateCheckbox(commentCheck, target);
				} 				
			});
			add(commentCheck);
			add(comment);			
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

}
