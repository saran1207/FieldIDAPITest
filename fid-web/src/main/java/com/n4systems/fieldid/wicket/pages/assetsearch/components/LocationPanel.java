package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.location.PredefinedLocationsPanel;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationByIdLoader;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class LocationPanel extends Panel {

    private IModel<Location> locationModel;
    private TextField<String> freeformLocationField;
    private LocationForm locationForm;

    private PredefinedLocationsPanel predefinedLocationsPanel;

    public LocationPanel(String id, IModel<Location> locationModel) {
        super(id);
        this.locationModel = locationModel;
        setOutputMarkupPlaceholderTag(true);

        locationForm = new LocationForm("locationForm");

        locationForm.add(predefinedLocationsPanel = new PredefinedLocationsPanel("predefinedLocationPanel"));
        
        add(locationForm);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/featureStyles/locationPicker.css");
    }

    public void setOwner(BaseOrg owner) {
        predefinedLocationsPanel.setOwner(owner);
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
                    onLocationPicked(target);
                    onClosePicker(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });

            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                	onClosePicker(target);
                }
            });
        }

        private void setModelValueFromPicker() {
            PredefinedLocation predefLocation = null;
            if (!predefinedLocationsPanel.getSelectedPredefinedLocationId().equals(PredefinedLocationsPanel.NO_PREDEFINED_LOCATION)) {
                PredefinedLocationByIdLoader predefLoader = new PredefinedLocationByIdLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
                predefLocation = predefLoader.setId(predefinedLocationsPanel.getSelectedPredefinedLocationId()).load();
            }
            Location location = new Location(predefLocation, freeformLocation);
            locationModel.setObject(location);
        }

    }

    public void clear() {
        locationForm.freeformLocation = null;
        predefinedLocationsPanel.clear();
    }

    protected void onClosePicker(AjaxRequestTarget target) { }

    protected void onLocationPicked(AjaxRequestTarget target) {}

}
