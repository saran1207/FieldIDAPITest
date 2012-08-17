package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.util.ListingPair;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class ListingPairChoiceRenderer implements IChoiceRenderer<ListingPair> {

    @Override
    public Object getDisplayValue(ListingPair object) {
        return object.getName();
    }

    @Override
    public String getIdValue(ListingPair object, int index) {
        return object.getId().toString();
    }

}
