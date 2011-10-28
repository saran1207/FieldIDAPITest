package com.n4systems.util.json;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.chart.ChartableMap;

@SuppressWarnings("serial")
public class JsonRenderer implements Serializable {

	
	public String render(Object bean) {
		Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ChartableMap.class, new ChartableMapSerializer()).create();
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
}
