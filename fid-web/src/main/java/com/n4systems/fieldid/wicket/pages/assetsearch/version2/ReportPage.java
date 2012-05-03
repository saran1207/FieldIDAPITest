package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.components.reporting.EventReportCriteriaPanel;
import com.n4systems.fieldid.wicket.components.reporting.results.ReportResultsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.ReportCriteriaPanel;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ReportPage extends AbstractSearchPage<EventReportCriteria> {

    private @SpringBean SavedReportService savedReportService;

    public ReportPage(PageParameters params) {
        super(params);
    }

    public ReportPage(EventReportCriteria eventReportCriteria) {
        this(eventReportCriteria, null);
    }

    public ReportPage(EventReportCriteria eventReportCriteria, SavedItem<EventReportCriteria> savedItem) {
        super(new PageParameters(), eventReportCriteria, savedItem);
    }

    @Override
    protected boolean isEmptyResults() {
        return !tenantHasEvents();
    }

    private boolean tenantHasEvents() {
        // XXX : change this to spring service.   can't test this with mocks.
        return new EventCountLoader().setTenantId(getTenantId()).load() > 0;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("speed.reporting"));
    }

    @Override
    protected SavedItem<EventReportCriteria> createSavedItemFromCriteria(EventReportCriteria searchCriteriaModel) {
        return new SavedReportItem(searchCriteriaModel);
    }

    @Override
    protected Component createSubMenu(String id, Model<EventReportCriteria> criteriaModel) {
        return new ReportingSubMenu(id, criteriaModel) {
            @Override protected Link createSaveLink(String id) {
                return ReportPage.this.createSaveLink(id, true);
            }
            @Override protected Link createSaveAsLink(String id) {
                return ReportPage.this.createSaveLink(id, false);
            }
            @Override protected IModel<String> getHeaderModel() {
                return new PropertyModel<String>(savedItem, "name");
            }
        };
    }

    @Override
    protected Component createCriteriaPanel(String id, final Model<EventReportCriteria> model) {
        return new ReportCriteriaPanel(id, model) {
            @Override protected void onSearchSubmit() {
                setResponsePage(new ReportPage(model.getObject(), savedItem));
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
        return new SaveReportPage((SavedReportItem) savedItem, overwrite) {
            @Override protected FieldIDFrontEndPage createCancelResponsePage() {
                return ReportPage.this;
            }
            @Override protected FieldIDFrontEndPage createSaveResponsePage(SavedReportItem newSavedItem) {
                return new ReportPage(newSavedItem.getSearchCriteria(), newSavedItem);
            }
        };
    }

    @Override
    protected Component createCriteriaPanel(String id, Model<EventReportCriteria> criteriaModel, SavedItem<EventReportCriteria> savedItem) {
        return new EventReportCriteriaPanel("criteriaPanel", criteriaModel, (SavedReportItem) savedItem);
    }

    @Override
    protected EventReportCriteria createCriteria() {
        return new EventReportCriteria();
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("title.reporting");
        if (searchCriteria.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteria.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

}
