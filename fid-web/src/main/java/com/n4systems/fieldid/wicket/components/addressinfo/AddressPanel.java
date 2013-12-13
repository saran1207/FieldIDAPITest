package com.n4systems.fieldid.wicket.components.addressinfo;

import com.google.gson.GsonBuilder;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.GpsLocation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.ILabelProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.autocomplete.WiQueryAutocompleteJavaScriptResourceReference;

import java.math.BigDecimal;

import static ch.lambdaj.Lambda.on;

public class AddressPanel extends Panel implements ILabelProvider<String> {

    private IModel<AddressInfo> model;
    private String externalMapJsVar;
    private boolean noMap = false;

    public AddressPanel(String id, IModel<AddressInfo> model) {
        super(id, model);
        this.model = model;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "address"));
        add(new TextField<String>("text", ProxyModel.of(model, on(AddressInfo.class).getInput())));
        add(new HiddenField<BigDecimal>("latitude", ProxyModel.of(model, on(AddressInfo.class).getGpsLocation().getLatitude())));
        add(new HiddenField<BigDecimal>("longitude", ProxyModel.of(model, on(AddressInfo.class).getGpsLocation().getLongitude())));
        // the wicket id's chosen here are chosen to reflect the json properties that are returned by the GoogleMaps API.
        // to help understand the javascript binding better.
        // see https://developers.google.com/maps/documentation/geocoding/#ReverseGeocoding
        //formatted_address
        add(new HiddenField<String>("country", ProxyModel.of(model, on(AddressInfo.class).getCountry())));
        add(new HiddenField<String>("administrative_area_level_1", ProxyModel.of(model, on(AddressInfo.class).getState())));
        add(new HiddenField<String>("locality", ProxyModel.of(model, on(AddressInfo.class).getCity())));
        add(new HiddenField<String>("street_address", ProxyModel.of(model, on(AddressInfo.class).getStreetAddress())));
        add(new HiddenField<String>("postal_code", ProxyModel.of(model, on(AddressInfo.class).getZip())));
        add(new WebMarkupContainer("map") {
            @Override public boolean isVisible() {
                return noMap ==false && externalMapJsVar == null;
            }
        }.setOutputMarkupPlaceholderTag(true));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderCSSReference("style/component/address.css");
        response.renderJavaScriptReference(WiQueryAutocompleteJavaScriptResourceReference.get());
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
        response.renderOnDomReadyJavaScript(String.format("googleMapFactory.createAutoCompleteAddress(%s);", getOptions()));
    }

    public AddressPanel withExternalMap(String externalMapJsVar) {
        this.externalMapJsVar = externalMapJsVar;
        return this;
    }

    public AddressPanel withNoMap() {
        this.noMap = true;
        return this;
    }

    protected String getOptions() {
        return new GsonBuilder().create().toJson(new Options());
    }

    @Override
    public IModel<String> getLabel() {
        return Model.of("address");
    }

    class Options {
        String id = String.format("#%s",AddressPanel.this.getMarkupId());
        Double lat;
        Double lng;
        String mapVar = externalMapJsVar;
        Boolean noMap = AddressPanel.this.noMap;

        Options() {
            GpsLocation gpsLocation = model.getObject().getGpsLocation();
            if (gpsLocation==null || gpsLocation.isEmpty()) {
                lat = null;
                lng = null;
            } else {
                lat = gpsLocation.getLatitude().doubleValue();
                lng = gpsLocation.getLongitude().doubleValue();
            }
        }

    }

}
