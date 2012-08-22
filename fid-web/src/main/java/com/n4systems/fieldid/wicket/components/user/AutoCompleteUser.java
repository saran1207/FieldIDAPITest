package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoComplete;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoCompleteResult;
import com.n4systems.model.user.User;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;

public class AutoCompleteUser extends AutoComplete<User> {
    
    private @SpringBean UserService userService;
    
    private HashSet<String> categories = new HashSet<String>();
    
    public AutoCompleteUser(String id, final IModel<User> model) {
        super(id, model);
    }

    @Override
    protected List<User> getChoices() {
        return getChoices(term);
    }

    @Override
    public List<User> getChoices(String term) {
        return userService.search(term, threshold);
    }

    @Override
    protected List<User> getChoicesForEmptyTerm() {
        return userService.search(threshold);
    }

    @Override
    protected String getWatermarkText() {
        return "type user name or id";
    }

    @Override
    protected void startRequest(Request request) {
        categories = new HashSet<String>();
    }

    protected String getCategory(User user) {
        String category = user.isAdmin() ? "Admin" :
                user.isFullUser() ? "Full" :
                user.isLiteUser() ? "Lite" : "Read Only";
        if (!categories.contains(category)) {
            categories.add(category);
            return category;
        }
        return "";
    }

    @Override
    protected String getDisplayValue(User user) {
        return user.getDisplayName();
    }

    protected AutoCompleteResult createAutocompleteJson(User user, String term) {
        term = normalizeSearchTerm(term);
        boolean thisOneSelected = user.equals(getModelObject());
        final String idValue = user.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        String tooltip = user.getUserID();
        return new AutoCompleteResult(user.getId()+"", user.getDisplayName(), getCategory(user), term, tooltip);
    }


}

