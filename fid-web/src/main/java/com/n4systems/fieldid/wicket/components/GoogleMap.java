package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.GpsBounds;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.services.search.MappedResults;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class GoogleMap<T extends HasGpsLocation> extends Panel {
    public static final String GOOGLE_MAPS_JS_ID = "googleMaps";
    public static final String GOOGLE_MAP_API_ID = "google-map-api";
    private static final String CREATE_AND_SHOW_JS = "%s = googleMapFactory.createAndShow(%s);";

    private Gson gson;

    private GpsModel<T> model;
    private GpsLocation centre = new GpsLocation(43.548548, -96.987305); // centre of north america is default location.
    private Integer defaultZoom = 10;
    private AbstractDefaultAjaxBehavior ajax;

    public GoogleMap(String id, final GpsModel<T> model) {
        super(id,model);
        this.model = model;
        setOutputMarkupId(true);
        add(new AttributeAppender("class",new Model<String>() {
            @Override public String getObject() {
                return model.isEmpty() ? getCssForEmptyMap() : getCss();
            }
        }, " "));
    }

    public GoogleMap(String id, Double latitude, Double longitude) {
        this(id, new GpsModel(new GpsLocation(latitude, longitude)));
    }

    public GoogleMap(String id, GpsLocation location) {
        this(id, new GpsModel(location));
    }

    public GoogleMap(String id, List<? extends HasGpsLocation> entities) {
        this(id, new GpsModel(entities));
    }

    public GoogleMap(String id, HasGpsLocation entity) {
        this(id, new GpsModel(entity.getGpsLocation()));
    }

    public GoogleMap withZoomPanNotifications() {
        if (ajax==null) {
            ajax =  new AbstractDefaultAjaxBehavior() {
                protected void respond(final AjaxRequestTarget target) {
                    IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                    double south = params.getParameterValue("s").toDouble();
                    double west = params.getParameterValue("w").toDouble();
                    double north = params.getParameterValue("n").toDouble();
                    double east = params.getParameterValue("e").toDouble();
                    // stuff these into the criteria.
                    onMapChange(target, new GpsBounds(south, west, north, east));
                    target.add(GoogleMap.this);
                }
            };
            add(ajax);
        }
        return this;
    }

    protected void onMapChange(AjaxRequestTarget target, GpsBounds gpsBounds) {
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
        return new GsonBuilder().
                    registerTypeAdapter(MappedResults.class, new MappedResultsSerializer()).
                    create();
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS_ID);       
        response.renderOnDomReadyJavaScript(String.format(CREATE_AND_SHOW_JS, getJsVar(), getGson().toJson(new GoogleMapOptions())));
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

    protected String getClickId(List<T> entitiesAtLocation) {
        if (entitiesAtLocation.size()!=1) {
            return null;
        }
        T entity = entitiesAtLocation.get(0);
        return entity==null ? null : entity.getId()+"";
    }

    protected String getDescription(MappedResults<T> results, GpsLocation gpsLocation) {
        List<T> entitiesAtLocation = results.getEntitiesAtLocation(gpsLocation);
        if (results.isGrouped()) {
            return getGroupedDescription(results.getCount());
        } else if (entitiesAtLocation.isEmpty()) {
            return getEmptyDescription();
        } else if (entitiesAtLocation.size()==1) {
            return getDescription(entitiesAtLocation.get(0));
        } else {
            return getMultipleDescription(entitiesAtLocation);
        }
    }

    protected String getMultipleDescription(List<T> entitiesAtLocation) {
        return Joiner.on(" , " ).skipNulls().join(entitiesAtLocation);
    }

    protected String getDescription(T entity) {
        return entity==null ? "" : entity.toString();
    }

    protected String getGroupedDescription(int count) {
        return new FIDLabelModel("label.mapped_count",count).getObject();
    }

    protected String getEmptyDescription() {
        return "";
    }


    class GoogleMapOptions implements Serializable {
        private Integer zoom = defaultZoom;
        private String id = GoogleMap.this.getMarkupId();
        private Double latitude = centre!=null ? centre.getLatitude().doubleValue() : null;
        private Double longitude = centre!=null ? centre.getLongitude().doubleValue() : null;
        private MappedResults<T> data = model.getObject();
        private String callbackUrl;

        GoogleMapOptions() {
            if (ajax!=null) {
                callbackUrl = ajax.getCallbackUrl().toString();
            }
        }
    }


    class MappedResultsSerializer implements JsonSerializer<MappedResults<T>> {

        @Override
        public JsonElement serialize(MappedResults<T> results, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("count",results.getCount());
            object.addProperty("grouped",results.isGrouped());
            JsonArray data = new JsonArray();
            for (GpsLocation location:results.getLocations()) {
                JsonObject o = new JsonObject();
                o.addProperty("latitude",location.getLatitude());
                o.addProperty("longitude",location.getLongitude());
                o.addProperty("id", getClickId(results.getEntitiesAtLocation(location)));
                o.addProperty("desc", getDescription(results,location));
                data.add(o);
            }
            object.add("results",data);
            return object;
        }
    }

}
