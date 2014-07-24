package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;


public class ProcedureWithoutAuditsAssetCell extends Panel {
    public ProcedureWithoutAuditsAssetCell(String id, IModel<? extends ProcedureDefinition> procedureModel) {
        super(id);

        add(new BookmarkablePageLink<Void>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedureModel.getObject().getAsset().getId()))
                        .setParameter("name", procedureModel.getObject().getEquipmentNumber())
                        .add(new Label("name", procedureModel.getObject().getEquipmentNumber()))
        );
    }
}
