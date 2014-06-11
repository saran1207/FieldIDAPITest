package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditAssetCell extends Panel {

    public ProcedureAuditAssetCell(String id, IModel<? extends Procedure> procedureModel) {
        super(id);

        add(new BookmarkablePageLink<Void>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedureModel.getObject().getType().getAsset().getId()))
                        .setParameter("name", procedureModel.getObject().getType().getEquipmentNumber())
                        .add(new Label("name", procedureModel.getObject().getType().getEquipmentNumber()))
                        .add(new AttributeAppender("class", new Model<String>("equipment-number"), " "))
        );
    }
}

