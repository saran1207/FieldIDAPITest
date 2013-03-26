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
        AutoCompleteUser autoCompleteUser = new AutoCompleteUser(id, model);
        autoCompleteUser.setRenderBodyOnly(true);
        return autoCompleteUser;
    }

    @Override
    protected String getComponentMarkupId(AutoComplete<User> component) {
        return component.getAutocompleteField().getMarkupId();
    }

}
