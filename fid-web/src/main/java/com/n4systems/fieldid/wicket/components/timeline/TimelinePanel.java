package com.n4systems.fieldid.wicket.components.timeline;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TimelinePanel<T> extends Panel {

    private IModel<List<T>> items;
    private TimePointInfoProvider<T> timePointInfoProvider;
    private DateFormat timelineJsDateFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
    private WebMarkupContainer timelineContainer;

    public TimelinePanel(String id, IModel<List<T>> items, TimePointInfoProvider<T> timePointInfoProvider) {
        super(id);
        this.items = items;
        this.timePointInfoProvider = timePointInfoProvider;
        setOutputMarkupPlaceholderTag(true);

        timelineContainer = new WebMarkupContainer("timeline");
        timelineContainer.setOutputMarkupId(true);
        add(timelineContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/timeline/storyjs-embed.js");
        response.renderOnDomReadyJavaScript(createTimelineInitJs());
    }

    private String createTimelineInitJs() {
        JsonObject timelineSourceConfig = new JsonObject();
        JsonObject timelineConfig = new JsonObject();

        timelineConfig.addProperty("type", "default");
        timelineConfig.add("date", createTimePoints());

        timelineSourceConfig.add("timeline", timelineConfig);

        JsonObject createStoryConfig  = new JsonObject();
        createStoryConfig.addProperty("type", "timeline");
        createStoryConfig.addProperty("width", "1000");
        createStoryConfig.addProperty("height", "600");
        createStoryConfig.add("source", timelineSourceConfig);
        createStoryConfig.addProperty("embed_id", timelineContainer.getMarkupId());

        StringBuffer createStoryJs = new StringBuffer();
        createStoryJs.append("createStoryJS(").append(createStoryConfig.toString()).append(");");
        return createStoryJs.toString();
    }

    private JsonArray createTimePoints() {
        JsonArray points = new JsonArray();

        for (T item : items.getObject()) {
            points.add(createTimePoint(item));
        }

        return points;
    }

    private JsonObject createTimePoint(T item) {
        JsonObject timePoint = new JsonObject();
        timePoint.addProperty("startDate", timelineJsDateFormat.format(timePointInfoProvider.getDate(item)));
        timePoint.addProperty("headline", timePointInfoProvider.getTitle(item));
        timePoint.addProperty("text", timePointInfoProvider.getText(item));

        JsonObject assetObject = new JsonObject();
        assetObject.addProperty("credit", "");
        assetObject.addProperty("caption", "");

        String mediaUrl = timePointInfoProvider.getMediaUrl(item);
        String thumbnailUrl = timePointInfoProvider.getThumbnailUrl(item);

        if (mediaUrl != null) {
            assetObject.addProperty("media", mediaUrl);
        }

        if (thumbnailUrl != null) {
            assetObject.addProperty("thumbnail", thumbnailUrl);
        }

        timePoint.add("asset", assetObject);
        return timePoint;
    }

}
