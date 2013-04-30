package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.ProcedureCriteria;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class ProcedureSubMenu extends SubMenu<ProcedureCriteria> {

    public ProcedureSubMenu(String id, final Model<ProcedureCriteria> model) {
		super(id, model);

        // all this stuff goes away...possibly implemented later. DD
        add(new WebMarkupContainer("exportToExcelLink").setVisible(false));
        add(new WebMarkupContainer("print").setVisible(false));
        add(new WebMarkupContainer("emailLink").setVisible(false));

        msg.setVisible(false);
    }

    @Override
    protected void updateMenuBeforeRender(ProcedureCriteria criteria) {
       ;    // do nothing.
    }

    @Override
    protected Link createSaveAsLink(String saveAs) {
        return null;
    }

    @Override
    protected Link createSaveLink(String save) {
        return null;
    }

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_procedures";
    }

    @Override protected IModel<String> getHeaderModel() {
        return new FIDLabelModel("label.procedures");
    }

}
