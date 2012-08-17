package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.api.Listable;
import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class GroupedListingPairModel extends LoadableDetachableModel<List<GroupedListingPair>> {

    private IModel<? extends List<? extends Listable<Long>>> wrappedModel;

    public GroupedListingPairModel(IModel<? extends List<? extends Listable<Long>>> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    @Override
    protected List<GroupedListingPair> load() {
        List<? extends Listable<Long>> wrappedList = wrappedModel.getObject();
        List<GroupedListingPair> listingPairList = new ArrayList<GroupedListingPair>(wrappedList.size());

        for (Listable<Long> listable : wrappedList) {
            listingPairList.add(createGroupedListingPair(listable));
        }

        return listingPairList;
    }

    protected GroupedListingPair createGroupedListingPair(Listable<Long> listable) {
        return new GroupedListingPair(listable.getId(), listable.getDisplayName(), null);
    }

}
