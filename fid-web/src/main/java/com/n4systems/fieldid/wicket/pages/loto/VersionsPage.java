package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class VersionsPage extends LotoPage{
    public VersionsPage(PageParameters params) {
        super(params);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.view_all_versions"));
    }

}
