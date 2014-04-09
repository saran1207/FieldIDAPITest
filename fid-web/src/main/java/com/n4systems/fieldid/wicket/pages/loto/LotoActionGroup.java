package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.Asset;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class LotoActionGroup extends Panel {

    public LotoActionGroup(String id, final IModel<Asset> assetModel) {
        super(id);

        add(new AjaxLink("authorLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new ProcedureDefinitionPage(assetModel.getObject()));
            }
        });

    }
}
