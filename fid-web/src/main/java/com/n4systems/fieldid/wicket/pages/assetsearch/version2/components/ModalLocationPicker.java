package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.MaskType;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.model.location.Location;

@Deprecated // to be replaced by AutoCompleteLocationPicker.
@SuppressWarnings("serial")
public class ModalLocationPicker extends Panel {

    private FIDModalWindow modal;
	private AjaxLink chooseLink;
	private WebMarkupContainer locationText;

    public ModalLocationPicker(String id, IModel<Location> locationModel) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        add(modal=new FIDModalWindow("choose",locationModel,500,275));
        modal.setMaskType(MaskType.TRANSPARENT);
        modal.setTitle(new StringResourceModel("label.location",this,null));
        
        modal.setContent(new LocationPanel(FIDModalWindow.CONTENT_ID,locationModel) {
        	@Override protected void onClosePicker(AjaxRequestTarget target) {
        		modal.close(target);   
        		target.add(locationText);
        	}
        	@Override protected void onLocationPicked(AjaxRequestTarget target) {
        		modal.close(target);
        	}
        });

        boolean advancedLocationEnabled = FieldIDSession.get().getSecurityGuard().isAdvancedLocationEnabled();

        WebMarkupContainer predefinedDisabledContainer = new WebMarkupContainer("predefinedDisabledContainer");
        predefinedDisabledContainer.add(new TextField<String>("freeformLocation", new PropertyModel<String>(locationModel, "freeformLocation")));
        predefinedDisabledContainer.setVisible(!advancedLocationEnabled);
        add(predefinedDisabledContainer);

        WebMarkupContainer predefinedEnabledContainer = new WebMarkupContainer("predefinedEnabledContainer");
        locationText = addBaseControls(predefinedEnabledContainer, locationModel);
        predefinedEnabledContainer.setVisible(advancedLocationEnabled);
        add(predefinedEnabledContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/modalWindow.js");
    }

    private WebMarkupContainer addBaseControls(WebMarkupContainer predefinedEnabledContainer, IModel<Location> locationModel) {
        WebMarkupContainer locationNameDisplay = new WebMarkupContainer("locationNameInput");
        locationNameDisplay.setOutputMarkupId(true);
        locationNameDisplay.add(new AttributeModifier("value", true, new PropertyModel<String>(locationModel, "fullName")));
        predefinedEnabledContainer.add(locationNameDisplay);
        

        predefinedEnabledContainer.add(chooseLink = new AjaxLink("chooseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.show(target);
                target.appendJavaScript("modalWindow.relativeTo('"+chooseLink.getMarkupId()+"');");
            }
        });
        chooseLink.setOutputMarkupPlaceholderTag(true);
        return locationNameDisplay;
    }

}
