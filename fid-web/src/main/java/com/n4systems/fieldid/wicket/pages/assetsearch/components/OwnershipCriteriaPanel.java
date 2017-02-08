package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class OwnershipCriteriaPanel<T extends SearchCriteria> extends Panel {

    private OrgLocationPicker locationPicker;

    public OwnershipCriteriaPanel(String id, IModel<T> model) {
        super(id, model);
        final PropertyModel<BaseOrg> ownerModel = new PropertyModel<BaseOrg>(getDefaultModel(), "owner");

        //Owner Picker
        add(new OrgLocationPicker("owner", ownerModel) {
            @Override
            protected void onChanged(AjaxRequestTarget target) {
                if(getTextString() == null || getTextString().equals("")) {
                    ownerModel.setObject(null);
                    locationPicker.setLocationOwner(null);
                } else {
                    locationPicker.setLocationOwner(getOwner());
                }
            }

            @Override
            protected boolean showClearIcon() {
                return true;
            }
        }.withAutoUpdate());

        //Location Picker
        final PropertyModel<Location> locationModel = new PropertyModel<Location>(getDefaultModel(), "location");
        final PropertyModel<PredefinedLocation> predefinedLocationModel = new PropertyModel<PredefinedLocation>(getDefaultModel(), "location.predefinedLocation");

        BaseOrg temp = ownerModel.getObject();

        locationPicker = new OrgLocationPicker("location", Model.of(temp), predefinedLocationModel){

            @Override
            protected void onChanged(AjaxRequestTarget target) {
                if(getTextString() == null || getTextString().equals("")) {
                    predefinedLocationModel.setObject(null);
                }
            }

            @Override
            public String getWatermarkText() {
                return new FIDLabelModel("message.locationpicker_watermark").getObject();
            }

            @Override
            protected boolean showClearIcon() {
                return true;
            }

        }.withLocations();
        add(locationPicker);

        //Freeform Location
        add(new TextField<String>("freeformLocation", new PropertyModel<String>(locationModel, "freeformLocation")));

        //Assigned To
        WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedToContainer");
        GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(getDefaultModel(), "assignedTo"), new GroupedVisibleUsersModel());
        groupedUserPicker.setNullValid(true);
        groupedUserPicker.add(new AttributeAppender("data-placeholder", " "));
        assignedUserContainer.add(groupedUserPicker);
        assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
        
        add(assignedUserContainer);
    }

}
