package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

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

      	public FilterForm(String id, IModel<AssetSearchCriteriaModel> model) {
        	super(id, new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
          	setOutputMarkupId(true);
          
		  	add(new IdentifiersCriteriaPanel("identifiers", model));
  			add(new AssetDetailsCriteriaPanel("assetDetailsCriteriaPanel", model) {	
				@Override protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
					SearchFilterPanel.this.onAssetTypeOrGroupUpdated(target, selectedAssetType, availableAssetTypes);
       			}
          	});
          	add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", model));
          	add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));
          	add(new DateRangePicker2("dateRangePicker", new PropertyModel<DateRange>(model, "dateRange")));        
      	}

      	@Override
      	protected void onSubmit() {
          	getModelObject().setReportAlreadyRun(true);
          	getModelObject().getSelection().clear();
	          //---make this refresh results object;
      	}
      	
  }


	
	

}
