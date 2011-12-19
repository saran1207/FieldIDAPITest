package com.n4systems.fieldid.wicket.components.assetsearch;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.model.EndOfDayDateModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class AssetSearchCriteriaPanel extends SRSCriteriaPanel<AssetSearchCriteriaModel> {

    public AssetSearchCriteriaPanel(String id, IModel<AssetSearchCriteriaModel> criteriaModel) {
        super(id, criteriaModel);
    }

    public AssetSearchCriteriaPanel(String id) {
        super(id, new Model<AssetSearchCriteriaModel>(new AssetSearchCriteriaModel()));
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
        form.add(new IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));
        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));
        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));
        form.add(new DateTimePicker("fromDate", new PropertyModel<Date>(form.getModel(), "identifiedFromDate")));
        form.add(new DateTimePicker("toDate", new EndOfDayDateModel(new PropertyModel<Date>(form.getModel(), "identifiedToDate"))));
    }

    @Override
    protected AssetSearchCriteriaModel createNewCriteriaModel() {
        return new AssetSearchCriteriaModel();
    }

    @Override
    protected WebPage createResultsPage(AssetSearchCriteriaModel criteria) {
        return new AssetSearchResultsPage(criteriaModel.getObject());
    }

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new AssetColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }
}
