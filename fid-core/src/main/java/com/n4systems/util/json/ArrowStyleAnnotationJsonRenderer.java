package com.n4systems.util.json;

import com.google.gson.*;
import com.n4systems.model.common.ImageAnnotation;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * A second, unique JSON renderer needed to be made for Arrow Style annotations.  This is because the two types of
 * Annotations both use the same JPA entity.  Using the same JsonRenderer class would make it impossible to determine
 * which renderer to call.
 *
 * Created by Jordan Heath on 14-11-27.
 */
public class ArrowStyleAnnotationJsonRenderer implements Serializable {

    private Gson gson = new GsonBuilder()
                                .registerTypeAdapter(ImageAnnotation.class, new ArrowStyleAnnotationSerializer())
                                .create();

    public String render(Object bean) {
        return gson.toJson(bean);
    }

    class ArrowStyleAnnotationSerializer implements JsonSerializer<ImageAnnotation> {
        @Override
        public JsonElement serialize(ImageAnnotation annotation, Type sourceType, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            Long id = annotation.getId()!=null ? annotation.getId() : annotation.getTempId();
            object.addProperty("id", id);
            object.addProperty("type", annotation.getType().getCssClass());
            //Since this is for arrow, should we even be passing text?  I don't think so, but I don't want to cause errors by it being null...
            object.addProperty("text", annotation.getText());
            object.addProperty("x", annotation.getX());
            object.addProperty("y", annotation.getY());
            object.addProperty("x2", annotation.getX_tail());
            object.addProperty("y2", annotation.getY_tail());
            //For now, we're adding this here as a static value.  Obviously, Arrow Style annotations will always be of
            //the Arrow Style type...
            object.addProperty("annotationType", "ARROW_STYLE");

            return object;
        }
    }
}
