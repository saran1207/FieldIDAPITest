package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;

import java.net.URL;

public class ExternalImage extends WebComponent {

    public ExternalImage(String id, String imageUrl) {
        super(id);
        add(new AttributeModifier("src", imageUrl));
	    setVisible(imageUrl != null && !imageUrl.equals("") && isValidImage(imageUrl));
    }

    protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
    }
    
    private boolean isValidImage(String imageUrl){
        URL url = null;
        try {
            url = new URL(imageUrl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
