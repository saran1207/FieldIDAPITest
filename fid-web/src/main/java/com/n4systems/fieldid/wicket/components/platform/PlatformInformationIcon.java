package com.n4systems.fieldid.wicket.components.platform;

import com.n4systems.fieldid.wicket.behavior.TooltipBehavior;
import com.n4systems.model.PlatformType;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class PlatformInformationIcon extends Panel {

    public PlatformInformationIcon(String id, IModel<PlatformType> platformType, IModel<String> platform) {
        super(id);

        setVisible(platformType.getObject() != null);

        ContextImage icon;
        add(icon = new ContextImage("icon", createIconUrlModel(platformType)));

        icon.add(new TooltipBehavior(platform));
    }

    private IModel<String> createIconUrlModel(final IModel<PlatformType> platformType) {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return "images/platforms/"+platformType.getObject().name()+".png";
            }
        };
    }

}
