package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.MaskType;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.org.BrowsePanel;
import com.n4systems.model.orgs.BaseOrg;

public class ModalOrgPicker extends Panel {

    private IModel<BaseOrg> orgModel;
    private AjaxLink chooseLink;

    private BrowsePanel browsePanel;
	private FIDModalWindow modal;
	private WebMarkupContainer orgNameDisplay;

    public ModalOrgPicker(String id, final IModel<BaseOrg> orgModel) {
        super(id, orgModel);

        setOutputMarkupPlaceholderTag(true);

        this.orgModel = orgModel;

        orgNameDisplay = new WebMarkupContainer("orgNameInput");
        orgNameDisplay.add(new AttributeModifier("value", true, new PropertyModel<String>(orgModel, "displayName"))).setOutputMarkupId(true);
        add(orgNameDisplay);

        addClearAndCloseLinks();

	    browsePanel = new BrowsePanel(FIDModalWindow.CONTENT_ID, orgModel) {
	        @Override
	        protected void onOrgSelected(AjaxRequestTarget target) {
	            closePicker(target);
	        }
	
	        @Override
	        protected void onCancelClicked(AjaxRequestTarget target) {
	            closePicker(target);
	        }
	    };
        add(modal=new FIDModalWindow("choose",orgModel,350,195));
        modal.setTitle(new StringResourceModel("label.owner",this,null));
        modal.setMaskType(MaskType.TRANSPARENT);
        modal.setContent(browsePanel);        
    }

	@Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/featureStyles/orgPickerNew.css");
        response.renderJavaScriptReference("javascript/modalWindow.js");
    }

    private void addClearAndCloseLinks() {
        add(new AjaxLink("clearLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                cancelPicker(target);
            }
		
        });

        add(chooseLink = new AjaxLink("chooseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onChoose(target);
                target.appendJavaScript("modalWindow.relativeTo('"+chooseLink.getMarkupId()+"');");
            }

        });
        chooseLink.setOutputMarkupPlaceholderTag(true);
    }

    protected void onChoose(AjaxRequestTarget target) {
    	modal.show(target);
    }
    
    protected void closePicker(AjaxRequestTarget target) {
    	modal.close(target);
    	target.add(orgNameDisplay);
        onPickerClosed(target);
    }
    
	protected void cancelPicker(AjaxRequestTarget target) {
		orgModel.setObject(null);
        if (browsePanel != null) {
            browsePanel.clearSelections();
        }
        target.add(ModalOrgPicker.this);
	}

    protected void onPickerClosed(AjaxRequestTarget target) {}

}
