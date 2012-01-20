package com.n4systems.fieldid.wicket.components.massupdate;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.user.User;

public class ConfirmDeletePanel extends Panel {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private MassUpdateManager massUpdateManager;
	
	private Panel previousPanel;
	
	private String confirmation;

	public ConfirmDeletePanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria, Panel previousPanel) {
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
		
		input.add(new PatternValidator(Pattern.compile("delete", Pattern.CASE_INSENSITIVE)));
			
		confirmDeleteForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
		
		add(confirmDeleteForm);
		
		add(new FIDFeedbackPanel("feedbackPanel"));
	}
	
	protected void onCancel() {};

	public Panel getPreviousPanel() {
		return previousPanel;
	}
	
	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}

}
