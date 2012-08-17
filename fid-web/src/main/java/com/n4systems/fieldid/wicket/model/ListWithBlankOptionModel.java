package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class ListWithBlankOptionModel extends LoadableDetachableModel<List<GroupedListingPair>> {

    private IModel<List<GroupedListingPair>> wrappedModel;

    public ListWithBlankOptionModel(IModel<List<GroupedListingPair>> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    @Override
    protected List<GroupedListingPair> load() {
        try {
            GroupedListingPair blankOption = new GroupedListingPair();
            blankOption.setId(0L);
            blankOption.setGroup("");

            List<GroupedListingPair> list = new ArrayList<GroupedListingPair>(wrappedModel.getObject());
            list.add(0, blankOption);

            return list;
        } catch (Exception e) {
            throw new RuntimeException("could not instantiate list item class", e);
        }
    }

}
