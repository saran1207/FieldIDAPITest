package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;

public class UserGroupTitleLabel extends Panel {
    public UserGroupTitleLabel(String labelId) {
        super(labelId);

        ContextImage tooltip;
        add(tooltip = new ContextImage("tooltip", "images/tooltip-icon.png"));
        tooltip.add(new TipsyBehavior(new FIDLabelModel("label.manage_user_groups_msg"), TipsyBehavior.Gravity.NW));
    }


}
