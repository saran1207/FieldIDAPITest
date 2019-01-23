package com.n4systems.fieldid.wicket.components.massupdate.asset;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.asset.MassUpdateAssetService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.model.PlatformType;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.concurrent.Callable;

public class ConfirmEditPanel extends AbstractMassUpdatePanel {

	private static final Logger logger = Logger.getLogger(ConfirmEditPanel.class);

	@SpringBean
	private UserService userService;

    @SpringBean
    private AsyncService asyncService;

    @SpringBean
    private MassUpdateAssetService massUpdateAssetService;
	
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
				final List<Long> assetIds = assetSearchCriteria.getObject().getSelection().getSelectedIds();
				final int assetCount = assetIds.size();
				NewRelic.addCustomParameter("Asset mass update count", assetCount);
                final User modifiedBy = getCurrentUser();
                final String currentPlatform = ThreadLocalInteractionContext.getInstance().getCurrentPlatform();
                final PlatformType platformType = ThreadLocalInteractionContext.getInstance().getCurrentPlatformType();

				final Token newRelicToken = NewRelic.getAgent().getTransaction().getToken();
                AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {

					@Trace(async = true)
                    @Override
                    public Void call() throws Exception {

						newRelicToken.link(); // link this async thread to the main NewRelic transaction for reporting
						long startTime = System.nanoTime();

                        ThreadLocalInteractionContext.getInstance().setCurrentUser(modifiedBy);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatform(currentPlatform);
                        ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(platformType);

                        try {
							logger.info("Beginning Asset mass update for " + assetCount + " assets");
                          	massUpdateAssetService.updateAssets(assetIds, massUpdateAssetModel.getAsset(), massUpdateAssetModel.getSelect(), modifiedBy, getNonIntegrationOrderNumber());
							massUpdateAssetService.sendSuccessEmailResponse(assetIds, modifiedBy);
							long endTime = System.nanoTime();
							logger.info("Asset mass update finished for " + assetCount + " assets and took " + ((endTime-startTime) / 1000000) + " ms");
                        } catch (Exception e) {
							logger.error("Asset mass update failed",e);
                            massUpdateAssetService.sendFailureEmailResponse(assetIds, modifiedBy);
                        }

                        ThreadLocalInteractionContext.getInstance().clear();
						newRelicToken.expire();
                        return null;
                    }
                });
                asyncService.run(task);
                ConfirmEditPanel.this.onSubmit();
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

	private String getNonIntegrationOrderNumber() {
		return massUpdateAssetModel.getAsset().getNonIntergrationOrderNumber();
	}
	
	@Override
	public boolean isConfirmPanel() {
		return true;
	}

}
