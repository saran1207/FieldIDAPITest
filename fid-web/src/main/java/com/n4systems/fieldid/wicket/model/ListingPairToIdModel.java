package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.model.IModel;

// This converts a chosen listing pair item into an underlying model's id field
public class ListingPairToIdModel implements IModel<GroupedListingPair> {

    private IModel<Long> idModel;

    public ListingPairToIdModel(IModel<Long> idModel) {
        this.idModel = idModel;
    }

    @Override
    public GroupedListingPair getObject() {
        GroupedListingPair listingPair = new GroupedListingPair();
        listingPair.setId(idModel.getObject());
        return listingPair;
    }

    @Override
    public void setObject(GroupedListingPair object) {
        if (object == null) {
            idModel.setObject(null);
        } else {
            idModel.setObject(object.getId());
        }
    }

    @Override
    public void detach() {
        idModel.detach();
    }

}
