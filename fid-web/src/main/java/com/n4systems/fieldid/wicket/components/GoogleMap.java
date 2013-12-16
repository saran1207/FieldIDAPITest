package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.GpsLocation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.List;

public class GoogleMap extends Panel {
    public static final String GOOGLE_MAPS_JS_ID = "googleMaps";
    public static final String GOOGLE_MAP_API_ID = "google-map-api";
    private static final String GOOGLE_MAP_WITH_LOCATION_JS = "%s = googleMapFactory.createAndShowWithLocation('%s',%s );";
    private static final String GOOGLE_MAP_NO_LOCATION_JS = "%s = googleMapFactory.createAndShow('%s',%s, %d);";

    private List<Coord> coords = Lists.newArrayList();
    // centre of north america is default location.
    private Coord centre = new Coord(43.548548, -96.987305);
    private int defaultZoom = 12;

    public GoogleMap(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AttributeAppender("class",new Model<String>() {
            @Override public String getObject() {
                return coords.isEmpty() ? "no-location" : "";
            }
        }, " "));
    }

    public GoogleMap(String id, Double... points) {
        this(id);
        Preconditions.checkArgument(points.length % 2 == 0, "must specify even number of points (latitude & longitude pairs)");
        for (int i=0; i<points.length; i+=2) {
            addLocation(points[i], points[i+1]);
        }
    }

    public GoogleMap(String id, IModel<GpsLocation> model) {
        this(id);
        addLocation(model.getObject());
    }

    public GoogleMap(String id, GpsLocation location) {
        this(id);
        addLocation(location);
    }

    public GoogleMap setLocation(GpsLocation location) {
        coords = Lists.newArrayList();
        addLocation(location);
        return this;
    }

    public GoogleMap addLocation(GpsLocation location) {
        if (location!=null) {
            addLocation(location.getLatitude().doubleValue(),location.getLongitude().doubleValue());
        }
        return this;
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
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_NO_LOCATION_JS, getJsVar(), getMarkupId(), centre.toString(), defaultZoom));
        } else {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_WITH_LOCATION_JS, getJsVar(), getMarkupId(),getCoordsAsJsParams()));
        }
    }

    public String getJsVar() {
        return "map_"+getMarkupId();
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
