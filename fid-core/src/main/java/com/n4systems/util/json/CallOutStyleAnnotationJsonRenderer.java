package com.n4systems.util.json;

import com.google.gson.*;
import com.n4systems.model.common.ImageAnnotation;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by rrana on 2015-03-09.
 */
public class CallOutStyleAnnotationJsonRenderer implements Serializable {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(ImageAnnotation.class, new CallOutStyleAnnotationSerializer())
            .create();

    public String render(Object bean) {
        return gson.toJson(bean);
    }

    class CallOutStyleAnnotationSerializer implements JsonSerializer<ImageAnnotation> {
        @Override
        public JsonElement serialize(ImageAnnotation annotation, Type sourceType, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            Long id = annotation.getId()!=null ? annotation.getId() : annotation.getTempId();
            object.addProperty("id", id);
            object.addProperty("type", annotation.getType().getCssClass());
            object.addProperty("x", annotation.getX());
            object.addProperty("y", annotation.getY());
            object.addProperty("text", annotation.getText());
            object.addProperty("fill", annotation.getType().getBackgroundColor());
            object.addProperty("stroke", annotation.getType().getFontColor());

            //not sure if we need this
            object.addProperty("annotationType", "CALL_OUT_STYLE");
            return object;
        }
    }
}
