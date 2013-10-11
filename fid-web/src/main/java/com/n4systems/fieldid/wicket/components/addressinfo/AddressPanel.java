package com.n4systems.fieldid.wicket.components.addressinfo;

import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Address;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.ui.autocomplete.WiQueryAutocompleteJavaScriptResourceReference;

import java.math.BigDecimal;

import static ch.lambdaj.Lambda.on;

public class AddressPanel extends Panel {

    public AddressPanel(String id, IModel<Address> model) {
        super(id, model);
        add(new AttributeAppender("class","address"));
        add(new TextField<String>("text", ProxyModel.of(model, on(Address.class).getText())));
        add(new HiddenField<BigDecimal>("latitude", ProxyModel.of(model, on(Address.class).getGpsLocation().getLatitude())));
        add(new HiddenField<BigDecimal>("longitude", ProxyModel.of(model, on(Address.class).getGpsLocation().getLongitude())));
        add(new WebMarkupContainer("map").setOutputMarkupPlaceholderTag(true));
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderCSSReference("style/component/address.css");
        response.renderJavaScriptReference(WiQueryAutocompleteJavaScriptResourceReference.get());
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
        response.renderOnDomReadyJavaScript(String.format("googleMapFactory.createAutoCompleteAddress($('#%s'));", getMarkupId()));
    }

}
