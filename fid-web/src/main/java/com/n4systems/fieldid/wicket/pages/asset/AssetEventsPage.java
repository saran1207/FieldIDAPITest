package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.events.*;
import com.n4systems.fieldid.wicket.data.EventByNetworkIdProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.List;

public class AssetEventsPage extends AssetPage{

    public AssetEventsPage(PageParameters params) {
        super(params);

        final Asset asset = assetModel.getObject();

        add(new HeaderPanel("header", assetModel, false));

        add(new Link<Void>("listLink") {
            @Override
            public void onClick() {
            }
        });

        add(new Link<Void>("groupLink") {
            @Override
            public void onClick() {
            }
        });

        add(new Link<Void>("mapLink") {
            @Override
            public void onClick() {
            }
        });


        add(new DefaultDataTable<Event>("eventsTable", getEventTableColumns(), new EventByNetworkIdProvider(asset.getNetworkId(), "schedule.completedDate", SortOrder.DESCENDING), 10));

    }
    
    private List<IColumn<Event>> getEventTableColumns() {
        List<IColumn<Event>> columns = new ArrayList<IColumn<Event>>();

        columns.add(new ResultIconColumn(new FIDLabelModel(""), "status"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event.state"),"schedule.status", "schedule.status"));
        columns.add(new EventDueColumn(new FIDLabelModel("label.due"), "schedule.nextDate", "date"));
        columns.add(new EventCompletedColumn(new FIDLabelModel("label.completed"), "schedule.completedDate", "date"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("title.viewevent"), "type.name", "type.name"));
        columns.add(new ResultColumn(new FIDLabelModel("label.result"), "status", "status.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.status"), "eventStatus", "eventStatus.displayName"));
        columns.add(new GpsIconColumn(new FIDLabelModel(""), "latitude"));
        columns.add(new ActionsColumn(new FIDLabelModel(""), "id"));
        return columns;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/events.css");
        response.renderCSSReference("style/tipsy/tipsy.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");

        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");

    }
}


