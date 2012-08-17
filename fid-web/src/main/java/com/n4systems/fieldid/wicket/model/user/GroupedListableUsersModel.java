package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.GroupedListingPairModel;
import com.n4systems.model.api.Listable;
import com.n4systems.model.user.User;
import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedListableUsersModel extends GroupedListingPairModel {

    public GroupedListableUsersModel(IModel<? extends List<? extends Listable<Long>>> wrappedModel) {
        super(wrappedModel);
    }

    @Override
    protected GroupedListingPair createGroupedListingPair(Listable<Long> listable) {
        User user = (User) listable;
        return new GroupedListingPair(user.getId(), user.getAssignToDisplayName(), user.getOwner().getDisplayName());
    }
}
