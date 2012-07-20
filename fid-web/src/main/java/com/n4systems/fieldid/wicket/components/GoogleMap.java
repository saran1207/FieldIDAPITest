package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoogleMap extends Panel {
    public static final String GOOGLE_MAPS_JS_ID = "googleMaps";
    public static final String GOOGLE_MAP_API_ID = "google-map-api";
    private static final String GOOGLE_MAP_WITH_LOCATION_JS = "googleMapFactory.createAndShowWithLocation('%s',%s );";
    private static final String GOOGLE_MAP_NO_LOCATION_JS = "googleMapFactory.createAndShow('%s',%s, %d);";

    private List<Coord> coords = new ArrayList<Coord>();
    // centre of north america is default location.
    private Coord centre = new Coord(43.548548, -96.987305);
    private int defaultZoom = 3;

    public GoogleMap(String id) {
        super(id);
        setOutputMarkupId(true);
    }

    public GoogleMap(String id, Double... points) {
        this(id);
        Preconditions.checkArgument(points.length % 2 == 0, "must specify even number of points (latitude & longitude pairs)");
        for (int i=0; i<points.length; i+=2) {
            addLocation(points[i], points[i+1]);
        }
    }

    public GoogleMap addLocation(Double latitude, Double longitude) {
        coords.add(new Coord(latitude,longitude));
        return this;
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS_ID);
        if (coords.isEmpty()) {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_NO_LOCATION_JS, getMarkupId(), centre.toString(), defaultZoom));
        } else {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_WITH_LOCATION_JS,getMarkupId(),getCoordsAsJsParams()));
        }
    }

    public String getCoordsAsJsParams() {
        String s =  Joiner.on(",").join(coords);
        return s;
    }

    public GoogleMap withCentredLocation(Double latitude, Double longitude) {
        centre = new Coord(latitude,longitude);
        return this;
    }

    public GoogleMap withDefaultZoom(int zoom) {
        defaultZoom = zoom;
        return this;
    }



    private class Coord implements Serializable {
        double latitude;
        double longitude;
        public Coord(double lat, double lng) { 
            this.latitude = lat;
            this.longitude = lng;
        }

        @Override  
        @Deprecated // CAVEAT : this is made for javascript serialization, not human consumption.
        public String toString() {
            return "'"+latitude + "','" + longitude + "'";
        }
    }
}
