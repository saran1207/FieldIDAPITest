package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.ArrayList;
import java.util.List;

public class GoogleMap extends Panel {
    public static final String GOOGLE_MAPS_JS = "googleMaps";
    private static final String GOOGLE_MAP_API = "google-map-api";
    private static final String INIT_FORMAT = "googleMapFactory.createAndShowWithLocation('%s',%s );";

    private List<Coord> coords = new ArrayList<Coord>();
    private String coordsAsJsParams;

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
        response.renderJavaScriptReference("http://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS);
        response.renderOnDomReadyJavaScript(String.format(INIT_FORMAT,getMarkupId(),getCoordsAsJsParams()));
    }

    public String getCoordsAsJsParams() {
        String s =  Joiner.on(",").join(coords);
        return s;
    }


    private class Coord {
        double latitude;
        double longitude;
        public Coord(double lat, double lng) { 
            this.latitude = lat;
            this.longitude = lng;
        }

        @Override  // CAVEAT : this is made for javascript, not everyday usage.
        public String toString() {
            return "'"+latitude + "','" + longitude + "'";
        }
    }
}
