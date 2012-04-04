package com.n4systems.fieldid.wicket.components.massupdate.asset;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
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

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;

public class ConfirmDeletePanel extends AbstractMassUpdatePanel {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private MassUpdateManager massUpdateManager;
	
	private String confirmation;
	private Button submitButton;

	public ConfirmDeletePanel(String id, final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id);
		this.previousPanel = previousPanel;
		
		Form<Void> confirmDeleteForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {			
				List<Long> assetIds = assetSearchCriteria.getObject().getSelection().getSelectedIds();
				try {
					Long results = massUpdateManager.deleteAssets(assetIds, getCurrentUser());
					assetSearchCriteria.getObject().getSelection().clear();
					setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
					info(new FIDLabelModel("message.asset_massdelete_successful", results.toString()).getObject());
				} catch (UpdateFailureException e) {
					e.printStackTrace();
				}
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

}
