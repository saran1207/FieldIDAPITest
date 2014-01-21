package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class GoogleMap extends Panel {
    public static final String GOOGLE_MAPS_JS_ID = "googleMaps";
    public static final String GOOGLE_MAP_API_ID = "google-map-api";
    private static final String GOOGLE_MAP_WITH_LOCATION_JS = "%s = googleMapFactory.createAndShowWithLocation('%s',%s );";
    private static final String GOOGLE_MAP_NO_LOCATION_JS = "%s = googleMapFactory.createAndShow('%s',%s, %d);";

    private @SpringBean JsonRenderer jsonRenderer;

    private GpsModel coords;
    // centre of north america is default location.
    private GpsLocation centre = new GpsLocation(43.548548, -96.987305);
    private int defaultZoom = 5;

    public GoogleMap(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AttributeAppender("class",new Model<String>() {
            @Override public String getObject() {
                return coords.isEmpty() ? getCssForEmptyMap() : getCss();
            }
        }, " "));
    }

    protected String getCss() {
        return "";
    }

    protected String getCssForEmptyMap() {
        return "hide";
    }

    public GoogleMap(String id, Double latitude, Double longitude) {
        this(id);
        coords = new GpsModel(new GpsLocation(latitude, longitude));
    }

    public GoogleMap(String id, IModel<GpsLocation> model) {
        this(id);
        coords = new GpsModel(model.getObject());
    }

    public GoogleMap(String id, GpsLocation location) {
        this(id);
        coords = new GpsModel(location);
    }

    public GoogleMap(String id, List<? extends HasGpsLocation> entities) {
        this(id);
        coords = new GpsModel(entities);
    }

    public GoogleMap(String id, HasGpsLocation entity) {
        this(id);
        coords = new GpsModel(entity.getGpsLocation());
    }

    public GoogleMap setLocation(GpsLocation... locations) {
        coords = new GpsModel(Lists.newArrayList(locations));
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS_ID);
        if (coords.isEmpty()) {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_NO_LOCATION_JS, getJsVar(), getMarkupId(), centre.toString(), defaultZoom));
        } else {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_WITH_LOCATION_JS, getJsVar(), getMarkupId(),jsonRenderer.render(coords.getObject())));
        }
    }

    public String getJsVar() {
        return "map_"+getMarkupId();
    }

    public GoogleMap withCentredLocation(Double latitude, Double longitude) {
        centre = new GpsLocation(latitude,longitude);
        return this;
    }

    public GoogleMap withDefaultZoom(int zoom) {
        defaultZoom = zoom;
        return this;
    }

}
