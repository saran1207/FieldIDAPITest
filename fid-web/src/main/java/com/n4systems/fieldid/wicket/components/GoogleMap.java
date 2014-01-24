package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.services.search.MappedResults;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

public class GoogleMap extends Panel {
    public static final String GOOGLE_MAPS_JS_ID = "googleMaps";
    public static final String GOOGLE_MAP_API_ID = "google-map-api";
    private static final String GOOGLE_MAP_WITH_LOCATION_JS = "%s = googleMapFactory.createAndShowWithLocation('%s',%s );";
    private static final String GOOGLE_MAP_NO_LOCATION_JS = "%s = googleMapFactory.createAndShow('%s',%s, %d);";

    private Gson gson;

    private GpsModel model;
    private GpsLocation centre = new GpsLocation(43.548548, -96.987305); // centre of north america is default location.
    private int defaultZoom = 5;

    public GoogleMap(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AttributeAppender("class",new Model<String>() {
            @Override public String getObject() {
                return model.isEmpty() ? getCssForEmptyMap() : getCss();
            }
        }, " "));
    }

    public GoogleMap(String id, GpsModel<?> model) {
        this(id);
        this.model = model;
    }

    public GoogleMap(String id, Double latitude, Double longitude) {
        this(id);
        model = new GpsModel(new GpsLocation(latitude, longitude));
    }

    public GoogleMap(String id, GpsLocation location) {
        this(id);
        model = new GpsModel(location);
    }

    public GoogleMap(String id, List<? extends HasGpsLocation> entities) {
        this(id);
        model = new GpsModel(entities);
    }

    public GoogleMap(String id, HasGpsLocation entity) {
        this(id);
        model = new GpsModel(entity.getGpsLocation());
    }

    protected String getCss() {
        return "";
    }

    protected String getCssForEmptyMap() {
        return "hide";
    }

    public GoogleMap setLocation(GpsLocation... locations) {
        model = new GpsModel(Lists.newArrayList(locations));
        return this;
    }

    private Gson getGson() {
        if (gson==null) {
            gson = new GsonBuilder().
                    registerTypeAdapter(MappedResults.class, new MappedResultsSerializer()).
                    create();

        }
        return gson;
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS_ID);
        if (model.isEmpty()) {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_NO_LOCATION_JS, getJsVar(), getMarkupId(), centre.toString(), defaultZoom));
        } else {
            response.renderOnDomReadyJavaScript(String.format(GOOGLE_MAP_WITH_LOCATION_JS, getJsVar(), getMarkupId(),getGson().toJson(model.getObject())));
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



    class MappedResultsSerializer implements JsonSerializer<MappedResults> {

        @Override
        public JsonElement serialize(MappedResults results, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("count",results.getCount());
            JsonArray data = new JsonArray();
            Iterator<MappedResults.MappedResult> iter = results.getMappedResults();
            while (iter.hasNext()) {
                JsonObject o = new JsonObject();
                MappedResults.MappedResult result = iter.next();
                GpsLocation location = result.getLocation();
                o.addProperty("latitude",location.getLatitude());
                o.addProperty("longitude",location.getLongitude());
                o.addProperty("desc",result.toString());
                data.add(o);
            }
            object.add("results",data);
            return object;
        }
    }




}
