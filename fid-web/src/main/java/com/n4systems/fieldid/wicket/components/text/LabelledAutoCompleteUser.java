package com.n4systems.fieldid.wicket.components.text;

import com.n4systems.fieldid.wicket.components.autocomplete.AutoComplete;
import com.n4systems.fieldid.wicket.components.user.AutoCompleteUser;
import com.n4systems.model.user.User;
import org.apache.wicket.model.IModel;

public class LabelledAutoCompleteUser extends LabelledComponent<AutoComplete<User>,User> {

    public LabelledAutoCompleteUser(String id, String key, IModel<User> model) {
        super(id, key, model);
    }

    @Override
    protected AutoComplete<User> createLabelledComponent(String id, IModel<User> model) {
        return new AutoCompleteUser(id,model);
    }

}
