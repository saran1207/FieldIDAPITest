package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.components.ExternalImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AssetImagePanel extends Panel {

    public AssetImagePanel(String id, IModel<String> imageUrl) {
        super(id, imageUrl);
        add(new ExternalImage("assetImage", imageUrl.getObject()));
    }

}
