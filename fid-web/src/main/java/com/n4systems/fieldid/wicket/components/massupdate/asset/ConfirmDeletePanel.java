package com.n4systems.fieldid.wicket.components.massupdate.asset;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ConfirmDeletePanel extends AbstractMassUpdatePanel {

    private static final Logger logger = Logger.getLogger(ConfirmDeletePanel.class);
	
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
                if (!isDeletedEntered()) {
                    error(getString("typeDeleteToContinue"));
                    return;
                }

				try {
                    List<Long> assetIds = assetSearchCriteria.getObject().getSelection().getSelectedIds();
                    Long results = massUpdateManager.deleteAssets(assetIds, getCurrentUser());
					assetSearchCriteria.getObject().getSelection().clear();
					setResponsePage(new SearchPage(assetSearchCriteria.getObject()));
					info(new FIDLabelModel("message.asset_massdelete_successful", results.toString()).getObject());
				} catch (UpdateFailureException e) {
                    error("Mass deletion failed. (Are one or more of the assets used in master events?)");
                    logger.error("Error with mass delete", e);
				}
			}
		};

		confirmDeleteForm.add(new TextField<String>("confirmationField", new PropertyModel<String>(this, "confirmation")));

		confirmDeleteForm.add(submitButton = new Button("submitButton"));
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

    private boolean isDeletedEntered() {
        return "delete".equalsIgnoreCase(confirmation);
    }
	
	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}
	
	@Override
	public boolean isConfirmPanel() {
		return true;
	}

}
