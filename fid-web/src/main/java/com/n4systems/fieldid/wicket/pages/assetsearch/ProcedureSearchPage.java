package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.fieldid.wicket.components.proceduresearch.results.ProcedureResultsPanel;
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

public class ProcedureSearchPage extends AbstractSearchPage<ProcedureCriteria> {

    @SpringBean
    private ProcedureSearchService procedureSearchService;

    public ProcedureSearchPage(PageParameters params) {
        super(params);
    }

    public ProcedureSearchPage(ProcedureCriteria procedureCriteria) {
        this(procedureCriteria, null);
    }

    public ProcedureSearchPage(ProcedureCriteria procedureCriteria, SavedItem<ProcedureCriteria> savedItem) {
        super(new PageParameters(), procedureCriteria, savedItem);
    }

    @Override
    protected boolean isEmptyResults() {
        return !tenantHasProcedures();
    }

    private boolean tenantHasProcedures() {
        return procedureSearchService.hasProcedures();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedures"));
    }

    @Override
    protected SavedItem<ProcedureCriteria> createSavedItemFromCriteria(ProcedureCriteria procedureCriteriaModel) {
        return null;
    }

    @Override
    protected Component createSubMenu(String id, Model<ProcedureCriteria> criteriaModel) {
        return new ProcedureSubMenu(id, criteriaModel);
    }

    @Override
    protected Component createCriteriaPanel(String id, final Model<ProcedureCriteria> model) {
        return new ProcedureCriteriaPanel(id, model) {
            @Override protected void onSearchSubmit() {
                setResponsePage(new ProcedureSearchPage(model.getObject()));
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
        return new ProcedureResultsPanel(id, criteriaModel);
    }

    @Override
    protected void saveLastSearch(ProcedureCriteria searchCriteria) {
    }

    @Override
    protected Page createSaveResponsePage(boolean overwrite) {
        return new DashboardPage();
    }

    @Override
    protected ProcedureCriteria createCriteria() {
        return new ProcedureCriteria();
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("label.procedures");
        if (searchCriteria.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteria.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

}
