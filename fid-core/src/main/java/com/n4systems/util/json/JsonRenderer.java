package com.n4systems.util.json;

import com.google.gson.*;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.chart.ChartableMap;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Iterator;

@SuppressWarnings("serial")
public class JsonRenderer implements Serializable {

	private Gson gson = new GsonBuilder().
                                registerTypeAdapter(ChartableMap.class, new ChartableMapSerializer()).
                                registerTypeAdapter(ImageAnnotation.class, new ImageAnnotationSerializer()).
                                create();

	public String render(Object bean) {		
		String json = gson.toJson(bean);
		return json;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	class ChartableMapSerializer implements JsonSerializer<ChartableMap> {

		@Override
		public JsonElement serialize(ChartableMap chartableMap, Type typeOfSrc, JsonSerializationContext context) {
			// e.g. result = [ [0,123], [1,87], [5,96] ]
			JsonArray data = new JsonArray();
			for (Iterator<Chartable<?>> i = chartableMap.values().iterator(); i.hasNext(); ) {
				Chartable<?> chartable = i.next();				
				JsonArray a = new JsonArray();				
				a.add(new JsonPrimitive(chartable.getLongX()));
				a.add(new JsonPrimitive(chartable.getY()));
				data.add(a);
			}
	        return data;
		}
	}

    class ImageAnnotationSerializer implements JsonSerializer<ImageAnnotation> {

        @Override
        public JsonElement serialize(ImageAnnotation annotation, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            Long id = annotation.getId()!=null ? annotation.getId() : annotation.getTempId();
            object.addProperty("id",id);
            object.addProperty("type", annotation.getType().getCssClass());
            object.addProperty("text",annotation.getText());
            object.addProperty("x",annotation.getX());
            object.addProperty("y",annotation.getY());
            return object;
        }
    }

    class GpsLocationSerializer implements JsonSerializer<GpsLocation> {

        @Override
        public JsonElement serialize(GpsLocation src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("latitude",src.getLatitude().doubleValue());
            object.addProperty("longitude",src.getLongitude().doubleValue());
            return object;
        }
    }

}
