package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.version.FieldIdVersion;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

public class CachingStrategyLink extends WebMarkupContainer {

    private String attribute = "href";

    public CachingStrategyLink(String id, String attribute) {
        super(id);
        add(new AttributeAppender(attribute, Model.of("?" + FieldIdVersion.getVersion()), ""));
    }

    public CachingStrategyLink(String id) {
        this(id, "href");
    }
}
