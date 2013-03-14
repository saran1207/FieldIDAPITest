package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.ProcedureService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.ProcedureCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.ProcedureSubMenu;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.SearchBlankSlate;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.ProcedureCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ProcedurePage extends AbstractSearchPage<ProcedureCriteria> {

    private @SpringBean
    ProcedureService procedureService;

    public ProcedurePage(PageParameters params) {
        super(params);
    }

    public ProcedurePage(ProcedureCriteria procedureCriteria) {
        this(procedureCriteria, null);
    }

    public ProcedurePage(ProcedureCriteria procedureCriteria, SavedItem<ProcedureCriteria> savedItem) {
        super(new PageParameters(), procedureCriteria, savedItem);
    }

    @Override
    protected boolean isEmptyResults() {
        return !tenantHasProcedures();
    }

    private boolean tenantHasProcedures() {
        return procedureService.hasProcedures();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("speed.reporting"));
    }

    @Override
    protected SavedItem<ProcedureCriteria> createSavedItemFromCriteria(ProcedureCriteria procedureCriteriaModel) {
        return null;    //not supported in this page...just return null for now.
    }

    @Override
    protected Component createSubMenu(String id, Model<ProcedureCriteria> criteriaModel) {
        return new ProcedureSubMenu(id, criteriaModel);
    }

    @Override
    protected Component createCriteriaPanel(String id, final Model<ProcedureCriteria> model) {
        return new ProcedureCriteriaPanel(id, model) {
            @Override protected void onSearchSubmit() {
                setResponsePage(new ProcedurePage(model.getObject(), savedItem));
            }
            @Override protected void onSearchError() {
                resetPageOnError();
            }
        };
    }

    @Override
    protected Component createBlankSlate(String id) {
        return new SearchBlankSlate(id);
    }

    @Override
    protected Component createResultsPanel(String id, Model<ProcedureCriteria> criteriaModel) {
        return null;
// TODO DD : implement search here...
//                new ReportResultsPanel(id, criteriaModel) {
//            @Override protected void updateSelectionStatus(AjaxRequestTarget target) {
//                super.updateSelectionStatus(target);
//                target.add(searchMenu);
//            };
//        };
    }

    @Override
    protected void saveLastSearch(ProcedureCriteria searchCriteria) {
        //  TODO : savedProcedureService.save(searchCriteria);
    }

    @Override
    protected Page createSaveReponsePage(boolean overwrite) {
        return new DashboardPage();
    }

    @Override
    protected ProcedureCriteria createCriteria() {
        return new ProcedureCriteria();
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("title.procedure");
        if (searchCriteria.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteria.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

}
