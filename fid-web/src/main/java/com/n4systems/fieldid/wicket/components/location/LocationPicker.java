package com.n4systems.fieldid.wicket.components.location;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationByIdLoader;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class LocationPicker extends Panel {

    private IModel<Location> locationModel;
    private WebMarkupContainer locationPickerContainer;
    private AjaxLink chooseLink;

    private PredefinedLocationsPanel predefinedLocationsPanel;
    private String freeformLocation;

    public LocationPicker(String id, IModel<Location> locationModel) {
        super(id);
        this.locationModel = locationModel;
        setOutputMarkupPlaceholderTag(true);

        add(CSSPackageResource.getHeaderContribution("style/featureStyles/locationPicker.css"));

        add(locationPickerContainer = new WebMarkupContainer("locationPickerContainer"));
        locationPickerContainer.setOutputMarkupPlaceholderTag(true);

        LocationForm locationForm = new LocationForm("locationForm");
        locationPickerContainer.add(locationForm);

        WebMarkupContainer predefinedEnabledContainer = new WebMarkupContainer("predefinedEnabledContainer");
        addBaseControls(predefinedEnabledContainer, locationModel);
        predefinedEnabledContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAdvancedLocationEnabled());
        add(predefinedEnabledContainer);

        WebMarkupContainer predefinedDisabledContainer = new WebMarkupContainer("predefinedDisabledContainer");
        predefinedDisabledContainer.add(new TextField<String>("freeformLocation", new PropertyModel<String>(locationModel, "freeformLocation")));
        predefinedDisabledContainer.setVisible(!FieldIDSession.get().getSecurityGuard().isAdvancedLocationEnabled());
        add(predefinedDisabledContainer);

        locationForm.add(predefinedLocationsPanel = new PredefinedLocationsPanel("predefinedLocationPanel"));
    }

    private void addBaseControls(WebMarkupContainer predefinedEnabledContainer, IModel<Location> locationModel) {
        WebMarkupContainer locationNameDisplay = new WebMarkupContainer("locationNameInput");
        locationNameDisplay.add(new AttributeModifier("value", true, new PropertyModel<String>(locationModel, "fullName")));
        predefinedEnabledContainer.add(locationNameDisplay);

        predefinedEnabledContainer.add(chooseLink = new AjaxLink("chooseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                locationPickerContainer.setVisible(true);
                target.addComponent(LocationPicker.this);
                target.appendJavascript("translate($('" + locationPickerContainer.getMarkupId() + "'), $('" + chooseLink.getMarkupId() + "'), 0, 0);");
            }
        });
        chooseLink.setOutputMarkupPlaceholderTag(true);
    }

    class LocationForm extends Form {

        private String freeformLocation;

        public LocationForm(String id) {
            super(id);

            add(new TextField<String>("predefFreeformLocation", new PropertyModel<String>(this, "freeformLocation")));

            add(new AjaxButton("selectLocationButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setModelValueFromPicker();
                    closePicker(target);
                }
            });

            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    closePicker(target);
                }
            });
        }

        private void setModelValueFromPicker() {
            PredefinedLocation predefLocation = null;
            if (!predefinedLocationsPanel.getSelectedPredefinedLocationId().equals(-1L)) {
                PredefinedLocationByIdLoader predefLoader = new PredefinedLocationByIdLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
                predefLocation = predefLoader.setId(predefinedLocationsPanel.getSelectedPredefinedLocationId()).load();
            }
            Location location = new Location(predefLocation, freeformLocation);
            locationModel.setObject(location);
        }

    }

    private void closePicker(AjaxRequestTarget target) {
        locationPickerContainer.setVisible(false);
        target.addComponent(LocationPicker.this);
    }

}
