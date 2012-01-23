package com.n4systems.fieldid.wicket.components.massupdate;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.massupdate.MassUpdateService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.util.AssetRemovalSummary;

public class DeleteDetailsPanel extends AbstractMassUpdatePanel{
	
	@SpringBean 
	private MassUpdateService massUpdateService;
	
	@SpringBean
	private AssetService assetService;
	
	public DeleteDetailsPanel(String id, IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		super(id);

		this.previousPanel = previousPanel;
		
		AssetRemovalSummary assetRemovalSummary = getRemovalSummary(assetSearchCriteria.getObject().getSelection().getSelectedIds());
		
		add(new Label("deleteDetailsMessage", new FIDLabelModel("message.mass_delete_details", 
				assetSearchCriteria.getObject().getSelection().getNumSelectedIds())));
		add(new Label("eventsToDelete", assetRemovalSummary.getEventsToDelete().toString()));
		add(new Label("schedulesToDelete", assetRemovalSummary.getSchedulesToDelete().toString()));
		add(new Label("subAssetsToDetach", assetRemovalSummary.getSubAssetsToDetach().toString()));
		add(new Label("projectToDetachFrom", assetRemovalSummary.getProjectToDetachFrom().toString()));
		
		Fragment detachFromMaster = new Fragment("detachSummary", "detachFromMaster", this);
		
		detachFromMaster.setVisible(assetRemovalSummary.isDetachFromMaster());
		
		add(detachFromMaster);
		
		
		Form<Void> deleteDetailsForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				onNext();
			}
		};
		
		add(deleteDetailsForm);
		
		deleteDetailsForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
	}

	protected void onNext() {};
	
	private AssetRemovalSummary getRemovalSummary(List<Long> selectedIds) {
		
		AssetRemovalSummary aggregateSummary = new AssetRemovalSummary(null);
				
		for (Long id: selectedIds) {
			Asset asset = assetService.getAsset(id);
			AssetRemovalSummary summary = massUpdateService.testArchive(asset);
			
			if (summary.getAssetUsedInMasterEvent() != 0L) {
				aggregateSummary.setAssetUsedInMasterEvent(summary.getAssetUsedInMasterEvent());
			} else if (summary.isDetachFromMaster()) {
				aggregateSummary.setDetachFromMaster(true);
			}
			aggregateSummary.setEventsToDelete(aggregateSummary.getEventsToDelete() + summary.getEventsToDelete());
			aggregateSummary.setSchedulesToDelete(aggregateSummary.getSchedulesToDelete() + summary.getSchedulesToDelete());
			aggregateSummary.setSubAssetsToDetach(aggregateSummary.getSubAssetsToDetach() + summary.getSubAssetsToDetach());
			aggregateSummary.setProjectToDetachFrom(aggregateSummary.getProjectToDetachFrom() + summary.getProjectToDetachFrom());
		}		
		return aggregateSummary;
	}
}
