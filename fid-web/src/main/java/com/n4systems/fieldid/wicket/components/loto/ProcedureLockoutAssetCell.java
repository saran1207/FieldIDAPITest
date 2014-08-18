package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-05-19.
 */
public class ProcedureLockoutAssetCell extends Panel {

    public ProcedureLockoutAssetCell(String id, IModel<? extends Procedure> procedureModel) {
        super(id);
        if(procedureModel.getObject() != null) {
            add(new BookmarkablePageLink<Void>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedureModel.getObject().getAsset().getId()))
                    .setParameter("name", procedureModel.getObject().getType().getEquipmentNumber())
                    .add(new Label("name", procedureModel.getObject().getType().getEquipmentNumber()))
            );
        } else {
            add(new Link<Void>("assetLink") {
                @Override
                public void onClick() {

                }
            }.add(new Label("name")));
        }
    }
}

