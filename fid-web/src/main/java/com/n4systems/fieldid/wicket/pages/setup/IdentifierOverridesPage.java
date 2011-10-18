package com.n4systems.fieldid.wicket.pages.setup;

import java.util.List;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.AssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;

@SuppressWarnings("serial")
public class IdentifierOverridesPage extends FieldIDFrontEndPage {

    @SpringBean
    private SystemSettingsService systemSettingsService;
    
	public IdentifierOverridesPage(PageParameters params) {
        super(params);
        add(new IdentifierOverridesForm("identifierOverridesForm", new AssetTypesForTenantModel()));        
    }

    class IdentifierOverridesForm extends Form<List<AssetType>> { 

    	public IdentifierOverridesForm(String id, IModel<List<AssetType>> model) { 
    		super(id, model);
    		
            add(new FIDFeedbackPanel("feedbackPanel"));
            
		    add(new ListView<AssetType>("assetTypeOverrides", model) {
		        @Override
		        protected void populateItem(ListItem<AssetType> item) {
		            item.add(new CheckBox("override", new PropertyModel<Boolean>(item.getModel(), "identifierOverridden")));
		            item.add(new FlatLabel("assetTypeName", new PropertyModel<Boolean>(item.getModel(), "name")));
		            item.add(new TextField<String>("identifierLabel", new PropertyModel<String>(item.getModel(), "identifierLabel")));
		            item.add(new TextField<String>("identifierFormat", new PropertyModel<String>(item.getModel(), "identifierFormat")));
		        }
		    });
            add(new Button("submitButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", SystemSettingsPage.class));		    
    	}
    	
    	@Override
    	protected void onSubmit() {
        	systemSettingsService.saveSystemSettings(getModelObject());
        	FieldIDSession.get().info(new FIDLabelModel("message.system_settings_updated").getObject());
        	setResponsePage(SystemSettingsPage.class);
 		}
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.identifier_overrides"));
    }	
	
}
