package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;



public class SearchFilterPanel extends Panel {

	public SearchFilterPanel(String id, final IModel<AssetSearchCriteria> model) {
		super(id,model);
		setOutputMarkupId(true);
		
		CollapsiblePanel p1 = new CollapsiblePanel("identifiers", new StringResourceModel("label.identifiers",this,null)) {
			@Override protected Panel createContainedPanel(String id) {
				return new IdentifiersCriteriaPanel(id, model);
			}			
		};
	  	add(p1);		  	

		CollapsiblePanel p2 = new CollapsiblePanel("assetDetailsCriteriaPanel", new StringResourceModel("label.asset_details",this,null) ) {
			@Override protected Panel createContainedPanel(String id) {
				return new AssetDetailsCriteriaPanel(id,model) {
					@Override protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
						SearchFilterPanel.this.onAssetTypeOrGroupUpdated(target, selectedAssetType, availableAssetTypes);
	       			}
				};				
			}
		};
	  	add(p2);

		CollapsiblePanel p3 = new CollapsiblePanel("ownershipCriteriaPanel", new StringResourceModel("label.ownership",this,null)) {
			@Override protected Panel createContainedPanel(String id) {
				return new OwnershipCriteriaPanel<>(id, model);
			}
		};
	  	add(p3);

		CollapsiblePanel p4 = new CollapsiblePanel("lockoutTagoutCriteriaPanel", new StringResourceModel("label.lockout_tagout", this, null)) {
			@Override
			protected Panel createContainedPanel(String id) {
				return new LockoutTagoutCriteriaPanel(id, model);
			}
		};
		//p4.setVisible(FieldIDSession.get().getSecurityGuard().isLotoEnabled());
		add(p4);

		CollapsiblePanel p5 = new CollapsiblePanel("orderDetailsCriteriaPanel", new StringResourceModel("label.orderdetails",this,null)) {
			@Override protected Panel createContainedPanel(String id) {
				return new OrderDetailsCriteriaPanel(id);
			}
		};
        p5.setHideWhenContainedPanelInvisible(true);
	  	add(p5);

		CollapsiblePanel p6 = new CollapsiblePanel("dateRangePicker", new StringResourceModel("label.daterange",this,null)) {
			@Override 	protected Panel createContainedPanel(String id) {
				return new DateRangePicker(id,new PropertyModel<DateRange>(model, "dateRange"));
			}			
		};
	  	add(p6);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("style/legacy/newCss/component/simple-advanced-search.css");
		super.renderHead(response);
	}
	
						
	
	protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}

}
