package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class AssignToChoiceRenderer implements IChoiceRenderer<User> {

    @Override
    public Object getDisplayValue(User user) {
        return user.getAssignToDisplayName();
    }

    @Override
    public String getIdValue(User user, int index) {
        return user.getId()+"";
    }
}
