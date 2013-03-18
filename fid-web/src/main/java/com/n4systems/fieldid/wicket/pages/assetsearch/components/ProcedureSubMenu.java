package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.print.ExportReportToExcelPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.model.search.ProcedureCriteria;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class ProcedureSubMenu extends SubMenu<ProcedureCriteria> {

    private Link exportLink;
    private WebMarkupContainer print;

    public ProcedureSubMenu(String id, final Model<ProcedureCriteria> model) {
		super(id, model);

        add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportReportToExcelPage>("exportToExcelLink", ExportReportToExcelPage.class, model)));

        print = new WebMarkupContainer("print");
        // TODO DD : what should print do...hook this up.
        add(print);

        add(new Link("emailLink") {
            @Override public void onClick() {
                setResponsePage(new SendSavedItemPage(model, getPage()));
            }
        });

        initializeLimits();
    }

    @Override
    protected void updateMenuBeforeRender(ProcedureCriteria criteria) {
        super.updateMenuBeforeRender(criteria);
        int selected = criteria.getSelection().getNumSelectedIds();

        exportLink.setVisible(selected > 0 && selected < maxExport);
        print.setVisible(selected > 0 && selected < maxPrint);
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
        return new FIDLabelModel("title.procedure");
    }


}
