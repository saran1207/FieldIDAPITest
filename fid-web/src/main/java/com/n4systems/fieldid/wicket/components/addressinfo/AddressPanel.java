package com.n4systems.fieldid.wicket.components.addressinfo;

import com.google.gson.GsonBuilder;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.GpsLocation;
import com.n4systems.services.config.ConfigService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.ILabelProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.autocomplete.WiQueryAutocompleteJavaScriptResourceReference;

import java.math.BigDecimal;

import static ch.lambdaj.Lambda.on;

public class AddressPanel extends Panel implements ILabelProvider<String> {

    @SpringBean
    private ConfigService configService;

    private static final String GOOGLE_APIS_JS = "https://maps.googleapis.com/maps/api/js?key=%s";
    private IModel<AddressInfo> model;
    private String externalMapJsVar;
    private boolean noMap = false;
    private boolean hideIfChildrenHidden = false;
    private boolean empty;
    private final TextField<String> text;

    public AddressPanel(String id, IModel<AddressInfo> model) {
        super(id, model);
        this.model = model;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "address"));
        add(text = new TextField<String>("text", ProxyModel.of(model, on(AddressInfo.class).getFormattedAddress())));
        add(new HiddenField<BigDecimal>("latitude", ProxyModel.of(model, on(AddressInfo.class).getGpsLocation().getLatitude())));
        add(new HiddenField<BigDecimal>("longitude", ProxyModel.of(model, on(AddressInfo.class).getGpsLocation().getLongitude())));
        add(new HiddenField<String>("country", ProxyModel.of(model, on(AddressInfo.class).getCountry()))
                .add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override protected void onUpdate(AjaxRequestTarget target) {
                        onCountryChange(target);
                    }
                })
        );
        // the wicket id's chosen here are chosen to reflect the json properties that are returned by the GoogleMaps API.
        // to help understand the javascript binding better.
        // see https://developers.google.com/maps/documentation/geocoding/#ReverseGeocoding
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

    protected void onCountryChange(AjaxRequestTarget target) { }

    protected boolean isTimeZoneVisible() {
        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderCSSReference("style/legacy/component/address.css");
        response.renderJavaScriptReference(WiQueryAutocompleteJavaScriptResourceReference.get());
        response.renderJavaScriptReference(String.format(GOOGLE_APIS_JS, configService.getConfig().getWeb().getGoogleApiKey()), GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
        response.renderOnDomReadyJavaScript(String.format("googleMapFactory.createAutoCompleteAddress(%s);", getOptions()));
        response.renderOnDomReadyJavaScript("var attr = $('.txt').autocomplete('widget').attr('class'); $('.txt').autocomplete('widget').attr('class', 'auto-complete-address ' + attr);");
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

    public AddressPanel hideIfChildrenHidden() {
        this.hideIfChildrenHidden = true;
        return this;
    }

    @Override
    public boolean isVisible() {
        if (super.isVisible()) {
            return get("text").isVisible();
        }
        return false;
    }

    public AddressPanel setRequired(boolean required) {
        text.setRequired(required);
        return this;
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
