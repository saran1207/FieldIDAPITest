package com.n4systems.fieldid.wicket.components.massupdate;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.user.User;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.MassUpdateAssetsTask;

public class ConfirmEditPanel extends Panel {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private MassUpdateManager massUpdateManager;
	
	private Panel previousPanel;
	
	private MassUpdateAssetModel massUpdateAssetModel;

	public ConfirmEditPanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria, Panel previousPanel, final MassUpdateAssetModel massUpdateAssetModel) {
		super(id, assetSearchCriteria);
		this.previousPanel = previousPanel;
		this.massUpdateAssetModel = massUpdateAssetModel;
		
		add(new Label("massEditMessage", new FIDLabelModel("message.mass_edit_confirm_details", assetSearchCriteria.getObject().getSelection().getNumSelectedIds())));
		
		Form<Void> confirmEditForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				List<Long> assetIds = assetSearchCriteria.getObject().getSelection().getSelectedIds();
				MassUpdateAssetsTask task = new MassUpdateAssetsTask(massUpdateManager, assetIds, massUpdateAssetModel.getAsset(), massUpdateAssetModel.getSelect(), getCurrentUser(), getNonIntegrationOrderNumber());
				TaskExecutor.getInstance().execute(task);
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
				info(new FIDLabelModel("message.assetmassupdating").getObject());
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
	
	protected void onCancel() {};

	public Panel getPreviousPanel() {
		return previousPanel;
	}

	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}
	
	private String getNonIntegrationOrderNumber() {
		return massUpdateAssetModel.getAsset().getNonIntergrationOrderNumber();
	}

}
