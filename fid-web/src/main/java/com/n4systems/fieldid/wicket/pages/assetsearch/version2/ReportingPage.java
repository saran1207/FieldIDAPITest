package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.components.reporting.EventReportCriteriaPanel;
import com.n4systems.fieldid.wicket.components.reporting.results.ReportResultsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.ReportingSubMenu;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.SearchBlankSlate;
import com.n4systems.fieldid.wicket.pages.reporting.SaveReportPage;
import com.n4systems.model.event.EventCountLoader;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ReportingPage extends AbstractSearchPage<EventReportCriteria> {

    private @SpringBean SavedReportService savedReportService;

    public ReportingPage(PageParameters params) {
        super(params);
        EventReportCriteria model = new EventReportCriteria();
        init(model, createSavedItemFromCriteria(model), true);
    }

    public ReportingPage(EventReportCriteria eventReportCriteria, SavedItem<EventReportCriteria> savedItem, boolean showLeftMenu) {
        super(new PageParameters());
        SavedReportItem newSavedReportItem = new SavedReportItem(eventReportCriteria);
        newSavedReportItem.setId(savedItem == null ? null : newSavedReportItem.getId());
        init(eventReportCriteria, newSavedReportItem, showLeftMenu);
    }

    @Override
    protected boolean isEmptyResults() {
        return !tenantHasEvents();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("speed.reporting"));
    }

    private boolean tenantHasEvents() {
        // XXX : change this to spring service.   can't test this with mocks.
        return new EventCountLoader().setTenantId(getTenantId()).load() > 0;
    }

    @Override
    protected SavedItem<EventReportCriteria> createSavedItemFromCriteria(EventReportCriteria searchCriteriaModel) {
        return new SavedReportItem(searchCriteriaModel);
    }

    @Override
    protected Component createSubMenu(String id, Model<EventReportCriteria> criteriaModel) {
        return new ReportingSubMenu(id, criteriaModel) {
            @Override protected Component createSaveLink(String id) {
                return ReportingPage.this.createSaveLink(id, true);
            }
        };
    }

    @Override
    protected Component createCriteriaPanel(String id, final Model<EventReportCriteria> model) {
        return new ReportCriteriaPanel(id, model) {
            @Override protected void onSearchSubmit() {
                setResponsePage(new ReportingPage(model.getObject(),null,false));
            }
            @Override protected void onNoDisplayColumnsSelected() { }
        };
    }

    @Override
    protected Component createBlankSlate(String id) {
        return new SearchBlankSlate(id);
    }

    @Override
    protected Component createResultsPanel(String id, Model<EventReportCriteria> criteriaModel) {
        return new ReportResultsPanel(id, criteriaModel) {
            @Override protected void updateSelectionStatus(AjaxRequestTarget target) {
                super.updateSelectionStatus(target);
                target.add(searchMenu);
            };
        };
    }

    @Override
    protected void saveLastSearch(EventReportCriteria searchCriteria) {
        savedReportService.saveLastSearch(searchCriteria);
    }

    @Override
    protected Page createSaveReponsePage(boolean overwrite) {
        return new SaveReportPage((SavedReportItem) savedItem, ReportingPage.this, overwrite);
    }

    @Override
    protected Component createCriteriaPanel(String id, Model<EventReportCriteria> criteriaModel, SavedItem<EventReportCriteria> savedItem) {
        return new EventReportCriteriaPanel("criteriaPanel", criteriaModel, (SavedReportItem) savedItem);
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("title.reporting");
        if (searchCriteria.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteria.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	response.renderJavaScriptReference("javascript/fieldIdWide.js");
        response.renderCSSReference("style/pageStyles/wide.css");
        response.renderOnDomReadyJavaScript("fieldIdWidePage.init("+isShowLeftMenu()+");");
    }
    
}
