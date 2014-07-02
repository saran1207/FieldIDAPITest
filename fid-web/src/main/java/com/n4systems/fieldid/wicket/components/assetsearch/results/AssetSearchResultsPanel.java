package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.google.gson.JsonObject;
import com.n4systems.fieldid.service.search.columns.AssetTextOrFilterSearchService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.GpsModel;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.AssetSearchDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.GpsBounds;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
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
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.List;

public class AssetSearchResultsPanel extends SRSResultsPanel<AssetSearchCriteria, AssetSearchRecord> {

    private static final String MARKER_FORMAT =
            "<div class='marker'>" +
                    "<p class='title'>%s / %s / %s</p>" +
                    "<p class='owner'>%s</p>" +
                    "%s" +
            "</div>";
    private static final String MARKER_LINK_FORMAT = "<a class='link' href='%s' class='marker'>%s</a><br>";

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

        GoogleMap<AssetSearchRecord>map = new GoogleMap<AssetSearchRecord>("resultsMap", mapModel) {
            @Override protected void onMapChange(AjaxRequestTarget target, GpsBounds bounds, int zoom, GpsLocation centre) {
                super.onMapChange(target, bounds, zoom, centre);
                criteriaModel.getObject().setBounds(bounds);
                target.add(AssetSearchResultsPanel.this.map);
            }

            @Override protected String getDescription(AssetSearchRecord entity) {
                return getMapMarkerTextWithUrl(entity);
            }

            @Override protected String getMultipleDescription(AssetSearchRecord entity) {
                return getMapMarkerText(entity);
            }

            @Override protected String getCssForEmptyMap() {
                return "no-locations";
            }

            @Override
            protected void addCustomProperties(JsonObject o, MappedResults<AssetSearchRecord> results, GpsLocation location) {
                o.addProperty("colour", getMarkerColour(results, location));
            }
        };
        map.withZoomPanNotifications().setOutputMarkupPlaceholderTag(true).setVisible(false);
        return map;
    }


    protected String getMarkerColour(MappedResults<AssetSearchRecord> results, GpsLocation gpsLocation) {
        List<AssetSearchRecord> entitiesAtLocation = results.getEntitiesAtLocation(gpsLocation);
        if (entitiesAtLocation.isEmpty()) {
            return "";
        } else {
            return getMarkerColour(entitiesAtLocation.get(0));
        }
    }

    private String getMarkerColour(AssetSearchRecord assetSearchRecord) {

        ThingEvent nextOpenEvent = new NextEventScheduleLoader().setAssetId(assetSearchRecord.getId()).load();

        if (nextOpenEvent == null) {
            return GoogleMap.MapMarkerColour.GREEN.toString();
        } else {
            GoogleMap.MapMarkerColour markerColour;
            Boolean isAction =  nextOpenEvent.isAction();
            DateTime dueDate = new DateTime(nextOpenEvent.getDueDate());
            if (dueDate.isBeforeNow()) {
                    markerColour = isAction ? GoogleMap.MapMarkerColour.RED_A : GoogleMap.MapMarkerColour.RED;
            } else if (true ) {
                Interval interval30Days = new Interval(DateTime.now(), DateTime.now().plusDays(30));
                if (interval30Days.contains(dueDate)) {
                    markerColour = isAction ? GoogleMap.MapMarkerColour.YELLOW_A : GoogleMap.MapMarkerColour.YELLOW;
                } else {
                    markerColour = GoogleMap.MapMarkerColour.GREEN;
                }
            } else {
                markerColour = GoogleMap.MapMarkerColour.GREEN;
            }
            return markerColour.toString();
        }
    }


    protected String getMapMarkerText(AssetSearchRecord entity) {
        return getMapMarkerTextWithUrl(entity,"");
    }

    protected String getMapMarkerTextWithUrl(AssetSearchRecord entity) {
        String url = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(AssetSummaryPage.class, new PageParameters().add("uniqueID", entity.getId()).add("useContext", "false")).toString()));
        return getMapMarkerTextWithUrl(entity,String.format(MARKER_LINK_FORMAT,url,linkLabel));
    }

    protected String getMapMarkerTextWithUrl(AssetSearchRecord entity, String url) {
        return String.format(MARKER_FORMAT, entity.getType(), entity.getSerialNumber(), entity.getStatus(), entity.getOwner(), url );
    }

    @Override
    protected boolean isTableButtonVisible() {
        return criteriaModel.getObject().getQuery()==null;
    }

    @Override
    protected boolean isMapButtonVisible() {
        // don't show map in advanced search mode.
        return criteriaModel.getObject().getQuery()==null;
    }
}

