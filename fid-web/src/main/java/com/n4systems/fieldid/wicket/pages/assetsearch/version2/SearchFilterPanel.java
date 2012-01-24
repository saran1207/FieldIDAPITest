package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.AssetDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.DateRangePicker2;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.OwnershipCriteriaPanel;
import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.utils.DateRange;



@SuppressWarnings("serial")
public class SearchFilterPanel extends AbstractConfigPanel {

	public SearchFilterPanel(String id, Model<AssetSearchCriteriaModel> model, final Mediator mediator) {
		super(id,model, mediator);
	}
	
	@Override
	protected void updateMenu(Component... components ) {
		for (Component c:components) { 
			if (c.getId().equals("filters")) {
				c.add(new AttributeAppender("class", new Model<String>("disabled"), " "));
			} else if (c.getId().equals("columns")) { 
				c.add(createShowConfigBehavior("columns"));
			}						
		}
	}

	@Override
	protected Form<AssetSearchCriteriaModel> createForm(String id,
			Model<AssetSearchCriteriaModel> model) {
		return new FilterForm(id,model);				
	}
	
	protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}
	
	// ------------------------------------------------------------------------------------------------------
	
    public class FilterForm extends Form<AssetSearchCriteriaModel> {

      	public FilterForm(String id, final IModel<AssetSearchCriteriaModel> model) {
        	super(id, new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
          	setOutputMarkupId(true);

			CollapsiblePanel p1 = new CollapsiblePanel("identifiers", new StringResourceModel("label.identifiers",this,null), EXPAND_IMG, COLLAPSE_IMG) {
				@Override protected Panel createContainedPanel(String id) {
					return new IdentifiersCriteriaPanel(id, model);
				}			
			};
		  	add(p1);		  	

			CollapsiblePanel p2 = new CollapsiblePanel("assetDetailsCriteriaPanel", new StringResourceModel("label.asset_details",this,null), EXPAND_IMG, COLLAPSE_IMG) {
				@Override protected Panel createContainedPanel(String id) {
					return new AssetDetailsCriteriaPanel(id,model) {
						@Override protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
							SearchFilterPanel.this.onAssetTypeOrGroupUpdated(target, selectedAssetType, availableAssetTypes);
		       			}
					};				
				}
			};
		  	add(p2);

			CollapsiblePanel p3 = new CollapsiblePanel("ownershipCriteriaPanel", new StringResourceModel("label.ownership",this,null), EXPAND_IMG, COLLAPSE_IMG) {
				@Override protected Panel createContainedPanel(String id) {
					return new OwnershipCriteriaPanel(id, model);
				}
			};
		  	add(p3);
		  	
			CollapsiblePanel p4 = new CollapsiblePanel("orderDetailsCriteriaPanel", new StringResourceModel("label.orderdetails",this,null), EXPAND_IMG, COLLAPSE_IMG) {
				@Override protected Panel createContainedPanel(String id) {
					return new OrderDetailsCriteriaPanel(id);
				}
			};
		  	add(p4);		  	

			CollapsiblePanel p5 = new CollapsiblePanel("dateRangePicker", new StringResourceModel("label.daterange",this,null), EXPAND_IMG, COLLAPSE_IMG) {
				@Override 	protected Panel createContainedPanel(String id) {
					return new DateRangePicker2(id,new PropertyModel<DateRange>(model, "dateRange"));
				}			
			};
		  	add(p5);		  	  			
      	}

		@Override
      	protected void onSubmit() {
          	getModelObject().setReportAlreadyRun(true);
          	getModelObject().getSelection().clear();
	          //---make this refresh results object;
      	}
      	
  }

}
