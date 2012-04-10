package com.n4systems.fieldid.wicket.components.massupdate.asset;

import java.util.List;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.MassUpdateAssetsTask;

public class ConfirmEditPanel extends AbstractMassUpdatePanel {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private MassUpdateManager massUpdateManager;
	
	private MassUpdateAssetModel massUpdateAssetModel;

	public ConfirmEditPanel(String id, final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel, final MassUpdateAssetModel massUpdateAssetModel) {
		super(id, assetSearchCriteria);
		this.previousPanel = previousPanel;
		this.massUpdateAssetModel = massUpdateAssetModel;
		
		add(new Label("massEditMessage", new FIDLabelModel("message.mass_edit_confirm_details", 
				                                           assetSearchCriteria.getObject().getSelection().getNumSelectedIds(), 
				                                           new FIDLabelModel("label.assets.lc").getObject())));
		
		Form<Void> confirmEditForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				List<Long> assetIds = assetSearchCriteria.getObject().getSelection().getSelectedIds();
				MassUpdateAssetsTask task = new MassUpdateAssetsTask(massUpdateManager, assetIds, massUpdateAssetModel.getAsset(), massUpdateAssetModel.getSelect(), getCurrentUser(), getNonIntegrationOrderNumber());
				TaskExecutor.getInstance().execute(task);
				setResponsePage(new SearchPage(assetSearchCriteria.getObject()));
				info(new FIDLabelModel("message.massupdating", new FIDLabelModel("label.assets").getObject()).getObject());
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
	
	private User getCurrentUser() {
		return userService.getUser( FieldIDSession.get().getSessionUser().getId());
	}
	
	private String getNonIntegrationOrderNumber() {
		return massUpdateAssetModel.getAsset().getNonIntergrationOrderNumber();
	}

}
