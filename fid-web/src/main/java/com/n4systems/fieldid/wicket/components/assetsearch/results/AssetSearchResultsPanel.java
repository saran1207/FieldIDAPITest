package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.service.search.columns.AssetTextOrFilterSearchService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.GpsModel;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.AssetSearchDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.GpsBounds;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.MappedResults;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetSearchResultsPanel extends SRSResultsPanel<AssetSearchCriteria, AssetSearchRecord> {

    private static final String MARKER_FORMAT =
            "<div class='marker'>" +
                    "<p class='title'>%s / %s / %s</p>" +
                    "<br>" +
                    "<p class='owner'>%s</p>" +
                    "<a href='%s' class='marker'>%s</a><br>" +
            "</div>";
    private final String linkLabel = new FIDLabelModel("label.view_asset").getObject();

    private @SpringBean AssetTextOrFilterSearchService assetTextOrFilterSearchService;

    public AssetSearchResultsPanel(String id, final IModel<AssetSearchCriteria> criteriaModel) {
        super(id, criteriaModel);
        resultButtons.setVisible(true);
    }

      @Override
    protected IColumn<RowView> createActionsColumn() {
        return new AssetActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<AssetSearchCriteria> criteriaModel) {
        return new AssetSearchDataProvider(criteriaModel.getObject());
    }

    @Override
    protected Component createMap(String id) {
        GpsModel<AssetSearchRecord> mapModel = new GpsModel<AssetSearchRecord>() {
            @Override protected MappedResults<AssetSearchRecord> load() {
                return assetTextOrFilterSearchService.performMapSearch(criteriaModel.getObject());
            }
        };

        GoogleMap<AssetSearchRecord>map = new GoogleMap<AssetSearchRecord>("resultsMap",mapModel) {
            @Override protected String getCssForEmptyMap() {
                return "";
            }
            @Override protected void onMapChange(AjaxRequestTarget target, GpsBounds bounds) {
                criteriaModel.getObject().setBounds(bounds);
                target.add(AssetSearchResultsPanel.this.map);
            }
            @Override protected String getDescription(AssetSearchRecord entity) {
                return getMapMarkerText(entity);
            }
        };
        map.withZoomPanNotifications().setOutputMarkupPlaceholderTag(true).setVisible(false);
        return map;
    }

    protected String getMapMarkerText(AssetSearchRecord entity) {
        String url = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(AssetSummaryPage.class, new PageParameters().add("uniqueID", entity.getId()).add("useContext", "false")).toString()));
        return String.format(MARKER_FORMAT, entity.getType(), entity.getSerialNumber(), entity.getStatus(), entity.getOwner(), url, linkLabel );
    }

}

