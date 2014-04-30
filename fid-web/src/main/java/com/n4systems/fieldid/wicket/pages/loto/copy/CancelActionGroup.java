package com.n4systems.fieldid.wicket.pages.loto.copy;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.ProceduresListPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CancelActionGroup extends Panel {

    public CancelActionGroup(String id, final IModel<Asset> assetModel) {
        super(id, assetModel);
        add(new Link<Void>("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(ProceduresListPage.class, PageParametersBuilder.uniqueId(assetModel.getObject().getId()));
            }
        });

    }
}
